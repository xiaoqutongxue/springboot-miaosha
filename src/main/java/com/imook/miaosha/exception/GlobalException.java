package com.imook.miaosha.exception;

import com.imook.miaosha.result.CodeMsg;

/**
 * 通用异常
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionULD = 1L;

    private CodeMsg codeMsg;

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }

    public GlobalException(CodeMsg codeMsg){

        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }
}
