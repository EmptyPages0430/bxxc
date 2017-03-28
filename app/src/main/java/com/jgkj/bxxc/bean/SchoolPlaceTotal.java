package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/19.
 * 所有校区地址详细信息
 */

public class SchoolPlaceTotal {
    /**
     * 返回状态码
     */
    private int code;
    /**
     * 返回信息
     */
    private String reason;
    /**
     * 返回结果
     */
    private List<Result> result;

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public List<Result> getResult() {
        return result;
    }

    public class Result{
        /**
         * 城市区名称
         */
        private String school_aera;
        /**
         * 城市区id
         */
        private String sid;
        /**
         * 返回结果
         */
        private List<Res> res;

        public String getSid() {
            return sid;
        }

        public List<Res> getResult() {
            return res;
        }

        public String getSchool_aera() {
            return school_aera;
        }

        public class Res{
            /**
             * 返回场地id
             */
            private int id;
            /**
             * 区域id
             */
            private int sid;
            /**
             * 场地名称
             */
            private String sname;
            /**
             * 场地地址
             */
            private String faddress;
            /**
             * 场地经度
             */
            private String longitude;
            /**
             * 场地纬度
             */
            private String latitude;
            /**
             * 场地图片
             */
            private String sfile;

            public int getSid() {
                return sid;
            }

            public int getId() {
                return id;
            }

            public String getSname() {
                return sname;
            }

            public String getLongitude() {
                return longitude;
            }

            public String getFaddress() {
                return faddress;
            }

            public String getLatitude() {
                return latitude;
            }

            public String getSfile() {
                return sfile;
            }

        }

    }
}
