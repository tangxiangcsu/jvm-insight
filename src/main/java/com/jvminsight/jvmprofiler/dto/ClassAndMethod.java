package com.jvminsight.jvmprofiler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.dto
 * @NAME: ClassAndMethod
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
public class ClassAndMethod {
    private final String className;
    private final String methodName;
}
