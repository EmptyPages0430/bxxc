package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/23.
 */

public class ErrorMsg {
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
    /**
     * 内部类
     */
    public class Result{
        private String subCount;

        public String getSubCount() {
            return subCount;
        }
        public void setSubCount(String subCount) {
            this.subCount = subCount;
        }
    }
}
