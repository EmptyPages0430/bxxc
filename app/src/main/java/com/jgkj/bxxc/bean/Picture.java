package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/5.
 * 获取轮播图
 */

public class Picture {
    private int code;
    private String reason;
    private List<String> result;

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

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
