package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2017/1/5.
 */

public class ShowRePay {
    private int code;
    private String reason;
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{
        private int baixin_state;
        private double refee;
        private int uid;
        private String file;
        private String name;
        private String class_class;
        private String car;
        private String phone;

        public int getBaixin_state() {
            return baixin_state;
        }

        public void setBaixin_state(int baixin_state) {
            this.baixin_state = baixin_state;
        }

        public double getRefee() {
            return refee;
        }

        public void setRefee(double refee) {
            this.refee = refee;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getClass_class() {
            return class_class;
        }

        public void setClass_class(String class_class) {
            this.class_class = class_class;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
