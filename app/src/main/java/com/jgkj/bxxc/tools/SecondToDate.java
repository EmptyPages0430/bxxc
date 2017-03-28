package com.jgkj.bxxc.tools;


/**
 * Created by fangzhou on 2016/10/27.
 *
 * 格式化时间，活动倒计时
 */
public class SecondToDate {
    private long second;
    private long se;
    private long minute;
    private long hour;
    private long day;
    private long month;
    long mi = 60;
    long hh = 60 * 60 ;
    long dd = 24 * 60 * 60 ;
    long mm = 30 * 24 * 60 * 60 ;
    private StringBuffer sb;
    public SecondToDate(long second){
        this.second = second;
    }
    public String getDate(){
         month = second/mm;
         day = (second-month*mm) / dd;
         hour = (second -month*mm- day * dd) / hh;
         minute = (second -month*mm- day * dd - hour * hh) / mi;
         se = second -month*mm- day * dd - hour * hh - minute * mi;
        sb = new StringBuffer();
        if(month>0){
            sb.append(month+"个月");
        }
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(se > 0) {
            sb.append(se+"秒");
        }
        return sb.toString();
    }
}
