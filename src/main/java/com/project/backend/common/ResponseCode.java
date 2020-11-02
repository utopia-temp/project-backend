package com.project.backend.common;

/**
 * @author Utopia
 * <p>
 * 响应状态枚举类
 */

public enum ResponseCode {

    //成功
    SUCCESS(0, "SUCCESS"),
    //失败
    ERROR(1, "ERROR"),
    //参数错误
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),
    //需要进行登录
    NEED_LOGIN(10, "NEED_LOGIN");

    //具体值
    private final int code;
    //描述
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
