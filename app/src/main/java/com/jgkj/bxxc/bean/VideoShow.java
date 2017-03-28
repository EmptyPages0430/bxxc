package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2017/3/15.
 * 视屏展示页面
 */

public class VideoShow {
    private int code;
    private String reason;
    private Result result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{
        /**
         * 视屏地址
         */
        private String video;
        /**
         * 视频介绍
         */
        private String introduce;

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }


}
