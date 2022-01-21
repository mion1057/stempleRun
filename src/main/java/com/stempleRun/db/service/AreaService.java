package com.stempleRun.db.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.dto.Area;
import com.stempleRun.db.mapper.AreaMapper;

@Service
public class AreaService {

	@Autowired
	AreaMapper areaMapper;
	
	public ArrayList<Area> getAreas() throws Exception {
		return areaMapper.getAreas();
	}
	
	//빙고에서 사용하려하면  serverapi 동일한 메소드명이라고 안된대서 동일한기능 메소드 하나 더 추가
	public ArrayList<Area> getbingoAreas() throws Exception {
		return areaMapper.getbingoAreas();
	}
	
}
