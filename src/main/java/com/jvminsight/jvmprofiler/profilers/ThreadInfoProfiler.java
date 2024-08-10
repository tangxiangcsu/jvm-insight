package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.common.ErrorCode;
import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.exception.JVMException;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import lombok.Data;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: ThreadInfoProfiler
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: JVM线程分析器，包括总线程数、峰值线程数、实时/活动线程数和新线程数。
 **/
@Data
public class ThreadInfoProfiler extends ProfilerComm implements Profiler{
    /**
     * 解析器名称
     */
    public final static String PROFILER_NAME = "ThreadInfomattion";
    /**
     * 报告间隔时间
     */
    private long intervalMills = ProfilerConstants.DEFAULT_METRIC_INTERVAL;
    /**
     * 上次Profiler启动时的总线程数
     */
    private long previousTotalStartedThread = 0L;
    /**
     * 线程管理器
     */
    private ThreadMXBean threadMXBean;
    /**
     * 报告器
     */
    private Reporter reporter;

    public ThreadInfoProfiler(Reporter reporter){
        this.reporter = reporter;
        init();
    }

    private void init(){
        try{
            this.threadMXBean = ManagementFactory.getThreadMXBean();
        }catch (Exception e){
            throw new JVMException(ErrorCode.SYSTEM_ERROR,e.toString());
        }
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
        /**
         * JVM总线程数
         */
        long totalStartedThreadCount = 0L;
        /**
         * 当前活跃线程数
         */
        int liveThreadCount = 0;
        /**
         * JVM峰值线程数
         */
        int peekThreadCount = 0;
        /**
         * 新线程数
         */
        long newThreadCount = 0;

        if(threadMXBean != null){
            liveThreadCount = threadMXBean.getThreadCount();
            peekThreadCount = threadMXBean.getPeakThreadCount();
            totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount();
            newThreadCount = totalStartedThreadCount - this.previousTotalStartedThread;
        }

        Map<String, Object> map = new HashMap<>();

        map.put("epochMillis", System.currentTimeMillis());
        map.put("name", getProcessName());
        map.put("host", getHostName());
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

        map.put("totalStartedThreadCount", totalStartedThreadCount);
        map.put("newThreadCount", newThreadCount);
        map.put("liveThreadCount", liveThreadCount);
        map.put("peakThreadCount", peekThreadCount);

        if (reporter != null) {
            reporter.report(PROFILER_NAME, map);
        }

    }
}
