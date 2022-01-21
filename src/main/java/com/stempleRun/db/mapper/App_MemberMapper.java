package com.stempleRun.db.mapper;

import java.util.List;

import com.stempleRun.db.dto.App_MemberVO;
import com.stempleRun.db.dto.Member;
import com.stempleRun.db.dto.MemberVO;

public interface App_MemberMapper {

	public int appSignUp(App_MemberVO member) throws Exception;
	
	public List<App_MemberVO> getIdList(String m_id) throws Exception;
	
	public App_MemberVO appAutoLoginCheck(String m_id, String m_pw) throws Exception;

	public App_MemberVO app_getMember(String m_id);
}
	