package com.jvminsight.jvmprofiler.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.ai
 * @NAME: JvmCpuMemoryEntry
 * @USER: tangxiang
 * @DATE: 2024/7/29
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JvmCpuMemoryEntry implements Serializable {

    /**
     * 应用 id
     */
    private String appId = UUID.randomUUID().toString();

    /**
     * 问题名称
     */
    private String name = "JVM只能优化平台";

    /**
     * 问题描述
     */
    private String desc = "请根据改Java程序执行过程中的CPU和Memory信息给出相应结果";

    /**
     * 问题类别
     */
    private String type = "内存调优";

    /**
     * 问题数
     */
    int problemNumber = 2;

    /**
     * 建议数
     */
    int suggestionNumber = 3;
}
