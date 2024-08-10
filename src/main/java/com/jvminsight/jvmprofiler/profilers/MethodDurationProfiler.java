package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.dto.ClassAndMethodMetric;
import com.jvminsight.jvmprofiler.dto.Histogram;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import com.jvminsight.jvmprofiler.utils.ClassAndMethodLongMetricBuffer;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: MethodDurationProfiler
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 方法耗时采集，在方法前置和后置嵌入采集耗时的代码
 **/
@Data
public class MethodDurationProfiler extends ProfilerComm implements Profiler{

    public static final String PROFILER_NAME = "MethodDuration";

    private ClassAndMethodLongMetricBuffer buffer;

    private Reporter reporter ;

    private long intervalMillis = ProfilerConstants.DEFAULT_METRIC_INTERVAL;

    public MethodDurationProfiler(ClassAndMethodLongMetricBuffer buffer, Reporter reporter) {
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
        Map<ClassAndMethodMetric, Histogram> metircs = buffer.reset();

        long epochMills = System.currentTimeMillis();
        for(Map.Entry<ClassAndMethodMetric,Histogram> entry: metircs.entrySet()){
            Map<String, Object> commonMap = new HashMap<>();

            commonMap.put("epochMillis", epochMills);
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
                metricMap.put("metricName", entry.getKey().getMetricName() + ".count");
                metricMap.put("metricValue", (double) entry.getValue().getCount());
                reporter.report(PROFILER_NAME, metricMap);
            }
            {
                Map<String, Object> metricMap = new HashMap<>(commonMap);
                metricMap.put("metricName", entry.getKey().getMetricName() + ".sum");
                metricMap.put("metricValue", (double) entry.getValue().getSum());
                reporter.report(PROFILER_NAME, metricMap);
            }
            {
                Map<String, Object> metricMap = new HashMap<>(commonMap);
                metricMap.put("metricName", entry.getKey().getMetricName() + ".min");
                metricMap.put("metricValue", (double) entry.getValue().getMin());
                reporter.report(PROFILER_NAME, metricMap);
            }
            {
                Map<String, Object> metricMap = new HashMap<>(commonMap);
                metricMap.put("metricName", entry.getKey().getMetricName() + ".max");
                metricMap.put("metricValue", (double) entry.getValue().getMax());
                reporter.report(PROFILER_NAME, metricMap);
            }
        }
    }
}
