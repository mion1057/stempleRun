package com.stempleRun.db.dto;

public class app_story {

	private int s_num;
	private String s_title;
	private String s_content;
	private int area_num;
	private String area_name;
	private int s_complete;
	private int s_progress;
	
	
	public int getS_num() {
		return s_num;
	}
	
	public void setS_num(int s_num) {
		this.s_num = s_num;
	}
	
	public String getS_title() {
		return s_title;
	}
	
	public void setS_title(String s_title) {
		this.s_title = s_title;
	}
	
	public String getS_content() {
		return s_content;
	}
	
	public void setS_content(String s_content) {
		this.s_content = s_content;
	}

	public int getArea_num() {
		return area_num;
	}

	public void setArea_num(int area_num) {
		this.area_num = area_num;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public int getS_complete() {
		return s_complete;
	}

	public void setS_complete(int s_complete) {
		this.s_complete = s_complete;
	}

	public int getS_progress() {
		return s_progress;
	}

	public void setS_progress(int s_progress) {
		this.s_progress = s_progress;
	}
	
}
