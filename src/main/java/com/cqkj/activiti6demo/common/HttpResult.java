package com.cqkj.activiti6demo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一结果类
 * @param <T>
 */
@Data
public class HttpResult<T> implements Serializable {
    /**
     * 是否响应成功
     */
    private Boolean success;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应信息
     */
    private String message;

    /*** 构造器开始，私有，不可外部创建 ***/
    /**
     * 无参构造器
     */
    private HttpResult(){
        this.code = 200;
        this.success = true;
    }

    /**
     * 有参构造器
     * @param obj
     */
    private HttpResult(T obj){
        this.code = 200;
        this.success = true;
        this.data = obj;
    }

    /**
     * 有参构造器
     * @param resultCode
     */
    private HttpResult(ResultCodeEnum resultCode){
        this.success = false;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /*** 构造器结束 ***/

    /**
     * 通用返回成功(无结果)
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> success(){
        return new HttpResult();
    }

    /**
     * 返回成功(有结果)
     * @param data
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> success(T data){
        return new HttpResult<T>(data);
    }

    /**
     * 通用返回失败
     * @param resultCode
     * @param <T>
     * @return
     */
    public static<T> HttpResult<T> failure(ResultCodeEnum resultCode){
        return new HttpResult<T>(resultCode);
    }
}
