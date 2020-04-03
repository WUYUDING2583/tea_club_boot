package com.yuyi.tea.exception;

import com.yuyi.tea.common.CodeMsg;


/**
 * 统一异常
 */
public class GlobalException extends RuntimeException {

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }
    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
