package com.jgkj.bxxc.bean;

import java.util.List;

/**
 * Created by fangzhou on 2016/12/15.
 * 预约时间
 */

public class AppointTime {
        private String timeone;
        private List<Res> res;

    public String getTimeone() {
        return timeone;
    }

    public void setTimeone(String timeone) {
        this.timeone = timeone;
    }

    public List<Res> getRes() {
        return res;
    }

    public void setRes(List<Res> res) {
        this.res = res;
    }

    public class Res {
            private String time_slot;

            private int count;

            public String getTime_slot() {
                return time_slot;
            }

            public void setTime_slot(String time_slot) {
                this.time_slot = time_slot;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
}
