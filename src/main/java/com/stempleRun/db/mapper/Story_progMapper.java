package com.stempleRun.db.mapper;

public interface Story_progMapper {

	public int reg(int m_num, int s_num, int c_num, int s_c_complete);
	
	public int count(int s_num);
	
}
