package com.jvminsight.jvmprofiler.reporter;

import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.reporter
 * @NAME: Reporter
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: 报告器接口
 **/
public interface Reporter {

    default void updateArguments(Map<String, List<String>> parsedArgs) {
    }

    void report(String profilerName, Map<String, Object> metrics);

    void close();

}
