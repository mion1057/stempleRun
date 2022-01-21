package com.stempleRun.db.mapper;

import java.util.List;

import com.stempleRun.db.dto.App_PostVO;
import com.stempleRun.db.dto.PostVO;
import com.stempleRun.db.dto.RecommendVO;

public interface App_PostMapper {

	public List<App_PostVO> getPostInfo() throws Exception;
	public List<App_PostVO> getPopInfo() throws Exception;
	public List<App_PostVO> getLatestInfo() throws Exception;
	
	public List<PostVO> getPostInfos(int p_num) throws Exception;
	
	public int getAppReco(RecommendVO reco) throws Exception;
	public int appCountReco(int p_num) throws Exception;
	public int appRecommend(int p_num) throws Exception;
	
	// 추천테이블 데이터 받아오기
	public List<RecommendVO> appCheckReco() throws Exception;
	
	// 게시글 삽입
	public int appInsertPost(PostVO post) throws Exception;
	
}
