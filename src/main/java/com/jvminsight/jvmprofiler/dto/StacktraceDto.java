package com.jvminsight.jvmprofiler.dto;

import lombok.Data;

import java.util.List;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.dto
 * @NAME: StacktraceDto
 * @USER: tangxiang
 * @DATE: 2024/8/4
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
@Data
public class StacktraceDto {
    private long startEpoch;
    private long endEpoch;
    private String host;
    private String name;
    private String processUuid;
    private String appId;
    private String tag;
    private String cluster;
    private String role;
    private String threadName;
    private String threadState;
    private List<String> stacktrace;
    private long count;

    @Data
    public static class ClassAndMethod {
        private String className;
        private String methodName;
    }
}