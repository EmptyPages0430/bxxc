package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2016/12/5.
 * 获取验证码，进行本地验证
 */

public class Code {
    private int code;
    private String reason;
    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
