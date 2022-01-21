package com.stempleRun.db.mapper;

import java.util.ArrayList;

import com.stempleRun.db.dto.BingoVO;
import com.stempleRun.db.dto.EntityMember;
import com.stempleRun.db.dto.Spot;
import com.stempleRun.db.dto.Story;

public interface SpotMapper {


	public ArrayList<Spot> getUserHost(String m_id);

	public ArrayList<Spot> getWeekly(EntityMember user);

	public ArrayList<Spot> getMonthly(EntityMember user);

	public ArrayList<Story> getUserStory(EntityMember user);

	public ArrayList<BingoVO> getUserBingo(EntityMember user);


}
