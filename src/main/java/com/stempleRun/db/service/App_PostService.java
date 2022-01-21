package com.stempleRun.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.dto.App_PostVO;
import com.stempleRun.db.dto.PostVO;
import com.stempleRun.db.dto.RecommendVO;
import com.stempleRun.db.mapper.App_PostMapper;

@Service
public class App_PostService {

	@Autowired
	App_PostMapper mapper;
	
	public List<App_PostVO> getPostInfo() throws Exception {
		return mapper.getPostInfo();
	}
	
	public List<App_PostVO> getPopInfo() throws Exception {
		return mapper.getPopInfo();
	}
	
	public List<App_PostVO> getLatestInfo() throws Exception {
		return mapper.getLatestInfo();
	}
	
	public List<PostVO> getPostInfos(int p_num) throws Exception {
		return mapper.getPostInfos(p_num);
	}
	
	public int getAppReco(RecommendVO reco) throws Exception {
		return mapper.getAppReco(reco);
	}
	
	public int appCountReco(int p_num) throws Exception {
		return mapper.appCountReco(p_num);
	}
	
	public int appRecommend(int p_num) throws Exception {
		return mapper.appRecommend(p_num);
	}
	
	public List<RecommendVO> appCheckReco() throws Exception {
		return mapper.appCheckReco();
	}
}
