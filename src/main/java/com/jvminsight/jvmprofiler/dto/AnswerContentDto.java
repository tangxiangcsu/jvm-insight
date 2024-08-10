package com.jvminsight.jvmprofiler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.ai
 * @NAME: AnswerContentDTO
 * @USER: tangxiang
 * @DATE: 2024/7/29
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public class AnswerContentDto {
    /**
     * 存在问题列表
     */
    private List<Answer> problems;

    /**
     * 给出建议列表
     */
    private List<Answer> suggestions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Answer {
        private String description;
        private String key;
    }
}
