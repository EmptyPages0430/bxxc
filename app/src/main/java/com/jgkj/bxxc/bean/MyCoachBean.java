package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class MyCoachBean {
    private int code;
    private String reason;
    private List<Result> result;

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

    public class  Result{
        private int cid;
        private String faddress;
        private String coachname;
        private String file;
        private String class_type;
        private String number_plates;
        private int credit;
        private int haopin;
        private int tguo;
        private int zonghe;
        private int stunum;
        private String chexing;
        private int maxnum;
        private int subject;
        private String Prompt;

        public int getCid() {
            return cid;
        }


        public String getFaddress() {
            return faddress;
        }


        public String getCoachname() {
            return coachname;
        }


        public String getFile() {
            return file;
        }


        public String getNumber_plates() {
            return number_plates;
        }


        public String getClass_type() {
            return class_type;
        }


        public int getCredit() {
            return credit;
        }


        public int getTguo() {
            return tguo;
        }


        public int getHaopin() {
            return haopin;
        }


        public int getZonghe() {
            return zonghe;
        }


        public String getChexing() {
            return chexing;
        }


        public int getStunum() {
            return stunum;
        }


        public int getSubject() {
            return subject;
        }


        public int getMaxnum() {
            return maxnum;
        }


        public String getPrompt() {
            return Prompt;
        }

    }
}
