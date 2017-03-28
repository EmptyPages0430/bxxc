package com.jgkj.bxxc.bean;

import java.io.Serializable;

public class TimeTotal implements Serializable {

	/**
	 * 总结所有预约的时间
	 */
	private static final long serialVersionUID = 1L;
	private String stTime;
	private String enTime;
	private String appNo;
	
	public TimeTotal(String stTime, String enTime){
		this.stTime=stTime;
		this.enTime=enTime;
	}

	public String getStTime() {
		return stTime;
	}

	public void setStTime(String stTime) {
		this.stTime = stTime;
	}

	public String getEnTime() {
		return enTime;
	}

	public void setEnTime(String enTime) {
		this.enTime = enTime;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}



}
