package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import com.jvminsight.jvmprofiler.dto.Stacktrace;
import com.jvminsight.jvmprofiler.dto.StacktraceMetricBuffer;
import lombok.Data;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.profilers
 * @NAME: StackTraceProfiler
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: 通过JMX获取线程转储来收集堆栈跟踪，并将堆栈跟踪存储到给定的缓冲区
 **/
@Data
public class StackTraceProfiler extends ProfilerComm implements Profiler{
    /**
     * 解析器名称
     */
    public static final String PROFILER_NAME = "Stacktrace";
    /**
     * 报道器
     */
    private Reporter reporter;

    /**
     * 报道间隔
     */
    private long intervalMillis = ProfilerConstants.DEFAULT_METRIC_INTERVAL;
    /**
     * 栈追踪缓冲区
     */
    private StacktraceMetricBuffer buffer;
    /**
     * 忽略线程名前缀
     */
    private String ignoreThreadNamePrefix = "";
    /**
     * 最大String长度
     */
    private int maxStringLength = ProfilerConstants.MAX_STRING_LENGTH;

    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public StackTraceProfiler(StacktraceMetricBuffer buffer, Reporter reporter) {
        this.buffer = buffer;
        this.reporter = reporter;
    }


    @Override
    public long getIntervalMillis() {
        return 0;
    }

    @Override
    public void setReporter(Reporter reporter) {

    }

    @Override
    public void profile() {
        if(buffer == null)return;

        if(reporter == null)return;

        /**
         * 封装返回参数
         */
        long startEpoch = buffer.getLastResetMillis();

        Map<Stacktrace, AtomicLong> metrics = buffer.reset();

        long endEpoch = buffer.getLastResetMillis();

        for (Map.Entry<Stacktrace, AtomicLong> entry : metrics.entrySet()) {
            Map<String, Object> map = new HashMap<>();

            map.put("startEpoch", startEpoch);
            map.put("endEpoch", endEpoch);

            map.put("host", getHostName());
            map.put("name", getProcessName());
            map.put("processUuid", getProcessUuid());
            map.put("appId", getAppId());

            if (getTag() != null) {
                map.put("tag", getTag());
            }

            if (getCluster() != null) {
                map.put("cluster", getCluster());
            }

            if (getRole() != null) {
                map.put("role", getRole());
            }

            Stacktrace stacktrace = entry.getKey();

            map.put("threadName", stacktrace.getThreadName());
            map.put("threadState", stacktrace.getThreadState());

            Stacktrace.ClassAndMethod[] classAndMethodArray = stacktrace.getStack();
            if (classAndMethodArray!= null) {
                List<String> stackArray = new ArrayList<>(classAndMethodArray.length);
                for (int i = 0; i < classAndMethodArray.length; i++) {
                    Stacktrace.ClassAndMethod classAndMethod = classAndMethodArray[i];
                    stackArray.add(classAndMethod.getClassName() + "." + classAndMethod.getMethodName());
                }
                map.put("stacktrace", stackArray);
            }

            map.put("count", entry.getValue().get());

            reporter.report(PROFILER_NAME, map);
        }
    }
}
