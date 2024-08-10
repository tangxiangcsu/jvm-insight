package com.jvminsight.jvmprofiler.dto;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.dto
 * @NAME: ClassMethodArgument
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
@Data
public class ClassMethodArgument {
    private final String className;
    private final String methodName;
    private final int argumentIndex;
}
