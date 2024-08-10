package com.jvminsight.jvmprofiler.utils;

import com.jvminsight.jvmprofiler.dto.ClassAndMethod;

import java.util.List;

/**
 * @PACKAGE_NAME: com.jvminsight.jvmprofiler.utils
 * @NAME: ClassAndMethodFilter
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public class ClassAndMethodFilter {
    private static final String METHOD_NAME_WILDCARD = "*";

    private ClassAndMethod[] classAndMethods = new ClassAndMethod[0];

    public ClassAndMethodFilter(List<ClassAndMethod> classMethodNames) {
        if (classMethodNames != null) {
            this.classAndMethods = new ClassAndMethod[classMethodNames.size()];
            for (int i = 0; i < classMethodNames.size(); i++) {
                this.classAndMethods[i] = classMethodNames.get(i);
            }
        }
    }

    public boolean isEmpty() {
        return classAndMethods.length == 0;
    }

    public boolean matchClass(String className) {
        for (ClassAndMethod classAndMethod : classAndMethods) {
            if (className.startsWith(classAndMethod.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean matchMethod(String className, String methodName) {
        for (ClassAndMethod classAndMethod : classAndMethods) {
            if (className.startsWith(classAndMethod.getClassName())) {
                if (METHOD_NAME_WILDCARD.equals(classAndMethod.getMethodName())
                        || methodName.equals(classAndMethod.getMethodName())) {
                    return true;
                }
            }
        }
        return false;
    }
}