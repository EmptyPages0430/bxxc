package com.jgkj.bxxc.bean;

import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class HistoryView {
    //存储
    private List<View> list;
    //页面号码
    private int page;
    //正确答案
    private int right_Answer;
    //用户选择的答案
    private int user_Answer;
    //选项A
    private String answerA;
    //选项B
    private String answerB;
    //选项C
    private String answerC;
    //选项D
    private String answerD;
    //答案解释
    private String explain;
    //问题
    private String question;
    //图片
    private String url;

    public HistoryView(List<View> list, int page, int right_Answer, int user_Answer, String answerA, String answerB, String answerC, String answerD, String explain, String question, String url) {
        this.list = list;
        this.page = page;
        this.right_Answer = right_Answer;
        this.user_Answer = user_Answer;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.explain = explain;
        this.question = question;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getRight_Answer() {
        return right_Answer;
    }

    public void setRight_Answer(int right_Answer) {
        this.right_Answer = right_Answer;
    }

    public int getUser_Answer() {
        return user_Answer;
    }

    public void setUser_Answer(int user_Answer) {
        this.user_Answer = user_Answer;
    }

    public HistoryView(List<View> list, int page) {
        this.list = list;
        this.page = page;
    }

    public List<View> getList() {
        return list;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
