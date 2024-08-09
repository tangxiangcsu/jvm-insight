package com.jvminsight.jvmprofiler.utils;

import com.jvminsight.jvmprofiler.profilers.Profiler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.utils
 * @NAME: ProfilerRunner
 * @USER: tangxiang
 * @DATE: 2024/8/3
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Slf4j
public class ProfilerRunner implements Runnable{
    private static final int MAX_ERROR_COUNT_TO_LOG = 100;

    private final Profiler profiler;
    private final AtomicLong errorCounter = new AtomicLong(0);

    public ProfilerRunner(Profiler profiler) {
        this.profiler = profiler;
    }

    @Override
    public void run() {
        try {
            profiler.profile();
        } catch (Throwable e) {
            long errorCountValue = errorCounter.incrementAndGet();
            if (errorCountValue <= MAX_ERROR_COUNT_TO_LOG) {
                log.warn("Failed to run profile: " + profiler, e);
            } else {
                e.printStackTrace();
            }
        }
    }
}
