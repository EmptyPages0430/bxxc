package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2016/11/21.
 * 学车进程
 */
public class LearnProAction  {
    //进度内容介绍
    private String context;
    //进度名称
    private String proName;
    //进度状态是否完成
    private String proisComplete;

    public LearnProAction(String proName, String context, String proisComplete) {
        this.proName = proName;
        this.context = context;
        this.proisComplete = proisComplete;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getProisComplete() {
        return proisComplete;
    }

    public void setProisComplete(String proisComplete) {
        this.proisComplete = proisComplete;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }
}
