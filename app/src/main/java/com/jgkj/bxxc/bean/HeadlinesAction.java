package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2017/3/9.
 * 首页展示百信头条的轮播文字
 */

public class HeadlinesAction {
    /**
     * 返回码
     */
    private int code;
    /**
     * 请求结果的原因解释
     */
    private String reason;
    /**
     *请求结果集
     */
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
        /**
         * 头条id
         */
        private String newsid;
        /**
         * 头条标题
         */
        private String title;
        /**
         *头条副标题
         */
        private String subtitle;
        /**
         *头条图片
         */
        private String picture;
        /**
         *头条时间
         */
        private String time;
        /**
         *点击头条跳转的网页地址
         */
        private String url;

        public String getNewsid() {
            return newsid;
        }

        public void setNewsid(String newsid) {
            this.newsid = newsid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
