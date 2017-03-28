package com.jgkj.bxxc.bean;

/**
 * Created by fangzhou on 2016/11/9.
 * 预约详情
 */
public class AppAction {
    //教练名
    private String coachName;
    //预约时间
    private String time;
    //预约驾校
    private String schoolName;
    //预约科目
    private String sub;

    public AppAction(String coachName, String time, String sub, String schoolName) {
        this.coachName = coachName;
        this.time = time;
        this.sub = sub;
        this.schoolName = schoolName;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
}
