package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/3.
 * 场地信息
 */

public class Faddress {
    private String reason;
    private int code;
    private List<Results> results;

    public String getReason() {
        return reason;
    }

    public int getCode() {
        return code;
    }

    public List<Results> getResults() {
        return results;
    }

    public class Results{
        private String school;
        private List<Results1> results;

        public String getSchool() {
            return school;
        }


        public List<Results1> getResults() {
            return results;
        }

        public class Results1{
            private String name;
            private String address;
            private String latitude;
            private String longitude;

            public String getName() {
                return name;
            }


            public String getAddress() {
                return address;
            }


            public String getLatitude() {
                return latitude;
            }


            public String getLongitude() {
                return longitude;
            }

        }
    }
}
