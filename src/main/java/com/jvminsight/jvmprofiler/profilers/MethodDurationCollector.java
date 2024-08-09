package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.utils.ClassAndMethodLongMetricBuffer;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.profilers
 * @NAME: MethodDurationCollector
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: 方法持续时间收集器
 **/
public class MethodDurationCollector {
    private ClassAndMethodLongMetricBuffer buffer;

    public MethodDurationCollector(ClassAndMethodLongMetricBuffer buffer) {
        this.buffer = buffer;
    }

    public void collectLongMetric(String className, String methodName, String metricName, long metricValue) {
        buffer.appendValue(className, methodName, metricName, metricValue);
    }
}