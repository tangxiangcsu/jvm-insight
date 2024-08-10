package com.jvminsight.jvmprofiler.reporter;

import com.google.gson.Gson;

import java.util.Map;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.reporter
 * @NAME: ConsoleOutputReporter
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public class ConsoleOutputReporter implements Reporter{
    @Override
    public void report(String profilerName, Map<String, Object> metrics) {
        System.out.println(String.format("ConsoleOutReporter - %s : %s",profilerName, new Gson().toJson(metrics)));
    }

    @Override
    public void close() {

    }
}
