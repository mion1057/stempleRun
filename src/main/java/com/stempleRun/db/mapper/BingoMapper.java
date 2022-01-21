package com.stempleRun.db.mapper;

import com.stempleRun.db.dto.BingoVO;

public interface BingoMapper {
	
	public void bingoinsert(BingoVO b);
	
	public BingoVO findBingoNum(String b_title);
	
	public BingoVO findBingoTitle(int b_num);
	
	public BingoVO findBingodetails(BingoVO b);	
	
	//빙고 삭제
	public int bingoDelete(int b_num);
}
