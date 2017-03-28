package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/29.
 * 学车记录
 */

public class LearnHisAction {
    /**
     * 返回码
     */
    private int code;
    /**
     * 返回信息
     */
    private String reason;
    /**
     * 是否预约过考试
     */
    private int testState;
    /**
     * 返回结果
     */
    private List<Result> result;

    public int getTestState() {
        return testState;
    }

    public void setTestState(int testState) {
        this.testState = testState;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public List<Result> getResult() {
        return result;
    }

    public  class Result{
        /**
         * 天数
         */
        private String day;
        /**
         *时间段
         */
        private String time_slot;
        /**
         *
         */
        private String timeid;
        /**
         * 教练id
         */
        private String cid;
        /**
         * 学车进程
         */
        private int state;

        public String getTimeid() {
            return timeid;
        }


        public String getTime_slot() {
            return time_slot;
        }


        public String getDay() {
            return day;
        }


        public int getState() {
            return state;
        }


        public String getCid() {
            return cid;
        }

    }
}
