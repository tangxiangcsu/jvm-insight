package com.jvminsight.jvmprofiler.reporter;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.reporter
 * @NAME: RedisOutputReporter
 * @USER: tangxiang
 * @DATE: 2024/7/30
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/

@Slf4j
public class RedisOutputReporter implements Reporter{
    @Override
    public void updateArguments(Map<String, List<String>> parsedArgs) {

    }

    @Override
    public void report(String profilerName, Map<String, Object> metrics) {

    }

    @Override
    public void close() {

    }
}
