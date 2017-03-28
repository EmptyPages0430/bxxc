package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2017/1/21.
 *
 * 广告页
 *
 */

public class Advertising {
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
        /**
         * 广告图片url
         */
        private String content;
        /**
         * 广告图片大小
         */
        private String contentSize;
        /**
         * 广告图片存在时间
         */
        private String duration;
        /**
         * 点击广告图片跳转的地址
         */
        private String openUrl;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentSize() {
            return contentSize;
        }

        public void setContentSize(String contentSize) {
            this.contentSize = contentSize;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getOpenUrl() {
            return openUrl;
        }

        public void setOpenUrl(String openUrl) {
            this.openUrl = openUrl;
        }
    }

}
