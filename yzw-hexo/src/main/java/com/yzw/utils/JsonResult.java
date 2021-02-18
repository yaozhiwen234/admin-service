package com.yzw.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: liwei
 * @create: 2020-04-20
 * @description: 用于后台统一给APP提供格式化数据包装实体
 **/
@Setter
@Getter
@ToString
public class JsonResult<T> implements Serializable {

    /* 成功 */
    public static final int SUCCESS = 200;

    /* 失败 */
    public static final int FAIL = 400;

    /* 告警 */
    public static final int WARN = 301;

    /* 错误 */
    public static final int ERROR = 500;

    /* 未登录 */
    public static final int NOLOGIN = 401;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回内容
     */
    private String msg;

    /**
     * 数据对象
     */
    private T data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 初始化一个新的AjaxResult对象，表示一个空消息
     */
    private JsonResult() {

    }

    /**
     * 初始化一个新的AjaxResult对象，拥有消息码与消息描述
     *
     * @param code
     * @param msg
     */
    private JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 初始化一个新的AjaxResult对象，拥有消息码与消息描述
     *
     * @param code
     * @param msg
     */
    private JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static <T> JsonResult<T> success() {
        return JsonResult.success(null);
    }


    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> JsonResult<T> success(String msg, T data) {
        return new <T>JsonResult<T>(SUCCESS, msg, data);
    }

    /**
     * 返回成功消息
     *
     * @param data 数据对象
     * @return 成功消息
     */
    public static <T> JsonResult<T> success(T data) {
        return new <T>JsonResult<T>(SUCCESS, "操作成功", data);
    }

    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> JsonResult<T> fail() {
        return JsonResult.fail("操作失败");
    }

    /**
     * 返回失败消息
     *
     * @param msg 返回内容
     * @return 失败消息
     */
    public static <T> JsonResult<T> fail(String msg) {
        return JsonResult.fail(msg, null);
    }

    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> JsonResult<T> notVerify() {
        return JsonResult.error("没有权限", null);
    }

    /**
     * 返回失败消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 失败消息
     */
    public static <T> JsonResult<T> fail(String msg, T data) {
        return new <T>JsonResult<T>(FAIL, msg, data);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> JsonResult<T> warn(String msg) {
        return JsonResult.warn(msg, null);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> JsonResult<T> warn(String msg, T data) {
        return new <T>JsonResult<T>(WARN, msg, data);
    }

    /**
     * 返回错误消息
     */
    public static <T> JsonResult<T> error() {
        return JsonResult.error("操作失败");
    }


    /**
     * 返回未登录消息
     */
    public static <T> JsonResult<T> notLogin() {
        return JsonResult.noLogin("请先登录");
    }

    /**
     * 返回未登录消息
     *
     * @param msg 返回内容
     * @return 未登录消息
     */
    public static <T> JsonResult<T> noLogin(String msg) {
        return JsonResult.noLogin(msg, null);
    }

    /**
     * 返回未登录消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 未登录消息
     */
    public static <T> JsonResult<T> noLogin(String msg, T data) {
        return new <T>JsonResult<T>(NOLOGIN, msg, data);
    }

    /**
     * 返回错误消息
     */
    public static <T> JsonResult<T> parmError() {
        return JsonResult.error("参数错误");
    }

    /**
     * 返回错误消息
     */
    public static <T> JsonResult<T> sqlError() {
        return JsonResult.error("操作失败", null);
    }

    /**
     * 返回错误消息
     */
    public static <T> JsonResult<T> sqlError(String errorMessage) {
        return JsonResult.error(errorMessage);
    }

    /**
     * 返回错误消息
     */
    public static <T> JsonResult<T> normalError() {
        return JsonResult.error("操作失败", null);
    }


    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> JsonResult<T> error(String msg) {
        return JsonResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @return 警告消息
     */
    public static <T> JsonResult<T> noData() {
        return JsonResult.error("无数据", null);
    }


    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> JsonResult<T> error(String msg, T data) {
        return new <T>JsonResult<T>(ERROR, msg, data);
    }

    /**
     * 返回失败消息
     *
     * @return 失败消息
     */
    public static <T> JsonResult<T> authfail() {
        return JsonResult.fail("身份认证失败！");
    }

    /**
     * 返回失败消息
     *
     * @param msg 返回内容
     * @return 失败消息
     */
    public static <T> JsonResult<T> authfail(String msg) {
        return JsonResult.fail(msg, null);
    }

    /**
     * 返回失败消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 失败消息
     */
    public static <T> JsonResult<T> authfail(String msg, T data) {
        return new <T>JsonResult<T>(FAIL, msg, data);
    }


}
