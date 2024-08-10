package com.jvminsight.jvmprofiler.utils;

import com.jvminsight.jvmprofiler.dto.ClassAndMethodMetric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.utils
 * @NAME: ClassMethodArgumentMetricBuffer
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public class ClassMethodArgumentMetricBuffer {
    private volatile ConcurrentHashMap<ClassAndMethodMetric, AtomicLong> metrics = new ConcurrentHashMap<>();

    public void appendValue(String className, String methodName, String argument) {
        ClassAndMethodMetric methodMetricKey = new ClassAndMethodMetric(className, methodName, argument);
        AtomicLong counter = metrics.computeIfAbsent(methodMetricKey, key -> new AtomicLong(0));
        counter.incrementAndGet();
    }

    public Map<ClassAndMethodMetric, AtomicLong> reset() {
        ConcurrentHashMap<ClassAndMethodMetric, AtomicLong> oldCopy = metrics;
        metrics = new ConcurrentHashMap<>();
        return oldCopy;
    }
}
