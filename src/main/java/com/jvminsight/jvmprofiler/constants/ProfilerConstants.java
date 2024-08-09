package com.jvminsight.jvmprofiler.constants;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.profilers
 * @NAME: ProfilerConstants
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
public interface ProfilerConstants {
    /**
     * 默认扫描时间间隔
     */
    long DEFAULT_METRIC_INTERVAL = 60000;
    /**
     * 最大字符串长度
     */
    int MAX_STRING_LENGTH = 800000;
    /**
     * 执行器
     */
    String EXECUTOR_ROLE = "executor";
    /**
     * 驱动器
     */
    String DRIVER_ROLE = "driver";
}
