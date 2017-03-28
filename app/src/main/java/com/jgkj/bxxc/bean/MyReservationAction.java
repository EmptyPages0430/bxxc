package com.jgkj.bxxc.bean;

import java.io.Serializable;

public class MyReservationAction implements Serializable {
	/**
	 * 我的预约实体类
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String place;
	private String date;
	
	public MyReservationAction(String name, String place, String date){
		this.name = name;
		this.place = place;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	

}
