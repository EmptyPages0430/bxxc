package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2016/12/20.
 * 场地所有信息
 */

public class SchoolShow {
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
    /**
     * 距离
     */
    private String distance;
    /**
     * 距离
     */
    private String school_aera;
    /**
     * marker对应的A、B、C之类的字母
     */
    private String marker;

    public SchoolShow(int id, int sid, String sname, String faddress, String longitude, String latitude,
                      String sfile, String distance, String school_aera, String marker) {
        this.id = id;
        this.sid = sid;
        this.sname = sname;
        this.faddress = faddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.sfile = sfile;
        this.distance = distance;
        this.school_aera = school_aera;
        this.marker = marker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getFaddress() {
        return faddress;
    }

    public void setFaddress(String faddress) {
        this.faddress = faddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSfile() {
        return sfile;
    }

    public void setSfile(String sfile) {
        this.sfile = sfile;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSchool_aera() {
        return school_aera;
    }

    public void setSchool_aera(String school_aera) {
        this.school_aera = school_aera;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }
}
