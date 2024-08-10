package com.jvminsight.jvmprofiler.utils;

import com.jvminsight.jvmprofiler.common.ErrorCode;
import com.jvminsight.jvmprofiler.exception.JVMException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.utils
 * @NAME: ReflectionUtils
 * @USER: tangxiang
 * @DATE: 2024/8/1
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION: 反射工具
 **/
@Slf4j
public class ReflectionUtils {

    public static <T> Constructor<T> getConstructor(String implementClass, Class<T> interfaceClass){
        Class<?> clazz = null;

        try{
            clazz = Class.forName(implementClass);
        } catch (Exception e) {
            throw new JVMException(ErrorCode.SYSTEM_ERROR.getCode(), String.format("Failed to get class for %s", implementClass)+ e.toString());
        }

        if(!interfaceClass.isAssignableFrom(clazz)){
            throw new JVMException(ErrorCode.PARAMS_ERROR.getCode(), String.format("Invalid class %s, please make sure it is an implementation of %s", clazz, interfaceClass.getName()));
        }

        try{
            Class<T> concretelass = (Class<T>) clazz;
            Constructor<T> constructor = concretelass.getConstructor();
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new JVMException(ErrorCode.SYSTEM_ERROR.getCode(), String.format("Failed to get constructor for %s", clazz.getName()));
        }

    }
}
