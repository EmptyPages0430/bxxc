package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2017/1/17.
 *
 */

public class CreateDay_Time {

    private String day;
    private List<String> time;
    private List<Boolean> isApp;
    private List<Integer> count;

    public CreateDay_Time(String day, List<String> time, List<Boolean> isApp, List<Integer> count) {
        this.day = day;
        this.time = time;
        this.isApp = isApp;
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Integer> getCount() {
        return count;
    }

    public void setCount(List<Integer> count) {
        this.count = count;
    }

    public List<Boolean> getIsApp() {
        return isApp;
    }

    public void setIsApp(List<Boolean> isApp) {
        this.isApp = isApp;
    }
}
