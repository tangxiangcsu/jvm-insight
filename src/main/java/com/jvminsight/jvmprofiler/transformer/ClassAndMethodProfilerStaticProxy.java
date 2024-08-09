package com.jvminsight.jvmprofiler.transformer;

import com.jvminsight.jvmprofiler.profilers.MethodArgumentCollector;
import com.jvminsight.jvmprofiler.profilers.MethodDurationCollector;
import lombok.Data;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.transformer
 * @NAME: ClassAndMethodProfilerStaticProxy
 * @USER: tangxiang
 * @DATE: 2024/8/2
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
@Data
public class ClassAndMethodProfilerStaticProxy {
    private static MethodDurationCollector methodDurationCollectorSingleton;
    private static MethodArgumentCollector argumentCollectorSingleton;

    public static void setCollector(MethodDurationCollector collector) {
        methodDurationCollectorSingleton = collector;
    }

    public static void setArgumentCollector(MethodArgumentCollector collector) {
        argumentCollectorSingleton = collector;
    }
}
