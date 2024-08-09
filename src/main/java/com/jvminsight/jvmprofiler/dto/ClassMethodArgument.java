package com.jvminsight.jvmprofiler.dto;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.dto
 * @NAME: ClassMethodArgument
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
public class ClassMethodArgument {
    private final String className;
    private final String methodName;
    private final int argumentIndex;
}
