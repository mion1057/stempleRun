package com.stempleRun.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stempleRun.db.dto.Member;
import com.stempleRun.db.dto.Spot;
import com.stempleRun.db.dto.app_bingolist;
import com.stempleRun.db.dto.app_story;
import com.stempleRun.db.service.App_MypageService;

@Controller
@RequestMapping(value = "/mypage")
public class MypageController {
	@Autowired
	App_MypageService service;
	
	@RequestMapping(value = "/userId")
	@ResponseBody
	public List<Member> appHost(HttpServletRequest request) throws Exception{
		String a = request.getParameter("m_id");
		return service.getUserHost(a);
	}
	
	@RequestMapping(value = "/userStory")
	@ResponseBody
	public List<app_story> getUserStory(HttpServletRequest request) throws Exception{
		String a = request.getParameter("m_id");
		return service.getUserStory(a);
	}
	
	@RequestMapping(value = "/userBingo")
	@ResponseBody
	public List<app_bingolist> getUserBingo(HttpServletRequest request) throws Exception{
		String a = request.getParameter("m_id");
		return service.getUserBingo(a);

	}
	
	@RequestMapping(value = "/userWeek")
	@ResponseBody
	public List<Spot> getWeekly(HttpServletRequest request) throws Exception{
		String a = request.getParameter("m_id");
		return service.getWeekly(a);
	}
	
	@RequestMapping(value = "/usermonth")
	@ResponseBody
	public List<Spot> getMonthly(HttpServletRequest request) throws Exception{
		String a = request.getParameter("m_id");
		return service.getMonthly(a);
	}
}
