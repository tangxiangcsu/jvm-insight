package com.jvminsight.jvmprofiler.args;

import cn.hutool.json.JSONUtil;
import com.jvminsight.jvmprofiler.config.EmptyConfigProvider;
import com.jvminsight.jvmprofiler.reporter.ConsoleOutputReporter;
import com.jvminsight.jvmprofiler.utils.ReflectionUtils;
import com.jvminsight.jvmprofiler.common.ErrorCode;
import com.jvminsight.jvmprofiler.dto.ClassAndMethod;
import com.jvminsight.jvmprofiler.dto.ClassMethodArgument;
import com.jvminsight.jvmprofiler.exception.JVMException;
import com.jvminsight.jvmprofiler.reporter.Reporter;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jvminsight.jvmprofiler.args.ArgumentConstants.*;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler
 * @NAME: Arguments
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 指定命令提供哪些参数
 **/

@Data
public class Arguments {
    /**
     * noop
     */
    private boolean noop = false;

    /**
     * 报道器构造器
     */
    private Constructor<Reporter> reporterConstructor;
    /**
     * 配置文件构造器
     */
    private Constructor<ConfigProvider> configProvierConstructor;
    /**
     * 配置文件
     */
    private String configFile;
    /**
     * appId
     */
    private String appid;
    /**
     * 存疑
     */
    private long sampleInterval = 0L;
    /**
     * appId正则表达式
     */
    private String appIdReges = ArgumentConstants.DEFAULT_APP_ID_REGEX;
    /**
     * 指标测量间隔时间
     */
    private long metricInteval = ArgumentConstants.DEFAULT_METRIC_INTERVAL;
    /**
     * 标签
     */
    private String tag;
    /**
     * 集群
     */
    private String cluster;
    /**
     * 是否开启线程分析
     */
    private boolean threadProfiler = false;
    /**
     * 是否开启IO流分析
     */
    private boolean ioProfiler = false;
    /**
     * 方法持续时间分析
     */
    private List<ClassAndMethod> durationProfiler = new ArrayList<>();
    /**
     * 参数解析时间
     */
    private List<ClassMethodArgument> argumentProfiling = new ArrayList<>();
    /**
     * 最小的解析器间隔时间
     */
    public static final long MIN_INTERVAL_MILLIS = 50;
    /**
     * 原始参数
     */
    private Map<String, List<String>> rawArgValues = new HashMap<>();

    private Arguments(Map<String, List<String>> parseArgs){
        updateArguments(parseArgs);
    }
    /**
     * 参数分析
     */
    public static Arguments parseArgs(String args){
        if (args == null) {
            return new Arguments(new HashMap<>());
        }
        args = args.trim();
        if (args.isEmpty()) {
            return new Arguments(new HashMap<>());
        }
        Map<String, List<String>> map = new HashMap<>();
        /**
         * 将参数字符进行分割
         */
        for (String argPair : args.split(",")) {
            String[] strs = argPair.split("=");
            if (strs.length != 2) {
                throw new IllegalArgumentException("Arguments for the agent should be like: key1=value1,key2=value2");
            }

            String key = strs[0].trim();
            if (key.isEmpty()) {
                throw new IllegalArgumentException("Argument key should not be empty");
            }

            List<String> list = map.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(strs[1].trim());
        }
        return new Arguments(map);
    }
    public void updateArguments(Map<String, List<String>> parsedArgs) {
        rawArgValues.putAll(parsedArgs);

        String argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_NOOP);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            noop = Boolean.parseBoolean(argValue);
            System.out.println("Got argument value for noop: " + noop);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_REPORTER);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            reporterConstructor = ReflectionUtils.getConstructor(argValue, Reporter.class);
            System.out.println("Got argument value for reporter: " + argValue);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_CONFIG_PROVIDER);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            configProvierConstructor = ReflectionUtils.getConstructor(argValue, ConfigProvider.class);
            System.out.println("Got argument value for configProvider: " + argValue);
        }
        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_CONFIG_FILE);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            configFile = argValue;
            System.out.println("Got argument value for configFile: " + configFile);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_METRIC_INTERVAL);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            metricInteval = Long.parseLong(argValue);
            System.out.println("Got argument value for metricInterval: " + metricInteval);
        }

        if (metricInteval < MIN_INTERVAL_MILLIS) {
            throw new RuntimeException("Metric interval too short, must be at least " + MIN_INTERVAL_MILLIS);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_SAMPLE_INTERVAL);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            sampleInterval = Long.parseLong(argValue);
            System.out.println("Got argument value for sampleInterval: " + sampleInterval);
        }

        if (sampleInterval != 0 && sampleInterval < MIN_INTERVAL_MILLIS) {
            throw new RuntimeException("Sample interval too short, must be 0 (disable sampling) or at least " + MIN_INTERVAL_MILLIS);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_TAG);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            tag = argValue;
            System.out.println("Got argument value for tag: " + tag);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_CLUSTER);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            cluster = argValue;
            System.out.println("Got argument value for cluster: " + cluster);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_APP_ID_VARIABLE);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            appid = argValue;
            System.out.println("Got argument value for appIdVariable: " + appid);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_APP_ID_REGEX);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            appIdReges = argValue;
            System.out.println("Got argument value for appIdRegex: " + appIdReges);
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_THREAD_PROFILING);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            threadProfiler = Boolean.parseBoolean(argValue);
            System.out.println("Got argument value for threadProfiling: " + threadProfiler);
        }

        List<String> argValues = ArgumentUtils.getArgumentMultiValues(parsedArgs, ARG_DURATION_PROFILING);
        if (!argValues.isEmpty()) {
            durationProfiler.clear();
            for (String str : argValues) {
                int index = str.lastIndexOf(".");
                if (index <= 0 || index + 1 >= str.length()) {
                    throw new IllegalArgumentException("Invalid argument value: " + str);
                }
                String className = str.substring(0, index);
                String methodName = str.substring(index + 1);
                ClassAndMethod classAndMethod = new ClassAndMethod(className, methodName);
                durationProfiler.add(classAndMethod);
                System.out.println("Got argument value for durationProfiling: " + classAndMethod);
            }
        }

        List<String> argsValues = ArgumentUtils.getArgumentMultiValues(parsedArgs, ARG_ARGUMENT_PROFILING);
        if(!argsValues.isEmpty()){
            argumentProfiling.clear();
            for(String str : argsValues){
                int index = str.lastIndexOf(".");
                if(index<=0 || index + 1 >= str.length()){
                    throw new JVMException(ErrorCode.SYSTEM_ERROR.getCode(), "Invalid argument value: " + str);
                }
                String classMethod = str.substring(0,index);
                //System.out.println("============= classMethodName " + classMethod+ " index + 1" + index);
                int argumentIndex = Integer.parseInt(str.substring(index + 1, str.length()));

                index = classMethod.lastIndexOf(".");
                if (index <= 0 || index + 1 >= classMethod.length()) {
                    throw new JVMException(ErrorCode.SYSTEM_ERROR.getCode(), "Invalid argument value: " + str);
                }

                String className = classMethod.substring(0, index);
                String methodName = str.substring(index + 1, classMethod.length());

                ClassMethodArgument classMethodArgument = new ClassMethodArgument(className, methodName, argumentIndex);
                argumentProfiling.add(classMethodArgument);
                System.out.println("Got argument value for argumentProfiling: " + classMethodArgument);
            }
        }

        argValue = ArgumentUtils.getArgumentSingleValue(parsedArgs, ARG_IO_PROFILING);
        if (ArgumentUtils.needToUpdateArg(argValue)) {
            ioProfiler = Boolean.parseBoolean(argValue);
            System.out.println("Got argument value for ioProfiling: " + ioProfiler);
        }

    }

    public void runConfigProvider() {
        try {
            ConfigProvider configProvider;
            if(configProvierConstructor ==null){
                configProvider = new EmptyConfigProvider();
            }
            else{
                configProvider = configProvierConstructor.newInstance();
            }
            if (configProvider != null) {
                Map<String, Map<String, List<String>>> extraConfig = configProvider.getConfig();

                // Get root level config (use empty string as key in the config map)
                Map<String, List<String>> rootConfig = extraConfig.get("");
                if (rootConfig != null) {
                    updateArguments(rootConfig);
                    System.out.println("Updated arguments based on config: " + JSONUtil.toJsonStr(rootConfig));
                }

                // Get tag level config (use tag value to find config values in the config map)
                if (getTag() != null && !getTag().isEmpty()) {
                    Map<String, List<String>> overrideConfig = extraConfig.get(getTag());
                    if (overrideConfig != null) {
                        updateArguments(overrideConfig);
                        System.out.println("Updated arguments based on config override: " + JSONUtil.toJsonStr(overrideConfig));
                    }
                }
            }
        } catch (Throwable ex) {
            System.out.println("Failed to update arguments with config provider" + ex.toString());
        }
    }

    public Reporter getReporter(){
        if(reporterConstructor == null){
            return new ConsoleOutputReporter();
        }else{
            try{
                Reporter reporter = reporterConstructor.newInstance();
                reporter.updateArguments(getRawArgValues());
                return reporter;
            }catch (Exception e){
                throw new JVMException(ErrorCode.OPERATION_ERROR.getCode(), String.format("Failed to create reporter instance %s", reporterConstructor.getDeclaringClass()) + e.toString());
            }
        }
    }
}
