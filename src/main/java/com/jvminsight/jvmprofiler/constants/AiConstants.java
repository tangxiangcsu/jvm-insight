package com.jvminsight.jvmprofiler.constants;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.ai
 * @NAME: AiConstants
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: ai启动常量
 **/
public interface AiConstants {
    // region AI 生成题目功能
    String GENERATE_JVM_CPU_AND_MEMORY_OPTIMIZATION_SYSTEM_MESSAGE = "我有以下 JVM 运行时的 CPU 和内存使用数据，我想请你根据这些数据提供 JVM 存在问题和调优建议：\n" +
            "```\n" +
            "问题名称，\n" +
            "【【【问题描述】】】，\n" +
            "问题类别，\n" +
            "当前JVM可能存在问题个数，\n" +
            "JVM调优建议条数，\n" +
            "```\n" +
            "\n" +
            "请根据以上数据提供 JVM 内存和 CPU 调优的策略和建议，以提高性能和效率：\n" +
            "1. 要求：存在的问题和调优策略尽可能地短，调优策略不要包含序号，优化策略个数由我提供，题目不能重复\n" +
            "2. 严格按照下面的 json 格式输出题目和选项\n" +
            "```\n" +
            "[{\"problems\":[{\"description\":\"JVM可能存在问题\",\"key\":\"（1）\"},{\"description\":\"\",\"key\":\"（2）\"}],\"suggestions\":[{\"description\":\"JVM调优建议\",\"key\":\"（1）\"},{\"description\":\"\",\"key\":\"（2）\"}]}]\n" +

            "```\n" +
            "problems 是问题，suggestions 是建议，每个回复的 key 按照中文序（比如 1、2、3、4）以此类推，description 回答内容\n" +
            "3. 检查所有回答是否包含序号，若包含序号则去除序号\n" +
            "4. 返回的题目列表格式必须为 JSON 数组";
}
