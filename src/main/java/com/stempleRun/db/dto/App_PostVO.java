package com.stempleRun.db.dto;

import java.util.Date;

public class App_PostVO {

	private String p_title;
	private int p_num;
	private Date p_day;
	private int p_recommend;
	private String m_name;
	private String pic_file;
	private int pic_latitude;
	private int pic_longitude;
	
	public void setP_title(String p_title) {
		this.p_title = p_title;
	}
	public int getP_num() {
		return p_num;
	}
	public void setP_num(int p_num) {
		this.p_num = p_num;
	}
	public Date getP_day() {
		return p_day;
	}
	public void setP_day(Date p_day) {
		this.p_day = p_day;
	}
	public int getP_recommend() {
		return p_recommend;
	}
	public void setP_recommend(int p_recommend) {
		this.p_recommend = p_recommend;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public String getPic_file() {
		return pic_file;
	}
	public void setPic_file(String pic_file) {
		this.pic_file = pic_file;
	}
	public int getPic_latitude() {
		return pic_latitude;
	}
	public void setPic_latitude(int pic_latitude) {
		this.pic_latitude = pic_latitude;
	}
	public int getPic_longitude() {
		return pic_longitude;
	}
	public void setPic_longitude(int pic_longitude) {
		this.pic_longitude = pic_longitude;
	}
	public String getP_title() {
		return p_title;
	}


	
	
}
