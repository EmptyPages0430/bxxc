package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2016/10/29.
 */
public class ItemEntity {
    //id
    private int id;
    //考试问题
    private String question;
    //考试答案
    private int answer;
    //选项A
    private String item1;
    //选项B
    private String item2;
    //选项C
    private String item3;
    //选项D
    private String item4;
    //答案的解释
    private String explains;
    //题目图片地址
    private String url;

//    public ItemEntity(int id,String question,int answer,String item1, String item2, String item3,
//                       String item4,String explains,String uri){
//        this.id = id;
//        this.answer = answer;
//        this.item1 = item1;
//        this.item2 = item2;
//        this.item3 = item3;
//        this.item4 = item4;
//        this.explains = explains;
//        this.uri = uri;
//        this.question = question;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
