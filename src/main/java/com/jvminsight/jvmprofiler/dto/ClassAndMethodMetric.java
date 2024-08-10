package com.jvminsight.jvmprofiler.dto;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.dto
 * @NAME: ClassAndMethodMetric
 * @USER: tangxiang
 * @DATE: 2024/7/31
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
@Data
public class ClassAndMethodMetric {

    private final String className;
    private final String methodName;
    private final String metricName;
}
