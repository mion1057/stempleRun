package com.stempleRun.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stempleRun.db.dto.EntityMember;
import com.stempleRun.db.repository.MemberJpaRepository;
import com.stempleRun.db.service.Bingo_CallService;
import com.stempleRun.db.service.MemberService;
import com.stempleRun.db.service.PostService;
import com.stempleRun.db.service.SpotService;
import com.stempleRun.db.service.StoryService;



@Controller
@RequestMapping("/hotspot")
public class SpotController {
	
	@Autowired
	private MemberService service;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private SpotService spotService;
	
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private Bingo_CallService bingo_callservice;
	
	@RequestMapping(value = "/mypage")
	public String postList(Model model, HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
		try {
			EntityMember user = MemberJpaRepository.findByUserId(userDetails.getUsername());
			
			model.addAttribute("story", spotService.getUserStory(user));
			model.addAttribute("bingo", spotService.getUserBingo(user));
			model.addAttribute("week", spotService.getWeekly(user));
			model.addAttribute("month", spotService.getMonthly(user));
			
			//model.addAttribute("story", spotService);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/hotspot/mypage";
	}
	
//	@GetMapping(value="/test")
//	@ResponseBody
//	public ArrayList<Picture> getMap(){
//		return spotService.getPictures();	
//	}
//	@GetMapping(value="/test/week")
//	@ResponseBody
//	public ArrayList<Picture> getMap2(){
//		return spotService.getWeekPictures();	
//	}
//	@GetMapping(value="/test/month")
//	@ResponseBody
//	public ArrayList<Picture> getMap3(){
//		return spotService.getMonthPictures();	
//	}
//	
//	@GetMapping(value="/test/day")
//	@ResponseBody
//	public ArrayList<Picture> getMap1(){
//		return spotService.getDayPictures();	
//	}
	
	
	
	@RequestMapping(value="/hotspot_locate")
	public String LocateHotspot(Model model) {
		try {
			model.addAttribute("postList", postService.getList());
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/hotspot/hotspot_locate";
	}
	
	@RequestMapping(value="/hotspot_main")
	public String MainHotspot(Model model) {
		
		try {
			model.addAttribute("postList", postService.getList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "/hotspot/hotspot_main";
	}
	


}
