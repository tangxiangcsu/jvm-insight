package com.jvminsight.jvmprofiler.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.utils
 * @NAME: AgentThreadFactory
 * @USER: tangxiang
 * @DATE: 2024/8/2
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
public class AgentThreadFactory implements ThreadFactory {
    public static final String NAME_PREFIX = "tx_java_agent";

    private final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = defaultThreadFactory.newThread(r);
        if (thread != null) {
            thread.setDaemon(true);
            thread.setName(String.format("%s-%s", NAME_PREFIX, thread.getName()));
        }
        return thread;
    }
}
