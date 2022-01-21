package com.stempleRun.db.mapper;

import java.util.ArrayList;

import com.stempleRun.db.dto.Area;

public interface AreaMapper {

	public ArrayList<Area> getAreas() throws Exception;
	
	//빙고에서 사용하려하면  serverapi 동일한 메소드명이라고 안된대서 동일한기능 메소드 하나 더 추가
	public ArrayList<Area> getbingoAreas(); 
}
