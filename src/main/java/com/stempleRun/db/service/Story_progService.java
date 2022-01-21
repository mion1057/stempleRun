package com.stempleRun.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.mapper.Story_progMapper;

@Service
public class Story_progService {

	@Autowired
	Story_progMapper mapper;
	
	public int reg(int m_num, int s_num, int c_num, int s_c_complete) {
		return mapper.reg(m_num, s_num, c_num, s_c_complete);
	}
	
	public int count(int s_num) {
		return mapper.count(s_num);
	}
	
}
