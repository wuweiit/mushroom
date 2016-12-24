package org.marker.app.common;

/**
 *
 *
 * Created by ROOT on 2016/12/7.
 */
public enum  ErrorCode {


    USER_NOT_EXISTS(1001, "用户不存在！"),
    USER_IS_EXISTS(1002, "用户已经注册！"),

    USER_PASSWORD_ERROR(1003, "账号或者密码错误！"),
    EMAIL_IS_ERROR(1004, "邮箱格式错误！"),






    ;


    /** 状态码 */
    private Integer code;
    /** 说明内容 */
    private String msg;



    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
