package com.stempleRun.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.stempleRun.db.dto.Area;
import com.stempleRun.db.dto.Culture;
import com.stempleRun.db.dto.Hint;
import com.stempleRun.db.dto.Question;
import com.stempleRun.db.dto.Story;
import com.stempleRun.db.dto.Story_change;
import com.stempleRun.db.dto.Story_only;
import com.stempleRun.db.dto.app_story;
import com.stempleRun.db.dto.app_storyStart;
import com.stempleRun.db.service.AreaService;
import com.stempleRun.db.service.CultureService;
import com.stempleRun.db.service.HintService;
import com.stempleRun.db.service.QuestionService;
import com.stempleRun.db.service.StorageService;
import com.stempleRun.db.service.StoryService;
import com.stempleRun.db.service.Story_compService;
import com.stempleRun.db.service.Story_onlyService;
import com.stempleRun.db.service.Story_progService;
import com.stempleRun.db.service.app_StoryService;
import com.stempleRun.db.service.app_storyStartService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/storymake")
public class StoryMakeController {

	private final Logger logger = LoggerFactory.getLogger(StoryMakeController.class);
	
	@Autowired
	CultureService service;

	@Autowired
	AreaService areaService;

	@Autowired
	StoryService storyService;
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	Story_onlyService soService;
	
	@Autowired
	HintService hintService;
	
	@Autowired
	app_StoryService appService;
	
	@Autowired
	app_storyStartService appStartService;
	
	@Autowired
	Story_progService progService;
	
	@Autowired
	Story_compService compService;
	
	String h_fileName = null;
	String n_fileName = null;
	
	@Autowired
	StorageService storageService;

	@RequestMapping(value = "/reg")
	public String register(Model model) {

		try {

			model.addAttribute("areaList", areaService.getAreas());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/story/storyRegister";
	}

	// reg에서 저장버튼 누르면 실행
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(HttpServletRequest request, RedirectAttributes redirectAttr, Model model) throws Exception {

		model.addAttribute("cultureList", service.getCultures());

		
		// 스토리 저장
		Story s = new Story();

		s.setS_title(request.getParameter("s_title"));
		s.setS_content(request.getParameter("s_content"));
		s.setArea_num(Integer.parseInt(request.getParameter("area_num")));

		storyService.register(s);
		
		int s_num = storyService.getS_num();
		
		System.out.println(s_num);

		// 방금 저장된 s_num으로 s_num값 가져오기
		Story nowStory = storyService.findSNum(s_num);
		
		
		redirectAttr.addFlashAttribute("nowStory", nowStory);
		
		return "redirect:/storymake/manage";
	}

	@RequestMapping(value = "/manage")
	public String manage(Model model) {

		try {
			model.addAttribute("cultureList", service.getCultures());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/story/storyManage";
	}
	
	
	
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public String insert(MultipartFile[] n_file, MultipartFile[] h_file, HttpServletRequest request) {//MultipartHttpServletRequest fileRequest
		
		System.out.println("스토리 관리 insert 시작 ----------------");
		
		//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa : " + n_file[0]);
		
		int num = 1;
		
		int h_count = 0;
		
		int count = Integer.parseInt(request.getParameter("count"));
		
		int s_num = Integer.parseInt(request.getParameter("s_num"));
		
//		for(int i=1; i<count+1; i++) {
//			if(request.getParameter("h_t_real_data_"+i) != null) {
//				System.out.println("힌트 텍스트");
//			}
//			else {
//				System.out.println("힌트 파일");
//			}
//		}
		
		
//		for(int aa = 1; aa<count+1; aa++) {
//			System.out.println(aa + "번 h_file : " + h_file[aa-1]);
//			System.out.println(aa + "번 h_file 파일이름 : " + h_file[aa-1].getOriginalFilename());
//			System.out.println(aa + "번 n_file : " + n_file[aa-1]);
//			System.out.println(aa + "번 n_file 파일이름 : " + n_file[aa-1].getOriginalFilename());
//		}
		
		for(int i=1; i<count+1; i++) {
			
			System.out.println("시작 : " + num);
			
			// 문제등록
			Question q = new Question();
			q.setQ_content(request.getParameter("q_real_data_"+i));
			questionService.register(request.getParameter("q_real_data_"+i));
			
			System.out.println("문제 등록 완료 : " + num);
			
			int q_num = questionService.getQ_num();
			
			// 힌트 등록
			Hint h = new Hint();
			System.out.println("힌트 생성 => if문으로 들어갑니다잉 : " + num);
			if(request.getParameter("h_t_real_data_"+i) != null) {
				System.out.println("힌트가 텍스트");
				
				h.setH_content(request.getParameter("h_t_real_data_"+i));
				h.setH_file("없음");
				h.setQ_num(q_num);
				
				hintService.register(h);
				System.out.println("텍스트 힌트 등록 완료 : " + num);
			}
			else{
				System.out.println("힌트가 파일");
				storageService.store(h_file[h_count]);
				h.setH_file(h_file[h_count].getOriginalFilename());
				h.setH_content("없음");
				h.setQ_num(q_num);
				
				hintService.register(h);
				h_count += 1;
				System.out.println("파일 힌트 등록 완료 : " + num);
			}
			
			Culture culture = service.findNum(request.getParameter("c_name"+i));
			
			Story_only so = new Story_only();
			so.setS_num(s_num);
			so.setC_num(culture.getC_num());
			so.setQ_num(q_num);
			so.setS_order(i);
			storageService.store(n_file[i-1]);
			so.setS_file(n_file[i-1].getOriginalFilename());
			
			System.out.println("스토리  only 등록전 : " + num);
			
			soService.register(so);
			
			System.out.println("스토리 only 등록완료 : " + num);
			
			System.out.println("현재 실행 횟수 : " + num);
			num += 1;
		}
		
		return "redirect:/storymake/manage";
	}
	
	
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String update(MultipartFile[] n_file, MultipartFile[] h_file, HttpServletRequest request) throws Exception {//MultipartHttpServletRequest fileRequest
		
		System.out.println("스토리 관리 update 시작 ----------------");
		
		int num = 1;
		int h_count = 0;
		int n_count = 0;
		int count = Integer.parseInt(request.getParameter("count"));
		int s_num = Integer.parseInt(request.getParameter("s_num"));
		
		ArrayList<Story_change> loadData = soService.story_change(s_num);
			
		System.out.println("업데이트 시작 : " + num);
		System.out.println("loadData size : " + loadData.size());
		System.out.println("n_file length : " + n_file.length);
			
		// 문화재를 늘렸을때
		if(loadData.size() < count) {
			System.out.println("문화재를 늘렸을때 시작---------------------");
			for(int i=1; i<count+1; i++) {
				
				// 개수가 겹치는 부분까진 update
				if(num <= loadData.size()) {
					System.out.println("문화재를 늘렸을 때의 겹치는부분의 update 시작========================");
					int q_num = loadData.get(i-1).getQ_num();
					
					// 문제등록
					questionService.change(q_num, request.getParameter("q_real_data_" + i));
					
					System.out.println("문제 업데이트 완료 : " + num);
					System.out.println("힌트 업데이트 시작 : " + num);
					
					if(request.getParameter("h_t_real_data_"+i) != null) {
						System.out.println("힌트가 텍스트");
						
						hintService.content_change(q_num, request.getParameter("h_t_real_data_" + i));
						
						System.out.println("텍스트 힌트 업데이트 완료 : " + num);
					}
//					else if(request.getParameter("h_t_real_data_"+i) == null && request.getParameter("h_f_real_data_"+i) == null) {
//						System.out.println("여긴 갯수 늘렷을때의 갯수가 겹치는부분까진 update인데 힌트파일이랑 텍스트가 없다 => 파일을 고치지 않앗다 => 그냥 통과");
//					}
					else if(request.getParameter("h_check"+i) != null){
						System.out.println("힌트가 파일");
						
						storageService.store(h_file[h_count]);
						
						hintService.file_change(q_num, h_file[h_count].getOriginalFilename());
						
						h_count += 1;
						System.out.println("파일 힌트 업데이트 완료 : " + num);
					}
					
					System.out.println("story_only 업데이트 시작 : " + num);
					
					Culture c = service.findNum(request.getParameter("c_name" + num));
					
					if(request.getParameter("n_check"+i) != null) {
						storageService.store(n_file[n_count]);
						soService.change(s_num, c.getC_num(), i, n_file[n_count].getOriginalFilename());
						n_count += 1;
					}
					
					
					System.out.println("스토리 업데이트 완료");
					System.out.println("--------------------------------");
				}
				// 개수가 추가된 부분부턴 insert
				else {
					System.out.println("문화재를 늘렸을 때의 추가된 부분의 insert 시작========================");
					// 문제등록
					Question q = new Question();
					q.setQ_content(request.getParameter("q_real_data_"+i));
					questionService.register(request.getParameter("q_real_data_"+i));
					
					System.out.println("문제 등록 완료 : " + num);
					
					int q_num = questionService.getQ_num();
					
					// 힌트 등록
					Hint h = new Hint();
					System.out.println("힌트 생성 => if문으로 들어갑니다잉 : " + num);
					if(request.getParameter("h_t_real_data_"+i) != null) {
						System.out.println("힌트가 텍스트");
						
						h.setH_content(request.getParameter("h_t_real_data_"+i));
						h.setH_file("없음");
						h.setQ_num(q_num);
						
						hintService.register(h);
						System.out.println("텍스트 힌트 등록 완료 : " + num);
					}
					else{
						System.out.println("힌트가 파일");
						
						h.setH_file(h_file[h_count].getOriginalFilename());
						h.setH_content("없음");
						h.setQ_num(q_num);
						
						storageService.store(h_file[h_count]);
						
						hintService.register(h);
						
						h_count += 1;
						System.out.println("파일 힌트 등록 완료 : " + num);
					}
					
					Culture culture = service.findNum(request.getParameter("c_name"+i));
					
					Story_only so = new Story_only();
					so.setS_num(s_num);
					so.setC_num(culture.getC_num());
					so.setQ_num(q_num);
					so.setS_order(i);
					storageService.store(n_file[n_count]);
					so.setS_file(n_file[n_count].getOriginalFilename());
					n_count += 1;
					
					System.out.println("스토리  only 등록전 : " + num);
					
					soService.register(so);
					
					System.out.println("스토리 only 등록완료 : " + num);
					
					System.out.println("현재 실행 횟수 : " + num);
					System.out.println("--------------------------------");
				}
				num += 1;
			}
		}
		// 문화재를 줄엿을때
		else if(loadData.size() > count) {
			
			System.out.println("문화재를 줄엿을때 시작--------------");
			
			for(int i=1; i<loadData.size()+1; i++) {
				
				// 넘어온 개수만큼 update
				if(num <= count) {
					
					int q_num = loadData.get(i-1).getQ_num();
					
					// 문제등록
					questionService.change(q_num, request.getParameter("q_real_data_" + i));
					
					System.out.println("문제 업데이트 완료 : " + num);
					System.out.println("힌트 업데이트 시작 : " + num);
					
					if(request.getParameter("h_t_real_data_"+i) != null) {
						System.out.println("힌트가 텍스트");
						
						hintService.content_change(q_num, request.getParameter("h_t_real_data_" + i));
						
						System.out.println("텍스트 힌트 업데이트 완료 : " + num);
					}
					else if(request.getParameter("h_check"+i) != null) {
						System.out.println("힌트가 파일");
						
						storageService.store(h_file[h_count]);
						
						hintService.file_change(q_num, h_file[h_count].getOriginalFilename());
						
						h_count += 1;
						System.out.println("파일 힌트 업데이트 완료 : " + num);
					}
					
					System.out.println("story_only 업데이트 시작 : " + num);
					
					Culture c = service.findNum(request.getParameter("c_name" + num));
					
					if(request.getParameter("n_check"+i) != null) {
						storageService.store(n_file[n_count]);
						
						soService.change(s_num, c.getC_num(), i, n_file[n_count].getOriginalFilename());
						n_count += 1;
						
					}
					
					
					System.out.println("스토리 업데이트 완료");
					System.out.println("--------------------------------");
				}
				
				// 개수를 줄인 뒷 부분부터 삭제 (힌트 => 문제 => story_only)
				else {
					hintService.remove(loadData.get(i-1).getH_num());
					System.out.println("힌트삭제완료");
					soService.remove(s_num, i);
					System.out.println("story_only삭제완료");
					questionService.remove(loadData.get(i-1).getQ_num());
					System.out.println("문제삭제완료");
					
					System.out.println("--------------------------------");
				}
				num += 1;
			}
		}
		
		// 문화재가 같을때
		else {
			System.out.println("문화재 개수가 같을때 시작------------------");
			for(int i=1; i<loadData.size()+1; i++) {
				
				int q_num = loadData.get(i-1).getQ_num();
				
				// 문제등록
				questionService.change(q_num, request.getParameter("q_real_data_" + i));
				
				System.out.println("문제 업데이트 완료 : " + num);
				
				System.out.println("힌트 업데이트 시작 : " + num);
				
				if(request.getParameter("h_t_real_data_"+i) != null) {
					System.out.println("힌트가 텍스트");
					
					hintService.content_change(q_num, request.getParameter("h_t_real_data_" + i));
					
					System.out.println("텍스트 힌트 업데이트 완료 : " + num);
				}
				else if(request.getParameter("h_check"+i) != null) {
					System.out.println("힌트가 파일");
					
					storageService.store(h_file[h_count]);
					
					hintService.file_change(q_num, h_file[h_count].getOriginalFilename());
					
					h_count += 1;
					System.out.println("파일 힌트 업데이트 완료 : " + num);
				}
				
				System.out.println("story_only 업데이트 시작 : " + num);
				
				Culture c = service.findNum(request.getParameter("c_name"+num));
				
				System.out.println("1");
				
				if(request.getParameter("n_check"+i) != null) {
					System.out.println("2");
					storageService.store(n_file[n_count]);
					System.out.println("3");
					soService.change(s_num, c.getC_num(), i, n_file[n_count].getOriginalFilename());
					System.out.println("4");
					n_count += 1;
				}
				
				
				System.out.println("스토리 업데이트 완료");
				System.out.println("--------------------------------");
				
				num += 1;
			}
		}
		return "redirect:/storymake/manage";
	}
	
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public void delete(Story s) {
		System.out.println("asdfkljaskld : " + s.getS_num());
		
		ArrayList<Story_change> so;
		so = soService.story_change(s.getS_num());
		
		for(Story_change a : so) {
			hintService.remove(a.getH_num());
			System.out.println("힌트삭제완료");
			
			soService.remove(s.getS_num(), a.getS_order());
			System.out.println("story_only삭제완료");
			
			questionService.remove(a.getQ_num());
			System.out.println("문제삭제완료");
		}
		storyService.remove(s.getS_num());
		System.out.println("스토리삭제완료");
	}
	
    //스토리 목록
	@RequestMapping(value = "/storycall")
	public String storycall(Model model) {
		model.addAttribute("storylist", storyService.getStorys());
		return "/story/storycall";
	}
	
	//선택한 스토리 내용확인
	@RequestMapping(value = "manage/{s_num}")
	public String bingocalldetail(HttpServletRequest request, @PathVariable int s_num, Model model){	
			
			//문화재 관리 테이블 내 수정작업 시 필요한 문화재 목록 가져오기
			model.addAttribute("cultureList", service.getCultures());
			
			//현재 스토리 이름 출력
			Story nowStory = storyService.findSNum(s_num);
			model.addAttribute("nowStory", nowStory);
			
			//등록된 데이터 출력
			try {
				for(app_storyStart a : appStartService.getNeedPHF(String.valueOf(s_num))) {
					System.out.println(a.getC_name());
					System.out.println(a.getQ_content());
					System.out.println(a.getS_file());
				}
				model.addAttribute("data",appStartService.getNeedPHF(String.valueOf(s_num)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		return "/story/storyManage";
	}
	
//	// 힌트가 텍스트 일때 나레이션 파일 빼고 저장
//	@RequestMapping(value="/manage_save", method = RequestMethod.POST)
//	@ResponseBody
//	public String manage_save(HttpServletRequest request, Question q, Hint h, Story s, Culture c) {
//		
//		
//		//storageService.store(uploadfile);	
//		
//		//questionService.register();
//		
//		Question question = questionService.findNum(q.getQ_content());
//		
//		h.setH_file("없음");
//		h.setQ_num(question.getQ_num());
//		
//		hintService.register(h);
//		
//		Story_only so = new Story_only();
//		
//		so.setS_num(s.getS_num());
//		
//		Culture culture = service.findNum(c.getC_name());
//		
//		so.setC_num(culture.getC_num()); // 이거 해결하기
//		
//		
//		so.setQ_num(question.getQ_num());
//		so.setS_order(q.getQ_num()); // 이거 해결하기
//		
//		so.setS_file(n_fileName);
//		soService.register(so);
//		return null;
//	}
//	
//	 // 힌트가 파일일때  => 힌트파일, 나레이션 파일을 제외한 나머지를 저장
//	 @RequestMapping(value="/otherSave", method = RequestMethod.POST)
//	 @ResponseBody 
//	 public String otherSave(HttpServletRequest request, Question q, Story s, Culture c) {
//	 
//		 	//questionService.register(q);
//			
//			Question question = questionService.findNum(q.getQ_content());
//			
//			System.out.println("등록된 문제 번호 : " + question.getQ_num());
//			
//			Hint h = new Hint();
//			h.setH_num(0);
//			h.setH_content("없음");
//			h.setH_file(h_fileName);
//			h.setQ_num(question.getQ_num());
//			
//			System.out.println("힌트 내용 : " + h.getH_content());
//			System.out.println("힌트 파일 이름 : " + h.getH_file());
//			System.out.println(h.getQ_num());
//			
//			hintService.register(h);
//			
//			Story_only so = new Story_only();
//			
//			System.out.println("등록된 스토리 번호 : " + s.getS_num());
//			
//			so.setS_num(s.getS_num());
//			
//			Culture culture = service.findNum(c.getC_name());
//			so.setC_num(culture.getC_num()); // 이거 해결하기
//			
//			
//			so.setQ_num(question.getQ_num());
//			so.setS_order(q.getQ_num()); // 이거 해결하기
//			
//			so.setS_file(n_fileName);
//			
//			soService.register(so);
//		 
//			System.out.println("등록 완료");
//			
//			return null;
//	 }
//	 
//	// 힌트가 텍스트 일 때 나레이션 파일을 저장
//	@RequestMapping(value="/fileSave1", method = RequestMethod.POST)
//	@ResponseBody
//	public String fileSave1(HttpServletRequest request, @RequestParam("n_file") MultipartFile n_file,Question q, Hint h, Story s, Culture c) { //
//		
//		
//		if(n_file != null) {
//			System.out.println("텍스트로 저장할때 나레이션 파일 이름.................. : " + n_file.getOriginalFilename());
//			storageService.store(n_file);
//			n_fileName = n_file.getOriginalFilename();
//		}
//		else {
//			System.out.println("힌트가 텍스트 일때 ajax에서 파일값이 null임");
//		}
//		
//		return null;
//	}
//	
//	// 힌트가 파일일때 힌트파일과 나레이션 파일을 저장
//		@RequestMapping(value="/fileSave2", method = RequestMethod.POST)
//		@ResponseBody
//		public String fileSave2(MultipartHttpServletRequest fileRequest) { // 파일 2개를 날리때 MultipartHttpServletRequest fileRequest 이거 말고 딴거 적으면 에러남
//			
//			Iterator<String> iterator = fileRequest.getFileNames();
//			
//			String uploadFileName;
//			String sysFileName = "";
//			String orgFileName = "";
//			MultipartFile mFile = null;
//			
//			while(iterator.hasNext()) {
//				uploadFileName = iterator.next();
//				mFile = fileRequest.getFile(uploadFileName);
//				
//				orgFileName = mFile.getOriginalFilename();
//				System.out.println(orgFileName);
//				if(orgFileName != null) {
//					storageService.store(mFile);
//					
//					// 스토리 관리에서 힌트는 사진파일만 나레이션은 mp3만 등록한다는 가정하에 => 사진이 아니면 mp3니까 n_fileName에 넣어주고 => else는 사진파일일때니까 h_fileName변수에 넣어준다.
//					if(orgFileName.indexOf(".mp3") != -1) {
//						System.out.println("녹음파일이네");
//						System.out.println("녹음파일인지 파일명 확인중 : " + orgFileName);
//						n_fileName = orgFileName;
//					}
//					else {
//						System.out.println("사진파일이네");
//						System.out.println("사진파일인지 파일명 확인중 : " + orgFileName);
//						h_fileName = orgFileName;
//					}
//				}
//				else {
//					System.out.println("힌트가 파일 일때 null임 ");
//				}
//			}
//			return null;
//		}
	
//	@RequestMapping(value = "/aaa") //produces = "application/json;charset=utf-8")
//	public @ResponseBody JSONObject json() throws Exception {
//		
//		JSONObject jsonMain = new JSONObject();	// json 객체
//		
//		List<Story> items = storyService.getStorys();
//		JSONArray jArray = new JSONArray(); // json배열
//		
//		for(int i=0; i<items.size(); i++) {
//			Story s = items.get(i);
//			JSONObject row = new JSONObject();
//			
//			row.put("s_num", s.getS_num());
//			row.put("s_title", s.getS_title());
//			row.put("s_content", s.getS_content());
//			row.put("area_num", s.getArea_num());
//			
//			jArray.add(i, row);
//		}
//		// json객체에 배열을 넣음
//		jsonMain.put("sendData", jArray);
//		return jsonMain;
//	}
	
	@RequestMapping(value="/app_area")
	@ResponseBody
	public List<Area> getAreas(HttpServletRequest request) throws Exception {
		System.out.println("지역 진입");
		ArrayList<Area> a = areaService.getAreas();
		for(Area aa : a) {
			System.out.println("area_num : " + aa.getArea_num());
			System.out.println("area_num : " + aa.getArea_name());
		}
		return areaService.getAreas();
	}
	
	@RequestMapping(value="/app_area_storyList")
	@ResponseBody
	public List<app_story> getStoryList(HttpServletRequest request) throws Exception {
		System.out.println("해당 지역 넘버는 : " + request.getParameter("a_num"));
		return appService.getStorys(Integer.parseInt(request.getParameter("a_num")));//(Integer.parseInt(request.getParameter("a_num")));
	}
	
	@RequestMapping(value = "storyStart")
	   @ResponseBody
	   public List<app_storyStart> getStory_only(HttpServletRequest request) throws Exception {
		   System.out.println("앱에서 받아온 스토리 값 : " + request.getParameter("s_num"));
		   
		   ArrayList<app_storyStart> list = appStartService.getNeedPHF(request.getParameter("s_num"));
		   
		   for (app_storyStart a : list) {
			   System.out.println(a.getC_name());
		   }
		   
		   return list;
	   }
	
	@RequestMapping(value="/app_storyDuring")
	@ResponseBody
	public int storyDuring(HttpServletRequest request) {
		
		System.out.println("asdffff : " + request.getParameter("m_num"));
		System.out.println("asdffff : " + request.getParameter("s_num"));
		System.out.println("asdffff : " + request.getParameter("count"));
		
		int m_num = Integer.parseInt(request.getParameter("m_num"));
		int s_num = Integer.parseInt(request.getParameter("s_num"));
		int count = Integer.parseInt(request.getParameter("count"));
		
		
		ArrayList<Story_only> storys = new ArrayList();
		storys = soService.getStoryOnlys(s_num);
		
		int compare = 0;
		
		if(count == 0) {
			for(int i=0; i<storys.size(); i++) {
				progService.reg(m_num, s_num, storys.get(i).getC_num(), 0);
			}
		}
		else {
			if(compare < count) {
				progService.reg(m_num, s_num, storys.get(compare).getC_num(), 1);
			}
			else {
				progService.reg(m_num, s_num, storys.get(compare).getC_num(), 0);
			}
			compare += 1;
		}
		
//		for(Story_only s : storys) {
//			
//			if(count == 1) {
//				
//			}
//			
//			if(compare <= count) {
//				progService.reg(m_num, s_num, s.getC_num(), 1);
//			}
//			else {
//				progService.reg(m_num, s_num, s.getC_num(), 0);
//			}
//			compare += 1;
//		}
		
		
		int i = soService.count(s_num);
		System.out.println("asdffff : " + i);
		
		int j = progService.count(s_num);
		System.out.println("asdffff : " + j);
		
		Double k = (double) ((100 / i) * j); 
		
		int kk = Integer.parseInt(String.valueOf(Math.round(k)));
		
		System.out.println("double : " + k);
		System.out.println("vbadhbvkav : " + kk);
		
		int result = compService.reg(m_num, s_num, 0, kk);
		
		return 1;//result;
	}
	
	@RequestMapping(value="/app_storyEnd")
	@ResponseBody
	public int storyEnd(HttpServletRequest request) {
		System.out.println("asdf : " + request.getParameter("m_num"));
		System.out.println("asdf : " + request.getParameter("s_num"));
		
		int m_num = Integer.parseInt(request.getParameter("m_num"));
		int s_num = Integer.parseInt(request.getParameter("s_num"));
		
		ArrayList<Story_only> storys = new ArrayList();
		storys = soService.getStoryOnlys(s_num);
		
		int s_c_complete = 1;
		
		int check = 0;
		int result = 0;
		
		for(Story_only s : storys) {
			System.out.println("test : " + s.getC_num());
			System.out.println("test : " + s.getS_order());
			
			check = progService.reg(m_num, s_num, s.getC_num(), s_c_complete);
			System.out.println("결과값 : " + check);
			
			if(check == 0) {
				result = 0;
				break;
			}
			else {
				result = 1;
			}
		}
		
		if(result == 1) {
			compService.reg(m_num, s_num, 1, 100);
		}
		
		return result;
	}
	
	
//	   @RequestMapping(value = "/bbb")
//	   // 주소변경예정 @RequestMapping(value = "/android/recordInsert")
//	   @ResponseBody
//	   public List<app_story> getStorys(HttpServletRequest request) throws Exception {
//		/*
//		 * // 안드로이드에게 전달하는 데이터 Map<String, String> result = new HashMap<String,
//		 * String>();
//		 * 
//		 * result.put("data1", request.getParameter("rr_rider"));
//		 * 
//		 * List<Story> items = storyService.getStorys();
//		 * 
//		 * System.out.println(items.size());
//		 * 
//		 * for(int i=0; i<items.size(); i++) {
//		 * 
//		 * Story s = items.get(i);
//		 * 
//		 * result.put("s_num", String.valueOf(s.getS_num())); result.put("s_title",
//		 * s.getS_title()); result.put("s_content", s.getS_content());
//		 * result.put("area_num", String.valueOf(s.getArea_num()));
//		 * 
//		 * }
//		 * 
//		 * ArrayList<app_story> a = appService.getStorys();
//		 * 
//		 * for (app_story app_story : a) { System.out.println(app_story.getArea_name());
//		 * }
//		 */
//	      return appService.getStorys();
//	   }
	   
	   

	   
	   @RequestMapping(value="loadStory", method=RequestMethod.POST)
	   @ResponseBody
	   public HashMap<String, Object> loadStory(Model model) {
		    System.out.println("loadStory() 불러옴");
		   
			ArrayList<Story> loadedData = new ArrayList<Story>();
			HashMap<String, Object> allMap = new HashMap<String, Object>();
			
			try {
				loadedData = storyService.getStorys();	// Story_only 테이블에서 모든 Story_only 열 가져옴
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*for(int i = 0; i < loadedData.size(); i++) {
				System.out.println("count" + i);
				System.out.println(loadedData.get(i).getS_num());
				allMap.put("story_" + i, storyService.findSNum(loadedData.get(i).getS_num()));
				// allMap.put("cultural_" + i, service.getCulture(loadedData.get(i).getC_num()));
				// allMap.put("question_" + i, questionService.getQuestion(loadedData.get(i).getQ_num()));
			}*/
			int i = 0;
			int check = 0;
			for(Story s : loadedData) {
			
				if(check == s.getS_num()) {
					continue;
				}
				else {
					allMap.put("story_" + i, storyService.findSNum(s.getS_num()));
					check = s.getS_num();
					i++;
				}
			}
		   return allMap;
	   }

	   @RequestMapping(value = "/manage/{s_num}", method=RequestMethod.POST)
	   @ResponseBody
	   public ArrayList<ArrayList<app_storyStart>> loadStoryBySNum(@PathVariable("s_num") int s_num) {
			System.out.println("loadStoryBySNum 불러옴");

			// ArrayList<Story_only> loadedData = new ArrayList<Story_only>();
			// HashMap<String, Object> allMap = new HashMap<String, Object>();

			// s_num을 이용해서 story_only 불러오고 story_only 안 c_num 이용하여 무슨 문화재가 등록되있고, q_num 이용하여 어떤 문제가 등록되있는지
			// s_order를 이용하여 순서를 정해준다.

			// 불러온 스토리 Only 데이터 (c_num, s_num, q_num, s_order, s_file)
			// ArrayList<Story_only> storyOnlyData = new ArrayList<Story_only>();
			ArrayList<ArrayList<app_storyStart>> storyData = new ArrayList<ArrayList<app_storyStart>>();
			try {
				storyData.add(appStartService.getNeedPHF(String.valueOf(s_num)));
				System.out.println("storyData : " + storyData);
			} catch (Exception e) {
				//TODO: handle exception
				e.printStackTrace();
			}
			
			// storyOnlyData.add(soService.findStoryOnlyBySNum(s_num));	

			return storyData;

			/*for(int j = 0; j < storyData.size(); j++) {
				// 불러온 문화재 데이터 (c_num, c_name, c_latitude, c_longitude, c_file, m_num, g_code)
				// Culture cultureData = service.getCulture(storyOnlyData.getC_num());
				// 불러온 문제 데이터 (q_num, q_content)
				// Question questionData = questionService.getQuestion(storyOnlyData.getQ_num());
			}*/
			
			

		
			/*try {
				loadedData = soService.getStoryOnly();	
			} catch (Exception e) {
				e.printStackTrace();
			}
			for(int i = 0; i < loadedData.size(); i++) {
				System.out.println("count" + i);
				System.out.println(loadedData.get(i).getS_num());
				allMap.put("story_" + i, storyService.findSNum(loadedData.get(i).getS_num()));
				allMap.put("cultural_" + i, service.getCulture(loadedData.get(i).getC_num()));
				allMap.put("question_" + i, questionService.getQuestion(loadedData.get(i).getQ_num()));
			}*/
			
	   }
	   

	   
	   @RequestMapping(value="/app_test", method=RequestMethod.POST)
	   @ResponseBody
	   public void app_test(MultipartFile file, String title) {
		   storageService.store(file);
	   }
}