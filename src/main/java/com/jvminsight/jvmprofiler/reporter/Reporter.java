package com.jvminsight.jvmprofiler.reporter;

import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.reporter
 * @NAME: Reporter
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 报告器接口
 **/
public interface Reporter {

    default void updateArguments(Map<String, List<String>> parsedArgs) {
    }

    void report(String profilerName, Map<String, Object> metrics);

    void close();

}
