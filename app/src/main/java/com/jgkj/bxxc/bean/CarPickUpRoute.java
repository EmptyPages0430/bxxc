package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2017/3/14.
 * 车接车送接送路线
 */

public class CarPickUpRoute {
    /**
     * 返回码
     */
    private int code;
    /**
     *返回理由
     */
    private String reason;
    /**
     *返回结果
     */
    private List<Result> result;

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public class Result{
        /**
         *路线地图地址
         */
        private String route;
        /**
         *路线信息
         */
        private String info;

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
