package com.jvminsight.jvmprofiler.args;

import com.jvminsight.jvmprofiler.dto.ClassAndMethod;
import com.jvminsight.jvmprofiler.dto.ClassMethodArgument;
import com.jvminsight.jvmprofiler.dto.ProfilerGroup;
import com.jvminsight.jvmprofiler.dto.StacktraceMetricBuffer;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import com.jvminsight.jvmprofiler.transformer.ClassAndMethodProfilerStaticProxy;
import com.jvminsight.jvmprofiler.transformer.JavaAgentFileTransformer;
import com.jvminsight.jvmprofiler.profilers.*;
import com.jvminsight.jvmprofiler.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.instrument.Instrumentation;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler
 * @NAME: AgentImpl
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 根据参数来获得所对应的对象，创建Profiler，启动Profiler
 **/
@Slf4j
public class AgentImpl {

    public static final String VERSION = "1.0.0";

    private static final int MAX_THREAD_POOL_SIZE = 2;

    private boolean started = false;

    public void run(Arguments arguments, Instrumentation instrumentation, Collection<AutoCloseable> objectsToCloseOnShutdown){

        if(arguments.isNoop()){
            log.info("Agent noop is true, do not run anything");
            return;
        }

        Reporter reporter = arguments.getReporter();

        String processUUid = UUID.randomUUID().toString();

        String appId = null;

        String appIdVariable = arguments.getAppid();

        if(!StringUtils.isEmpty(appIdVariable)){
            appId = System.getenv(appIdVariable);
        }
        /**
         * 空缺
         */
        if(!arguments.getDurationProfiler().isEmpty()||!arguments.getArgumentProfiling().isEmpty()){
            /**
             * instrumentation.Transformer会根据对象来修改类的字节码
             * 添加持续时间解析器和参数解析器
             */
            instrumentation.addTransformer(new JavaAgentFileTransformer(
                    arguments.getDurationProfiler(),
                    arguments.getArgumentProfiling()
            ),true);
            /**
             * 获得JVM当中已经加载的类
             */
            Set<String> loadedClasses = Arrays.stream(instrumentation.getAllLoadedClasses())
                    .map(Class::getName).collect(Collectors.toSet());
            /**
             * 获取需要重新加载的类
             */
            Set<String> reloadClasses = arguments.getDurationProfiler().stream()
                    .map(ClassAndMethod::getClassName).collect(Collectors.toSet());
            /**
             * 获得参数解析器所要加载的类
             */
            reloadClasses.addAll(arguments.getArgumentProfiling().stream().map(ClassMethodArgument::getClassName).collect(Collectors.toSet()));
            /**
             * 去除已加载的类
             */
            reloadClasses.retainAll(loadedClasses);

            reloadClasses.forEach(clazz -> {
                try{
                    instrumentation.retransformClasses(Class.forName(clazz));
                    log.info("Reload class [" + clazz + "] success");
                }catch (Exception e){
                    log.error("Reload class [" + clazz + "] failed", e);
                }
            });
        }
        List<Profiler> profilers = createProfilersForArgument(reporter, arguments, processUUid, appId);

        ProfilerGroup profilerGroup = startProfilers(profilers);

        Thread shutdownHook = new Thread(new ShutdownHookRunner(profilerGroup.getPeriodicProfilers(), Arrays.asList(reporter), objectsToCloseOnShutdown));
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    /**
     * todo 创建解析器
     * @param reporter
     * @param arguments
     * @param processId
     * @param appId
     * @return
     */
    private List<Profiler> createProfilersForArgument(Reporter reporter,Arguments arguments,String processId, String appId){
        String tag = arguments.getTag();
        String cluster = arguments.getCluster();
        long metricInterval = arguments.getMetricInteval();

        List<Profiler> profilers = new ArrayList<>();
        CpuAndMemoryProfiler cpuAndMemoryProfiler = new CpuAndMemoryProfiler(reporter);
        cpuAndMemoryProfiler.setTag(tag);
        cpuAndMemoryProfiler.setCluster(cluster);
        cpuAndMemoryProfiler.setIntervalMillis(metricInterval);
        cpuAndMemoryProfiler.setProcessUuid(processId);
        cpuAndMemoryProfiler.setAppId(appId);

        profilers.add(cpuAndMemoryProfiler);

        /*ProcessInfoProfiler processInfoProfiler = new ProcessInfoProfiler(reporter);
        processInfoProfiler.setTag(tag);
        processInfoProfiler.setCluster(cluster);
        processInfoProfiler.setProcessUuid(processUuid);
        processInfoProfiler.setAppId(appId);

        profilers.add(processInfoProfiler);*/

        if(arguments.isThreadProfiler()){// 注入线程解析器
            ThreadInfoProfiler threadInfoProfiler = new ThreadInfoProfiler(reporter);
            threadInfoProfiler.setTag(tag);
            threadInfoProfiler.setCluster(cluster);
            threadInfoProfiler.setIntervalMills(metricInterval);
            threadInfoProfiler.setProcessUuid(processId);
            threadInfoProfiler.setAppId(appId);

            profilers.add(threadInfoProfiler);
        }

        if (!arguments.getDurationProfiler().isEmpty()) {
            ClassAndMethodLongMetricBuffer classAndMethodMetricBuffer = new ClassAndMethodLongMetricBuffer();

            MethodDurationProfiler methodDurationProfiler = new MethodDurationProfiler(classAndMethodMetricBuffer, reporter);
            methodDurationProfiler.setTag(tag);
            methodDurationProfiler.setCluster(cluster);
            methodDurationProfiler.setIntervalMillis(metricInterval);
            methodDurationProfiler.setProcessUuid(processId);
            methodDurationProfiler.setAppId(appId);

            MethodDurationCollector methodDurationCollector = new MethodDurationCollector(classAndMethodMetricBuffer);
            ClassAndMethodProfilerStaticProxy.setCollector(methodDurationCollector);

            profilers.add(methodDurationProfiler);
        }

        if (!arguments.getArgumentProfiling().isEmpty()) {
            ClassMethodArgumentMetricBuffer classAndMethodArgumentBuffer = new ClassMethodArgumentMetricBuffer();

            MethodArgumentProfiler methodArgumentProfiler = new MethodArgumentProfiler(classAndMethodArgumentBuffer, reporter);
            methodArgumentProfiler.setTag(tag);
            methodArgumentProfiler.setCluster(cluster);
            methodArgumentProfiler.setIntervalMillis(metricInterval);
            methodArgumentProfiler.setProcessUuid(processId);
            methodArgumentProfiler.setAppId(appId);

            MethodArgumentCollector methodArgumentCollector = new MethodArgumentCollector(classAndMethodArgumentBuffer);
            ClassAndMethodProfilerStaticProxy.setArgumentCollector(methodArgumentCollector);

            profilers.add(methodArgumentProfiler);
        }

        if (arguments.getSampleInterval() > 0) {
            StacktraceMetricBuffer stacktraceMetricBuffer = new StacktraceMetricBuffer();

            StackTraceCollector stackTraceCollector = new StackTraceCollector(stacktraceMetricBuffer, AgentThreadFactory.NAME_PREFIX);
            stackTraceCollector.setIntervalMillis(arguments.getSampleInterval());

            StackTraceProfiler stacktraceReporterProfiler = new StackTraceProfiler(stacktraceMetricBuffer, reporter);
            stacktraceReporterProfiler.setTag(tag);
            stacktraceReporterProfiler.setCluster(cluster);
            stacktraceReporterProfiler.setIntervalMillis(metricInterval);
            stacktraceReporterProfiler.setProcessUuid(processId);
            stacktraceReporterProfiler.setAppId(appId);

            profilers.add(stackTraceCollector);
            profilers.add(stacktraceReporterProfiler);
        }

        if (arguments.isIoProfiler()) {
            IOProfiler ioProfiler = new IOProfiler(reporter);
            ioProfiler.setTag(tag);
            ioProfiler.setCluster(cluster);
            ioProfiler.setIntervalMillis(metricInterval);
            ioProfiler.setProcessUuid(processId);
            ioProfiler.setAppId(appId);

            profilers.add(ioProfiler);
        }

        return profilers;

    }

    /**
     * todo 启动采集器
     */
    public ProfilerGroup startProfilers(Collection<Profiler> profilers){
        if(started){
            log.warn("Profilers already started, do not start it again");
            return new ProfilerGroup(new ArrayList<>(), new ArrayList<>());
        }

        List<Profiler> oneTimeProfilers = new ArrayList<>();
        List<Profiler> periodicProfilers = new ArrayList<>();

        for(Profiler profiler : profilers){
            if (profiler.getIntervalMillis() == 0) {
                oneTimeProfilers.add(profiler);
            } else if (profiler.getIntervalMillis() > 0) {
                periodicProfilers.add(profiler);
            } else {
                log.info(String.format("Ignored profiler %s due to its invalid interval %s", profiler, profiler.getIntervalMillis()));
            }
        }
        /**
         * 立即启动解析器
         */
        for(Profiler profiler : oneTimeProfilers){
            try {
                profiler.profile();
                log.info("Finished one time profiler: " + profiler);
            } catch (Exception ex) {
                log.warn("Failed to run one time profiler: " + profiler, ex);
            }
        }

        /**
         * 也先启动按计划的启动器
         */
        for (Profiler profiler : periodicProfilers) {
            try {
                profiler.profile();
                log.info("Ran periodic profiler (first run): " + profiler);
            } catch (Throwable ex) {
                log.warn("Failed to run periodic profiler (first run): " + profiler, ex);
            }
        }

        /**
         * 按计划启动
         */

        started = true;
        return new ProfilerGroup(oneTimeProfilers, periodicProfilers);

    }
    /**
     * todo 按计划启动采集器
     */
    private void scheduleProfilers(Collection<Profiler> profilers){
        int threadPoolSize = Math.min(profilers.size(), MAX_THREAD_POOL_SIZE);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threadPoolSize, new AgentThreadFactory());
        for (Profiler profiler : profilers) {
            if (profiler.getIntervalMillis() < Arguments.MIN_INTERVAL_MILLIS) {
                throw new RuntimeException("Interval too short for profiler: " + profiler + ", must be at least " + Arguments.MIN_INTERVAL_MILLIS);
            }

            ProfilerRunner worker = new ProfilerRunner(profiler);
            scheduledExecutorService.scheduleAtFixedRate(worker, 0, profiler.getIntervalMillis(), TimeUnit.MILLISECONDS);
            log.info(String.format("Scheduled profiler %s with interval %s millis", profiler, profiler.getIntervalMillis()));
        }
    }
}
