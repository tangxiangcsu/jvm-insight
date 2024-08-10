package com.jvminsight.jvmprofiler.args;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler
 * @NAME: ArgumentConstants
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public interface ArgumentConstants {
    public final static String DEFAULT_APP_ID_REGEX = "application_[\\w_]+";
    public final static long DEFAULT_METRIC_INTERVAL = 60000;
    public final static long DEFAULT_SAMPLE_INTERVAL = 100;

    public final static String ARG_NOOP = "noop";
    public final static String ARG_REPORTER = "reporter";
    public final static String ARG_CONFIG_PROVIDER = "configProvider";
    public final static String ARG_CONFIG_FILE = "configFile";
    public final static String ARG_METRIC_INTERVAL = "metricInterval";
    public final static String ARG_SAMPLE_INTERVAL = "sampleInterval";
    public final static String ARG_TAG = "tag";
    public final static String ARG_CLUSTER = "cluster";
    public final static String ARG_APP_ID_VARIABLE = "appIdVariable";
    public final static String ARG_APP_ID_REGEX = "appIdRegex";
    public final static String ARG_THREAD_PROFILING = "threadProfiling";
    public final static String ARG_DURATION_PROFILING = "durationProfiling";
    public final static String ARG_ARGUMENT_PROFILING = "argumentProfiling";

    public final static String ARG_IO_PROFILING = "ioProfiling";

    public static final long MIN_INTERVAL_MILLIS = 50;
}
