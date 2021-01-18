package com.cqkj.activiti6demo.common;

import com.google.common.base.Verify;

/**
 * 结果枚举类
 */
public enum ResultCodeEnum {
    /*** 通用部分 100 - 599 ***/
    // 成功请求
    SUCCESS(200, "successful"),
    // 重定向
    REDIRECT(301, "redirect"),
    // 资源未找到
    NOT_FOUND(404, "not found"),
    // 服务器错误
    SERVER_ERROR(500, "server error"),
    /********************************************
     * 这里可以根据不同模块用不同的区级分开错误码，例如: *
     * 1000～1999 区间表示用户模块错误              *
     * 2000～2999 区间表示订单模块错误              *
     * 3000～3999 区间表示商品模块错误              *
     ********************************************/
    // 用戶名或密碼错误
    VERIFY_ERROR(1100, "verify error"),
    // 部署流程资源文件错误
    DEPLOY_ERROR(4001, "deploy error"),
    // 部署流程资源文件错误
    IMPLEMENT_ERROR(5001, "implement error");
    /**
     * 响应状态码
     */
    private Integer code;
    private String message;
    ResultCodeEnum(Integer code, String msg){
        this.code = code;
        this.message = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

