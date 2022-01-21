package com.stempleRun.db.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.dto.BingoVO;
import com.stempleRun.db.dto.EntityMember;
import com.stempleRun.db.dto.Spot;
import com.stempleRun.db.dto.Story;
import com.stempleRun.db.mapper.SpotMapper;

@Service
public class SpotService {
	
	@Autowired
	SpotMapper spotMapper;
	 
	
	public ArrayList<BingoVO> getUserBingo(EntityMember user){
		return spotMapper.getUserBingo(user);
	}
	public ArrayList<Spot> getWeekly(EntityMember user){
		return spotMapper.getWeekly(user);
	}
	public ArrayList<Spot> getMonthly(EntityMember user){
		return spotMapper.getMonthly(user);
	}
	public ArrayList<Story> getUserStory(EntityMember user){
		return spotMapper.getUserStory(user);
		
	}

}
