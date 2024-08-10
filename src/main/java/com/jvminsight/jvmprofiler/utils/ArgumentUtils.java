package com.jvminsight.jvmprofiler.utils;

/**
 * @PACKAGE_NAME: com.jvminsight.jvmprofiler.utils
 * @NAME: ArgumentUtils
 * @USER: tangxiang
 * @DATE: 2024/7/30
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgumentUtils {

    public static boolean needToUpdateArg(String argValue) {
        return argValue != null && !argValue.isEmpty();
    }

    public static boolean needToUpdateRollingArg(String enableRolling) {
        return enableRolling != null && !enableRolling.isEmpty() && Boolean.parseBoolean(enableRolling);
    }

    public static String getArgumentSingleValue(Map<String, List<String>> parsedArgs, String argName) {
        List<String> list = parsedArgs.get(argName);
        if (list == null) {
            return null;
        }

        if (list.isEmpty()) {
            return "";
        }

        return list.get(list.size() - 1);
    }

    public static List<String> getArgumentMultiValues(Map<String, List<String>> parsedArgs, String argName) {
        List<String> list = parsedArgs.get(argName);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }
}
