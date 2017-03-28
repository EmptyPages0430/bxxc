package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/28.
 */

public class AppResultAction  {
    private int code;
    private String reason;
    private List<Result> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class  Result{
        private String day;
        private String time_slot;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getTime_slot() {
            return time_slot;
        }

        public void setTime_slot(String time_slot) {
            this.time_slot = time_slot;
        }
    }

}
