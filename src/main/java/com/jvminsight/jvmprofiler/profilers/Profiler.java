package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.reporter.Reporter;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: Profiler
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public interface Profiler {

    long getIntervalMillis();

    void setReporter(Reporter reporter);

    void profile();
}
