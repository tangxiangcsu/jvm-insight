package com.jvminsight.jvmprofiler.config;

import com.jvminsight.jvmprofiler.args.ConfigProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.jvminsight.jvmprofiler.config
 * @NAME: EmptyConfigProvider
 * @USER: tangxiang
 * @DATE: 2024/8/11
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public class EmptyConfigProvider implements ConfigProvider {
    @Override
    public Map<String, Map<String, List<String>>> getConfig() {
        return new HashMap<>();
    }
}
