package com.gkzy.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),
    USERNAME_EXISTS(1001, "用户名已存在"),
    PHONE_EXISTS(1002, "手机号已被注册"),
    LOGIN_FAILED(1003, "用户名或密码错误"),
    SCORE_REQUIRED(2001, "请先填写高考分数和位次"),
    PLAN_FULL(3001, "志愿已满，最多8个");
    private final int code;
    private final String message;
}
