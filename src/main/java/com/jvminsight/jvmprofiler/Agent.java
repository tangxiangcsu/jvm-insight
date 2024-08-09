package com.jvminsight.jvmprofiler;


import com.jvminsight.jvmprofiler.args.AgentImpl;
import com.jvminsight.jvmprofiler.args.Arguments;

import java.lang.instrument.Instrumentation;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler
 * @NAME: Agent
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: 服务启动代理,负责解析掺入的参数
 **/
public final class Agent {

    private static AgentImpl agentImpl = new AgentImpl();

    private Agent() {
    }

    // Java SE6开始，提供了在应用程序的VM启动后在动态添加代理的方式，即agentmain方式
    public static void agentmain(final String args, final Instrumentation instrumentation) {
        premain(args, instrumentation);
    }

    // premain是Java SE5开始就提供的代理方式，其必须在命令行指定代理jar，并且代理类必须在main方法前启动。
    public static void premain(final String args, final Instrumentation instrumentation) {
        System.out.println("Java Agent " + AgentImpl.VERSION + " premain args: " + args);

        // 解析参数
        Arguments arguments = Arguments.parseArgs(args);

        // 解析参数中指定的配资文件内的配置信息，覆盖传参带入的参数
        arguments.runConfigProvider();

        agentImpl.run(arguments, instrumentation, null);
    }
}
