package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2017/1/6.
 * 补考订单
 */

public class BuKaoOrder {
    private int code;
    private String reason;
    private int nocode;
    private String noreason;
    private List<Result> result;
    private List<Result> noresult;

    public int getNocode() {
        return nocode;
    }

    public void setNocode(int nocode) {
        this.nocode = nocode;
    }

    public String getNoreason() {
        return noreason;
    }

    public void setNoreason(String noreason) {
        this.noreason = noreason;
    }

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Result> getNoresult() {
        return noresult;
    }

    public void setNoresult(List<Result> noresult) {
        this.noresult = noresult;
    }

    public class Result{
        /**
         * 进程状态
         */
        private String baixin_state;
        /**
         * 订单号
         */
        private String out_trade_no;
        private String refee;
        /**
         * 订单状态
         */
        private String trade_status;
        private String uid;
        private String file;
        private String name;
        private String class_class;
        private String car;

        public String getBaixin_state() {
            return baixin_state;
        }

        public void setBaixin_state(String baixin_state) {
            this.baixin_state = baixin_state;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTrade_status() {
            return trade_status;
        }

        public void setTrade_status(String trade_status) {
            this.trade_status = trade_status;
        }

        public String getRefee() {
            return refee;
        }

        public void setRefee(String refee) {
            this.refee = refee;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
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

        public String getClass_class() {
            return class_class;
        }

        public void setClass_class(String class_class) {
            this.class_class = class_class;
        }
    }
}
