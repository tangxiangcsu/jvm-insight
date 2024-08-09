package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.dto.ClassAndMethodMetric;
import com.jvminsight.jvmprofiler.reporter.ConsoleOutputReporter;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import com.jvminsight.jvmprofiler.utils.ClassMethodArgumentMetricBuffer;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.profilers
 * @NAME: MethodArgumentProfiler
 * @USER: tangxiang
 * @DATE: 2024/8/2
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
public class MethodArgumentProfiler extends ProfilerComm implements Profiler {
    public static final String PROFILER_NAME = "MethodArgument";

    private ClassMethodArgumentMetricBuffer buffer;

    private Reporter reporter = new ConsoleOutputReporter();

    private long intervalMillis = ProfilerConstants.DEFAULT_METRIC_INTERVAL;

    public MethodArgumentProfiler(ClassMethodArgumentMetricBuffer buffer, Reporter reporter) {
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
        if (buffer == null) {
            return;
        }

        if (reporter == null) {
            return;
        }

        Map<ClassAndMethodMetric, AtomicLong> metrics = buffer.reset();

        long epochMillis = System.currentTimeMillis();

        for (Map.Entry<ClassAndMethodMetric, AtomicLong> entry : metrics.entrySet()) {
            Map<String, Object> commonMap = new HashMap<>();

            commonMap.put("epochMillis", epochMillis);
            commonMap.put("processName", getProcessName());
            commonMap.put("host", getHostName());
            commonMap.put("processUuid", getProcessUuid());
            commonMap.put("appId", getAppId());

            commonMap.put("className", entry.getKey().getClassName());
            commonMap.put("methodName", entry.getKey().getMethodName());

            if (getTag() != null) {
                commonMap.put("tag", getTag());
            }

            if (getCluster() != null) {
                commonMap.put("cluster", getCluster());
            }

            if (getRole() != null) {
                commonMap.put("role", getRole());
            }

            {
                Map<String, Object> metricMap = new HashMap<>(commonMap);
                metricMap.put("metricName", entry.getKey().getMetricName());
                metricMap.put("metricValue", (double) entry.getValue().get());
                reporter.report(PROFILER_NAME, metricMap);
            }
        }
    }
}
