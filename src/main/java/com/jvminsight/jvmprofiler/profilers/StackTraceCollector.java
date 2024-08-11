package com.jvminsight.jvmprofiler.profilers;

import com.jvminsight.jvmprofiler.constants.ProfilerConstants;
import com.jvminsight.jvmprofiler.dto.Stacktrace;
import com.jvminsight.jvmprofiler.dto.StacktraceMetricBuffer;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import lombok.Data;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.profilers
 * @NAME: StackTraceCollector
 * @USER: tangxiang
 * @DATE: 2024/8/2
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
@Data
public class StackTraceCollector implements Profiler{
    private long intervalMillis;
    private StacktraceMetricBuffer buffer;
    private String ignoreThreadNamePrefix = "";
    private int maxStringLength = ProfilerConstants.MAX_STRING_LENGTH;
    private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public StackTraceCollector(StacktraceMetricBuffer buffer, String ignoreThreadNamePrefix) {
        this(buffer, ignoreThreadNamePrefix, ProfilerConstants.MAX_STRING_LENGTH);
    }

    public StackTraceCollector(StacktraceMetricBuffer buffer, String ignoreThreadNamePrefix, int maxStringLength) {
        this.buffer = buffer;
        this.ignoreThreadNamePrefix = ignoreThreadNamePrefix == null ? "" : ignoreThreadNamePrefix;
        this.maxStringLength = maxStringLength;
    }



    @Override
    public long getIntervalMillis() {
        return this.intervalMillis;
    }

    @Override
    public void setReporter(Reporter reporter) {
        // this.reporter = reporter;
    }

    @Override
    public void profile() {
        /**
         * 获取所有线程信息
         */
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        if (threadInfos == null) {
            return;
        }
        /**
         * 遍历所有线程信息
         */
        for (ThreadInfo threadInfo : threadInfos) {
            String threadName = threadInfo.getThreadName();
            if (threadName == null) {
                threadName = "";
            }
            /**
             * 过滤ignoreThreadName
             */
            if (!ignoreThreadNamePrefix.isEmpty()
                    && threadName.startsWith(ignoreThreadNamePrefix)) {
                continue;
            }

            StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
            Stacktrace stacktrace = new Stacktrace();
            stacktrace.setThreadName(threadName);
            stacktrace.setThreadState(String.valueOf(threadInfo.getThreadState()));

            /**
             * 获取线程的栈信息
             */
            int totalLength = 0;
            List<Stacktrace.ClassAndMethod> stack = new ArrayList<>(stackTraceElements.length);
            for(int i = stackTraceElements.length - 1; i >= 0; i--){
                StackTraceElement stackTraceElement = stackTraceElements[i];
                String className = String.valueOf(stackTraceElement.getClassName());
                String methodName = String.valueOf(stackTraceElement.getMethodName());
                stack.add(new Stacktrace.ClassAndMethod(className, methodName));

                totalLength += className.length() + methodName.length();
                if (totalLength >= maxStringLength) {
                    stack.add(new Stacktrace.ClassAndMethod("_stack_", "_trimmed_"));
                    break;
                }
            }
            // 反转栈列表
            Stacktrace.ClassAndMethod[] classAndMethodArray = new Stacktrace.ClassAndMethod[stack.size()];
            for (int i = 0; i < stack.size(); i++) {
                classAndMethodArray[classAndMethodArray.length - 1 - i] = stack.get(i);
            }

            stacktrace.setStack(classAndMethodArray);

            buffer.appendValue(stacktrace);
        }
    }
}
