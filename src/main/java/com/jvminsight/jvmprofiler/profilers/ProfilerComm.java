package com.jvminsight.jvmprofiler.profilers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: ProfilerComm
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 常用JVM分析Field
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilerComm {

    private String tag = null;
    private String cluster = null;
    private String hostName = null;
    private String processName = null;
    private String processUuid = UUID.randomUUID().toString();

    private String jobId = null;
    private String appId = null;

    private String role = null;
}
