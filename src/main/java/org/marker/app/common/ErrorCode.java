package org.marker.app.common;

/**
 *
 *
 * Created by ROOT on 2016/12/7.
 */
public enum  ErrorCode {


    USER_NOT_EXISTS(1000, "user not exists"),

    USER_PASSWORD_ERROR(1001, "user password error"),




    

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
