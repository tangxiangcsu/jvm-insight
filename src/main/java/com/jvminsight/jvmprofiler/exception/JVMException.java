package com.jvminsight.jvmprofiler.exception;

import com.jvminsight.jvmprofiler.common.ErrorCode;

/**
 * @PACKAGE_NAME: com.jvm-insight.jvmprofiler.exception
 * @NAME: JVMException
 * @USER: tangxiang
 * @DATE: 2024/7/29
 * @PROJECT_NAME: jvm-insight
 * @DESCRIPTION:
 **/
public class JVMException extends RuntimeException{

    private final int code;

    public JVMException(int code, String message){
        super(message);
        this.code = code;
    }

    public JVMException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public JVMException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
