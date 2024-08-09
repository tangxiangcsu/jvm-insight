package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.utils.ClassMethodArgumentMetricBuffer;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.profilers
 * @NAME: MethodArgumentCollector
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
public class MethodArgumentCollector {
    private ClassMethodArgumentMetricBuffer buffer;

    public MethodArgumentCollector(ClassMethodArgumentMetricBuffer buffer) {
        this.buffer = buffer;
    }

    public void collectMetric(String className, String methodName, String argument) {
        if (argument == null) {
            argument = "";
        }

        if (argument.length() > ProfilerConstants.MAX_STRING_LENGTH) {
            argument = argument.substring(0, ProfilerConstants.MAX_STRING_LENGTH);
        }

        buffer.appendValue(className, methodName, argument);
    }
}
