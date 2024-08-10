package com.jvminsight.jvmprofiler.utils;

import com.jvminsight.jvmprofiler.profilers.Profiler;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.utils
 * @NAME: ShutdownHookRunner
 * @USER: tangxiang
 * @DATE: 2024/8/3
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 关闭解析器的线程
 **/
@Slf4j
public class ShutdownHookRunner implements Runnable{
    private List<Profiler> profilers;
    private List<Reporter> reporters;
    private List<AutoCloseable> closeables;
    public ShutdownHookRunner(Collection<Profiler> profilers, Collection<Reporter> reporters, Collection<AutoCloseable> objectsToCloseOnShutdown) {
        this.profilers = profilers == null ? new ArrayList<>() : new ArrayList<>(profilers);
        this.reporters = reporters == null ? new ArrayList<>() : new ArrayList<>(reporters);
        this.closeables = objectsToCloseOnShutdown == null ? new ArrayList<>() : new ArrayList<>(objectsToCloseOnShutdown);
    }

    @Override
    public void run() {
        log.info("stated to shutdown java agent");
        for (Profiler profiler : profilers) {
            try {
                log.info("Running periodic profiler (last run): " + profiler);
                profiler.profile();
                log.info("Ran periodic profiler (last run): " + profiler);
            } catch (Exception ex) {
                log.warn("Failed to run periodic profiler (last run): " + profiler, ex);
            }
        }

        for (Reporter r : reporters) {
            try {
                log.info("Closing reporter " + r);
                r.close();
                log.info("Closed reporter " + r);
            } catch (Exception ex) {
                log.warn("Failed to close reporter " + r + ", " + new Date() + ", " + System.currentTimeMillis(), ex);
            }
        }

        for (AutoCloseable closeable : closeables) {
            // Do not use logger.warn here because the logger may depend on error log reporter which will be already closed here.
            // So we use logShutdownMessage (System.out.println) to print out logs.
            try {
                log.info("Closing object " + closeable);
                closeable.close();
                log.info("Closed object " + closeable );
            } catch (Exception ex) {
                log.info("Failed to close object " + closeable);
                ex.printStackTrace();
            }
        }
    }
}
