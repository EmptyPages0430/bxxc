package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2017/1/14.
 */

public class NoResultAction {
    private int code;
    private String reason;
    public Result result;

    public Result getResult() {
        return result;
    }

    public class Result{
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

}
