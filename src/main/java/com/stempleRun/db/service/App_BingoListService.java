package com.stempleRun.db.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stempleRun.db.dto.app_bingolist;
import com.stempleRun.db.mapper.app_BingoListMapper;

@Service
public class App_BingoListService {
	
	@Autowired
	app_BingoListMapper app_bingoListMapper;
	
	public ArrayList<app_bingolist> getBingoList() {
		return app_bingoListMapper.getBingoList();
	}
}
