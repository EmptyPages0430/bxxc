package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2017/3/15.
 * 视屏展示页面
 */

public class VideoMsg {
    private int code;
    private String reason;
    private List<Result> result;

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result{
        /**
         * 视屏id
         */
        private String videoid;
        /**
         *视频展示图片
         */
        private String videopic;
        /**
         *视屏标题
         */
        private String title;
        /**
         *视频时长
         */
        private String times;

        public String getVideoid() {
            return videoid;
        }

        public void setVideoid(String videoid) {
            this.videoid = videoid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideopic() {
            return videopic;
        }

        public void setVideopic(String videopic) {
            this.videopic = videopic;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }
    }


}
