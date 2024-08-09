package com.jvminsight.jvmprofiler.args;

import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.args
 * @NAME: ConfigProvider
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
public interface ConfigProvider {
    Map<String, Map<String, List<String>>> getConfig();
}
