package com.stempleRun.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.stempleRun.db.dto.App_MemberVO;
import com.stempleRun.db.service.App_MemberService;

@Controller
@RequestMapping("/app_member")
public class App_MemberController {
	
	@Autowired
	App_MemberService service;
	
	@RequestMapping(value ="/app_signup", method = RequestMethod.POST)
	@ResponseBody
	private int appSignUp(@RequestBody App_MemberVO member) throws Exception {
		
		System.out.println(member.getM_name());
		
		return service.appSignUp(member);
	}
	
	@RequestMapping(value ="/app_idcheck", method = RequestMethod.POST)
	@ResponseBody
	private int appIdCheck(HttpServletRequest request) throws Exception {
		
		String a = request.getParameter("m_id");
		List<App_MemberVO> member = service.getIdList(a);
		System.out.println(member);
		return service.getIdList(a).size();
	}
	
	@RequestMapping(value = "/app_autoLoginCheck", method = RequestMethod.POST)
	@ResponseBody
	public int appAutoLoginCheck(HttpServletRequest request) {
		
		System.out.println(request.getParameter("id"));
		System.out.println(request.getParameter("pw"));
		
		int check = 0;
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		try {
			App_MemberVO m = service.appAutoLoginCheck(id, pw);
			if(m != null) {
				if(m.getM_id().equals(id) && m.getM_pw().equals(pw)) {
					check = 1;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("check = " + check);
		return check;
	}
}
