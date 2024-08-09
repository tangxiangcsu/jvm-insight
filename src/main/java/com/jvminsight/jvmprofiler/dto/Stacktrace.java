package com.jvminsight.jvmprofiler.dto;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.utils
 * @NAME: Stacktrace
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
public class Stacktrace {

    private String threadName;
    private String threadState;
    private ClassAndMethod[] stack = new ClassAndMethod[0];

    @Data
    public static class ClassAndMethod{
        private final String className;
        private final String methodName;
    }
}
