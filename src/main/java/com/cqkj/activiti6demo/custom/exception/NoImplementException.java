package com.cqkj.activiti6demo.custom.exception;

import com.cqkj.activiti6demo.common.HttpResult;
import com.cqkj.activiti6demo.common.ResultCodeEnum;

/**
 * 自定义的未实现activiti重写的错误
 */
public class NoImplementException extends RuntimeException {
    /**
     * 错误码
     */
    protected Integer code;
    protected String msg;


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NoImplementException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }
}
