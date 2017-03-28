package com.jgkj.bxxc.bean;


import java.util.List;

/**
 * Created by fangzhou on 2016/12/7.
 * 学成进程
 */

public class LearnProBean {
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

    public class Result{
        private int id;
        private String imagePath;
        private String State;
        private String learnIntroduce;
        private String myState;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getMyState() {
            return myState;
        }

        public void setMyState(String myState) {
            this.myState = myState;
        }

        public String getLearnIntroduce() {
            return learnIntroduce;
        }

        public void setLearnIntroduce(String learnIntroduce) {
            this.learnIntroduce = learnIntroduce;
        }
    }
}
