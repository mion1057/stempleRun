package com.stempleRun.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stempleRun.db.dto.App_PostVO;
import com.stempleRun.db.dto.PostVO;
import com.stempleRun.db.dto.RecommendVO;
import com.stempleRun.db.service.App_PostService;
import com.stempleRun.db.service.PostService;

@Controller
@RequestMapping("/app_post")
public class App_PostController {

	
	@Autowired
	App_PostService service;
	
	@Autowired
	PostService postService;
	
	@RequestMapping(value = "/post")
	@ResponseBody
	public List<App_PostVO> getPostInfo(HttpServletRequest request) throws Exception {
		System.out.println("사진마당접근");
		return service.getPostInfo();
	}
	
	@RequestMapping(value ="/popular")
	@ResponseBody
	public List<App_PostVO> getPopInfo() throws Exception {
		System.out.println("인기순접근");
		return service.getPopInfo();
	}
	
	@RequestMapping(value="/latest")
	@ResponseBody
	public List<App_PostVO> getLatestInfo() throws Exception {
		System.out.println("최신순접근");
		return service.getLatestInfo();
	}
	
	@RequestMapping(value ="/postInfoList")
	@ResponseBody
	public List<PostVO> getPostInfos(HttpServletRequest request) throws Exception {
		
		int a = Integer.parseInt(request.getParameter("p_num"));
		
		System.out.println(request.getParameter("p_num"));
		service.getPostInfos(a);
		
		System.out.println(service.getPostInfos(a));
		
		return service.getPostInfos(Integer.parseInt(request.getParameter("p_num")));
	}
	
	@ResponseBody
	@RequestMapping(value = "/app_reco", method = RequestMethod.POST)
	private int getAppReco(HttpServletRequest request, RecommendVO reco) throws Exception {
		
		System.out.println("접근");
		request.getParameter("p_num");
		request.getParameter("m_id");
		
		int p_num = Integer.parseInt(request.getParameter("p_num"));
		System.out.println("게시글번호 " + request.getParameter("p_num"));
		System.out.println("회원아이디 " + request.getParameter("m_id"));
		
		reco.setP_num(Integer.parseInt(request.getParameter("p_num")));
		reco.setM_id(request.getParameter("m_id"));
		
		RecommendVO recommend = postService.compareReco(reco);
		if(recommend == null) {
			service.getAppReco(reco);
			service.appRecommend(p_num);	
		} else {
			
		}
		
		System.out.println("포스트테이블 추천수 " + service.appCountReco(p_num));

		return service.appCountReco(p_num);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/app_reco_check")
	public int appCheckReco(HttpServletRequest request, RecommendVO reco) throws Exception {
		
		int check;
		
		reco.setP_num(Integer.parseInt(request.getParameter("p_num")));
		reco.setM_id(request.getParameter("m_id"));
		
		RecommendVO recommend = postService.compareReco(reco);
		if(recommend == null) {
			check = 0;
		} else {
			check = 1;
		}
		
		System.out.println("리턴할 체크값 " + check);
		
		return check;
				
	}
}
