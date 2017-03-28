package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/26.
 * 我的教练,预约
 */

public class MyCoachAction {
    private int code;
    private String reason;
    private Result result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public class Result{
        private int code;
        private String reason;
        private List<Res2> result;
        //教练id
        private String cid;
        /**
         * 状态判断是否可以改教练
         */
        private int zhuangtai;
        /**所带学员
         */
        private int nowstudent;
        /**
         * 场地
         */
        private String faddress;
        /**
         * 教练名称
         */
        private String coachname;
        /**
         * 教练头像地址
         */
        private String file;
        /**
         * 班型
         */
        private String class_type;
        /**
         * 车牌号
         */
        private String number_plates;
        /**
         * 车型
         */
        private String chexing;
        /**
         * 最大限定人数
         */
        private String maxnum;
        /**
         * 简介
         */
        private String Prompt;
        //教练排课的课时
        private List<Res1> subject;
        //我预约过的额课时
        private List<Res2> stusubject;

        public int getZhuangtai() {
            return zhuangtai;
        }

        public void setZhuangtai(int zhuangtai) {
            this.zhuangtai = zhuangtai;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<Res2> getResult() {
            return result;
        }

        public void setResult(List<Res2> result) {
            this.result = result;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getPrompt() {
            return Prompt;
        }

        public void setPrompt(String prompt) {
            Prompt = prompt;
        }

        public int getNowstudent() {
            return nowstudent;
        }

        public void setNowstudent(int nowstudent) {
            this.nowstudent = nowstudent;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getFaddress() {
            return faddress;
        }

        public void setFaddress(String faddress) {
            this.faddress = faddress;
        }

        public String getCoachname() {
            return coachname;
        }

        public void setCoachname(String coachname) {
            this.coachname = coachname;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getClass_type() {
            return class_type;
        }

        public void setClass_type(String class_type) {
            this.class_type = class_type;
        }

        public String getChexing() {
            return chexing;
        }

        public void setChexing(String chexing) {
            this.chexing = chexing;
        }

        public String getNumber_plates() {
            return number_plates;
        }

        public void setNumber_plates(String number_plates) {
            this.number_plates = number_plates;
        }

        public String getMaxnum() {
            return maxnum;
        }

        public void setMaxnum(String maxnum) {
            this.maxnum = maxnum;
        }

        public List<Res1> getSubject() {
            return subject;
        }

        public void setSubject(List<Res1> subject) {
            this.subject = subject;
        }

        public List<Res2> getStusubject() {
            return stusubject;
        }

        public void setStusubject(List<Res2> stusubject) {
            this.stusubject = stusubject;
        }

        public class  Res1{
            /**
             * 天数
             */
            private String timeone;
            /**
             * 时间段
             */
            private String time_slot;
            /**
             * 当前时间段所预约的人数
             */
            private int count;

            public String getTimeone() {
                return timeone;
            }

            public void setTimeone(String timeone) {
                this.timeone = timeone;
            }

            public String getTime_slot() {
                return time_slot;
            }

            public void setTime_slot(String time_slot) {
                this.time_slot = time_slot;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
        public class  Res2{
            /**
             * 天数
             */
            private String day;
            /**
             * 时间段
             */
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

}
