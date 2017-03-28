package com.jgkj.bxxc.tools;

/**
 * Created by fangzhou on 2016/10/27.
 * 没用的，用来转化计算时间的，但是后来没用上
 */
public class TimeToSecond {
    private String time;
    private StringBuffer sb;
    public TimeToSecond(String time){
        this.time = time;
    }
    public Long getSecond(){
        String[] split = time.split("-");
        Long month = Long.parseLong(split[0]);
        Long day = Long.parseLong(split[1]);
        Long hours = Long.parseLong(split[2]);
        Long second = (month*30*24+day*24+hours)*60*60*1000;
        return second;
    }

}
