package com.stempleRun.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.dto.App_MemberVO;
import com.stempleRun.db.dto.Member;
import com.stempleRun.db.dto.MemberVO;
import com.stempleRun.db.mapper.App_MemberMapper;

@Service
public class App_MemberService {

	@Autowired
	App_MemberMapper mapper;
	
	public int appSignUp(App_MemberVO member) throws Exception {
		return mapper.appSignUp(member);
	}
	
	public List<App_MemberVO> getIdList(String m_id) throws Exception {
		return mapper.getIdList(m_id);
	}
	
	public App_MemberVO appAutoLoginCheck(String m_id, String m_pw) throws Exception {
		return mapper.appAutoLoginCheck(m_id, m_pw);
	}
	
	public App_MemberVO app_getMember(String m_id) {
		return mapper.app_getMember(m_id);
	}
}
