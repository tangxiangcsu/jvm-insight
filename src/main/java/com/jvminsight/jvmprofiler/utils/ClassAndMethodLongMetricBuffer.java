package com.jvminsight.jvmprofiler.utils;

import com.jvminsight.jvmprofiler.dto.ClassAndMethodMetric;
import com.jvminsight.jvmprofiler.dto.Histogram;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.utils
 * @NAME: ClassAndMethodLongMetricBuffer
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: 存储方法执行时间指标的缓冲区，appendValue和reset都是线程安全的
 **/
public class ClassAndMethodLongMetricBuffer {
    private volatile ConcurrentHashMap<ClassAndMethodMetric, Histogram> metrics = new ConcurrentHashMap<ClassAndMethodMetric, Histogram>();

    public void appendValue(String className, String methodName, String metricName, long value){
        ClassAndMethodMetric methodMetric = new ClassAndMethodMetric(className, methodName, metricName);
        Histogram histogram = metrics.computeIfAbsent(methodMetric, key -> new Histogram());
        histogram.appendValue(value);
    }

    public Map<ClassAndMethodMetric, Histogram> reset(){
        ConcurrentHashMap<ClassAndMethodMetric, Histogram> oldCopy = metrics;
        metrics = new ConcurrentHashMap<>();
        return oldCopy;
    }

}
