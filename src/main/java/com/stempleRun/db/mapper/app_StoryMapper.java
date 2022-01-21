package com.stempleRun.db.mapper;

import java.util.ArrayList;

import com.stempleRun.db.dto.app_story;

public interface app_StoryMapper {

	public ArrayList<app_story> getStorys(int area_num) throws Exception;
	
}
