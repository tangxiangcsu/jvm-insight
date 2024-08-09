package com.jvminsight.jvmprofiler.ai;

import lombok.Data;

import java.io.Serializable;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.ai
 * @NAME: AiGenerateQuestionRequest
 * @USER: tangxiang
 * @DATE: 2024/7/29
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
public class AiJvmOptimizationAWRequest implements Serializable {

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 问题数
     */
    int problemNumber = 2;

    /**
     * 建议数
     */
    int suggestionNumber = 3;

    private static final long serialVersionUID = 1L;
}
