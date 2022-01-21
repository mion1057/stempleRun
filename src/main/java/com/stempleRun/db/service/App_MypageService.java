package com.stempleRun.db.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.dto.Member;
import com.stempleRun.db.dto.Spot;
import com.stempleRun.db.dto.app_bingolist;
import com.stempleRun.db.dto.app_story;
import com.stempleRun.db.mapper.App_MypageMapper;

@Service
public class App_MypageService {

	@Autowired
	App_MypageMapper mapper;
	
	public ArrayList<Member> getUserHost(String m_id) throws Exception{
		return mapper.getUserHost(m_id);
	}
	public ArrayList<app_story> getUserStory(String m_id) throws Exception{
		return mapper.getUserStory(m_id);
	}
	public ArrayList<app_bingolist> getUserBingo(String m_id) throws Exception{
		return mapper.getUserBingo(m_id);
	}
	public ArrayList<Spot> getWeekly(String m_id)throws Exception{
		return mapper.getWeekly(m_id);
	}
	public ArrayList<Spot> getMonthly(String m_id) throws Exception{
		return mapper.getMonthly(m_id);
	}
}
