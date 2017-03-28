package com.jgkj.bxxc.bean;

/**
 * Created by Administrator on 2016/12/3.
 */

public class StuEvaluation {
    //学员评价
    private String date;
    //评价内容
    private String comment;
    //学员头像
    private String default_file;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefault_file() {
        return default_file;
    }

    public void setDefault_file(String default_file) {
        this.default_file = default_file;
    }

}
