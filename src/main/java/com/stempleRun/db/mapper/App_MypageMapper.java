package com.stempleRun.db.mapper;

import java.util.ArrayList;

import com.stempleRun.db.dto.Member;
import com.stempleRun.db.dto.Spot;
import com.stempleRun.db.dto.app_bingolist;
import com.stempleRun.db.dto.app_story;

public interface App_MypageMapper {
	public ArrayList<Member> getUserHost(String m_id) throws Exception;
	
	public ArrayList<app_story> getUserStory(String m_id) throws Exception;
	
	public ArrayList<app_bingolist> getUserBingo(String m_id)throws Exception;
	
	public ArrayList<Spot> getWeekly(String m_id) throws Exception;
	
	public ArrayList<Spot> getMonthly(String m_id) throws Exception;
	
}
