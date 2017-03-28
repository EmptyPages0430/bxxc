package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MyOrder {
    private int code;
    private String reason;
    private int nocode;
    private String noreason;
    private List<Result> result;
    private List<Result> noresult;

    public int getCode() {
        return code;
    }


    public List<Result> getNoresult() {
        return noresult;
    }


    public List<Result> getResult() {
        return result;
    }


    public String getNoreason() {
        return noreason;
    }

    public int getNocode() {
        return nocode;
    }


    public String getReason() {
        return reason;
    }


    public class Result{
        private String out_trade_no;
        private int trade_status;
        private double total_fee;
        private int cid;
        private String faddress;
        private String coachname;
        private String file;
        private String class_type;
        private String number_plates;
        private int stunum;
        private String chexing;
        private String maxnum;

        public int getTrade_status() {
            return trade_status;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public String getFaddress() {
            return faddress;
        }

        public int getCid() {
            return cid;
        }

        public double getTotal_fee() {
            return total_fee;
        }

        public String getFile() {
            return file;
        }

        public String getCoachname() {
            return coachname;
        }

        public String getClass_type() {
            return class_type;
        }

        public String getNumber_plates() {
            return number_plates;
        }

        public int getStunum() {
            return stunum;
        }

        public String getChexing() {
            return chexing;
        }

        public String getMaxnum() {
            return maxnum;
        }
    }

}
