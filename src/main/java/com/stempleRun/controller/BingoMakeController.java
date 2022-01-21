package com.stempleRun.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stempleRun.db.dto.App_bingo;
import com.stempleRun.db.dto.Area;
import com.stempleRun.db.dto.BingoVO;
import com.stempleRun.db.dto.Bingo_onlyVO;
import com.stempleRun.db.dto.Culture;
import com.stempleRun.db.dto.Hint;
import com.stempleRun.db.dto.Question;
import com.stempleRun.db.dto.ReviewBoardVO;
import com.stempleRun.db.dto.Story;
import com.stempleRun.db.dto.app_bingolist;
import com.stempleRun.db.dto.app_story;
import com.stempleRun.db.service.App_bingoService;
import com.stempleRun.db.service.AreaService;
import com.stempleRun.db.service.BingoService;
import com.stempleRun.db.service.Bingo_CallService;
import com.stempleRun.db.service.Bingo_onlyService;
import com.stempleRun.db.service.CultureService;
import com.stempleRun.db.service.StoryService;
import com.stempleRun.db.service.App_BingoListService;

@Controller
@RequestMapping("/bingo")
public class BingoMakeController {
	@Autowired
	AreaService areaService;
	
	@Autowired
	CultureService service;
	
	@Autowired
	BingoService bingoService;
	
	@Autowired
	Bingo_onlyService boService;
	
	@Autowired
	Bingo_CallService bingo_callservice;
	
	@Autowired
	App_bingoService app_bingoService;
	
	@Autowired
	App_BingoListService app_BingoListService;
	
	
	//1. 빙고 생성페이지로 이동 (큰틀)
	@RequestMapping(value = "/bingoadd")
	public String register(Model model) {
		
		try {
			model.addAttribute("areaList", areaService.getAreas());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		

		return "/bingo/bingoadd";
	}
	
	
	//2. 빙고 생성페이지에서 저장을 통해 내용저장 (큰틀)
	@RequestMapping(value = "/bingosave", method = RequestMethod.POST)
	public String save(HttpServletRequest request, RedirectAttributes redirectAttr, Model model) throws Exception {

		model.addAttribute("cultureList", service.getCultures());

		
		// 빙고 저장
		BingoVO b = new BingoVO();
		
		//빙고VO에 내용변경
		b.setB_title(request.getParameter("b_title"));
		b.setB_content(request.getParameter("b_content"));
		b.setArea_num(Integer.parseInt(request.getParameter("area_num")));

		bingoService.register(b); 
		
		//빙고관리페이지에서 어떤 빙고인지 판별하기위한 기능
		BingoVO nowBingo = bingoService.findBingoNum(request.getParameter("b_title"));
		
		System.out.println(nowBingo.getB_num());
		System.out.println(nowBingo.getB_title());
		System.out.println(nowBingo.getB_content());
		
		//빙고관리페이지에서 어떤 빙고인지 판별하기위한 기능2		
		redirectAttr.addFlashAttribute("nowBingo", nowBingo);
		
		return "redirect:/bingo/manage"; 
	}
	
	
	//3. 빙고 관리페이지로 이동(문화재 목록, 이름을 가지고)
	@RequestMapping(value = "/manage")
	public String manage(Model model) {
		int addcheck = 0;
		try {
			model.addAttribute("addcheck", addcheck);
			model.addAttribute("cultureList", service.getCultures());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/bingo/bingomanage";
	}
	
	 //4. 빙고 관리페이지 내 데이터 저장 후 빙고목록으로 이동
	 @RequestMapping(value="/otherSave", method = RequestMethod.POST)
	 @ResponseBody 
	 public String otherSave(HttpServletRequest request, BingoVO b, Culture c, @RequestParam(value="list[]") List<String> list) {
	 
		 	int a = b.getB_num();
		 	int count = 1;

		 	for(String list2 : list) {
				Bingo_onlyVO bo = new Bingo_onlyVO();
				bo.setB_num(a);
				Culture culture = service.findNum(list2);
				bo.setC_num(culture.getC_num());
				bo.setB_order(count);
				count = count+1;
				boService.register(bo);
				System.out.println(bo);
	        }
		 	return "/bingo/bingomanage";
	 }

	//5. 빙고 불러오기 리스트	
	@RequestMapping(value = "/bingocall")
	public String call(Model model) {
		model.addAttribute("bingo",bingo_callservice.getbingoList());
		return "/bingo/bingocall";
	}
	
	//6. 선택한 빙고 내용확인
	@RequestMapping(value = "bingocalldetail/{b_num}")
	public String bingocalldetail(HttpServletRequest request, RedirectAttributes redirectAttr, @PathVariable int b_num, Model model) throws Exception {	
			//수정으로 버튼값주기
			int addcheck = 1;
			model.addAttribute("addcheck", addcheck);
			//문화재 관리 테이블 내 수정작업 시 필요한 문화재 목록 가져오기
			model.addAttribute("cultureList", service.getCultures());
			//현재 빙고 이름 출력
			model.addAttribute("nowBingo", bingoService.findBingoTitle(b_num));
			//문화재 관리 테이블 + bingo_only테이블 조인 후 해당 빙고에 맞는 문화재 출력
			model.addAttribute("bingocultureList", app_bingoService.bingogetCultures(b_num));
			System.out.println(b_num);
		return "/bingo/bingomanage";
	}
	
	//7. 내용 수정
	@RequestMapping(value = "updateSave", method=RequestMethod.POST)
	@ResponseBody
	private String boardUpdateProc(HttpServletRequest request, BingoVO b, Culture c, @RequestParam(value="list[]") List<String> list) throws Exception{		
		
		
		int b_num = Integer.parseInt(request.getParameter("b_num"));
		System.out.println(request.getParameter("b_num"));

		app_bingoService.changedelete(b_num);
		
	 	int a = b.getB_num();
	 	int count = 1;

	 	for(String list2 : list) {
			Bingo_onlyVO bo = new Bingo_onlyVO();
			bo.setB_num(a);
			Culture culture = service.findNum(list2);
			bo.setC_num(culture.getC_num());
			bo.setB_order(count);
			count = count+1;
			boService.register(bo);
			System.out.println(bo);
        }
	 	
	 	System.out.println("수정이상없다!!!!!!!");
	 	return "redirect:/bingo/bingomanage";
    }
	   
	//8. 빙고 삭제
	    @RequestMapping("bingodelete/{b_num}")
	    private String bingoDelete(@PathVariable int b_num) throws Exception{
	        
			BingoVO bingo = new BingoVO();
			
	    	boService.bingoSubDelete(b_num);
	    	bingoService.bingoDelete(b_num);
	    	
	        return "redirect:/bingo/bingocall";
	    }  
	    
		
	 //9. [앱] 빙고길잡이에 지역목록을 띄워보자
		@RequestMapping(value="/Bingo_app_area")
		@ResponseBody
		public List<Area> getAreas(HttpServletRequest request) throws Exception {
			System.out.println("지역목록");
			ArrayList<Area> BingoArea = areaService.getbingoAreas();
			for(Area aa : BingoArea) {
				System.out.println("area_num : " + aa.getArea_num());
				System.out.println("area_num : " + aa.getArea_name());
			}
			return areaService.getbingoAreas();
		}
		
	 //10. [앱] 지역에 맞는 빙고목록을 띄워보자
		@RequestMapping(value = "/Bingo_app_list")
	    @ResponseBody
		   public List<app_bingolist> getBingoList(HttpServletRequest request) {
		      return app_BingoListService.getBingoList();
		   }	
}
