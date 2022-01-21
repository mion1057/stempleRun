package com.stempleRun.db.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.mapper.Story_compMapper;

@Service
public class Story_compService {

	@Autowired
	Story_compMapper mapper;
	
	public int reg(int m_num, int s_num, int s_complete, int s_progress) {
		return mapper.reg(m_num, s_num, s_complete, s_progress);
	}
}
