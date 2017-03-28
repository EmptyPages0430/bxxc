package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class Version {
    private int code;
    private String reason;
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

    public class  Result{
        private int versionCode;
        private String versionName;
        private String path;

        public int getVersionCode() {
            return versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public String getPath() {
            return path;
        }

    }
}
