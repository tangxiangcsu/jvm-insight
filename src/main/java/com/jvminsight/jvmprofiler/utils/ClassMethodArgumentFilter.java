package com.jvminsight.jvmprofiler.utils;

import com.jvminsight.jvmprofiler.dto.ClassMethodArgument;

import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.utils
 * @NAME: ClassMethodArgumentFilter
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION:
 **/
public class ClassMethodArgumentFilter {
    private static final String METHOD_NAME_WILDCARD = "*";

    private ClassMethodArgument[] classMethodArguments = new ClassMethodArgument[0];

    public ClassMethodArgumentFilter(List<ClassMethodArgument> classMethodArgumentToFilter) {
        if (classMethodArgumentToFilter != null) {
            this.classMethodArguments = new ClassMethodArgument[classMethodArgumentToFilter.size()];
            for (int i = 0; i < classMethodArgumentToFilter.size(); i++) {
                this.classMethodArguments[i] = classMethodArgumentToFilter.get(i);
            }
        }
    }

    public boolean isEmpty() {
        return classMethodArguments.length == 0;
    }

    public boolean matchClass(String className) {
        for (ClassMethodArgument classAndMethod : classMethodArguments) {
            if (className.startsWith(classAndMethod.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> matchMethod(String className, String methodName) {
        List<Integer> result = new ArrayList<>();

        for (ClassMethodArgument classMethodArgument : classMethodArguments) {
            if (className.startsWith(classMethodArgument.getClassName())) {
                if (METHOD_NAME_WILDCARD.equals(classMethodArgument.getMethodName())
                        || methodName.equals(classMethodArgument.getMethodName())) {
                    result.add(classMethodArgument.getArgumentIndex());
                }
            }
        }

        return result;
    }
}

