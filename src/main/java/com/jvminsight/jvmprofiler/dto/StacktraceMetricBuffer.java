package com.jvminsight.jvmprofiler.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.utils
 * @NAME: StacktraceMetricBuffer
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
public class StacktraceMetricBuffer {

    private AtomicLong lastResetMillis = new AtomicLong(System.currentTimeMillis());

    private volatile ConcurrentHashMap<Stacktrace, AtomicLong> metrics = new ConcurrentHashMap<>();

    public void appendValue(Stacktrace stacktrace) {
        AtomicLong counter = metrics.computeIfAbsent(stacktrace, key -> new AtomicLong(0));
        counter.incrementAndGet();
    }

    public long getLastResetMillis() {
        return lastResetMillis.get();
    }

    public Map<Stacktrace, AtomicLong> reset() {
        ConcurrentHashMap<Stacktrace, AtomicLong> oldCopy = metrics;
        metrics = new ConcurrentHashMap<>();

        lastResetMillis.set(System.currentTimeMillis());

        return oldCopy;
    }
}
