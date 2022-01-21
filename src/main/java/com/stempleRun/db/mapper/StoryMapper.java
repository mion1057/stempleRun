package com.stempleRun.db.mapper;

import java.util.ArrayList;

import com.stempleRun.db.dto.Story;

public interface StoryMapper {

	public ArrayList<Story> getStorys();
	
	public void insert(Story s);
	
	public Story findSNum(int s_num);
	
	public Story findNum(String s_title);

	public ArrayList<Story> getStoryList(int a_num);
	
	public int getS_num();
	
	public void remove(int s_num);
}