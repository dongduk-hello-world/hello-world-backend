package com.helloworld.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.domain.Lecture;
import com.helloworld.domain.User;
import com.helloworld.service.LectureService;
import com.helloworld.service.UserService;

@RestController
@RequestMapping("/classes")
public class ClassController {
	
	@Autowired LectureService lectureService;
	@Autowired UserService userService;
	
	// class list 반환
	// 기본적으로 전체, query string에 따라 검색해서
	@GetMapping
    public ResponseEntity<Map<String, Object>> getClassList(@RequestParam String term, @RequestParam String professor, @RequestParam String language, Map<String, Object> model) { // query string 추가 필요
		List<ClassResponse> classes = new ArrayList<>();
		List<Lecture> list = lectureService.findByFilter(term, professor, language);
		for(Lecture l: list) {
			User u = userService.getUser(l.getProfessor_id());
			ClassResponse c = new ClassResponse();
			c.setClassId(l.getLecture_id());
			c.setClassName(l.getName());
			c.setDivide(l.getDivide());
			c.setPeriod(l.getPeriod());
			c.setProfessor(u.getName());
			classes.add(c);
		}
		model.put("classes", classes);
		return ResponseEntity.ok(model);
    }
	
	@PostMapping
    public List<String> add() {
		// request body에 있는 정보로 class 등록
		return null;
    }
	
	@GetMapping("/{classId}")
    public Map<String, String> get(@PathVariable long classId) {
		// class의 상세 정보
		Map<String, String> classInfo = new HashMap<>();
		
		classInfo.put("classId", "0");
		classInfo.put("className", "소프트웨어시스템개발");
		classInfo.put("professor", "박창섭");
		classInfo.put("period", "2023년 1학기");
		classInfo.put("divide", "1");
		
		return classInfo;
    }
	@PutMapping("/{classId}")
	public List<String> update(@PathVariable long classId) {
		// class 정보 변경
		return null;
    }
	@DeleteMapping("/{classId}")
	public List<String> delete(@PathVariable long classId) { 
		// class 삭제
		return null;
    }
	
	@GetMapping("/{classId}/assignments")
	public List<Map> getAssignmentList(@PathVariable long classId) { 
		// class에 개설된 assignment list
		List<Map> assignmentList = new ArrayList<Map>();
		Map<String, String> assignmentInfo1 = new HashMap<>();
		Map<String, String> assignmentInfo2 = new HashMap<>();
		
		assignmentInfo1.put("assignmentId", "0");
		assignmentInfo1.put("assignmentName", "중간고사 테스트");
		assignmentInfo1.put("writer", "박창섭");
		assignmentInfo1.put("startTime", "2023/02/02 17:30");
		assignmentInfo1.put("endTime", "2023/02/02 19:30");
		
		assignmentList.add(assignmentInfo1);
		
		assignmentInfo2.put("assignmentId", "1");
		assignmentInfo2.put("assignmentName", "기말고사 테스트");
		assignmentInfo2.put("writer", "박창섭");
		assignmentInfo2.put("startTime", "2023/06/02 17:30");
		assignmentInfo2.put("endTime", "2023/06/02 19:30");
		
		assignmentList.add(assignmentInfo2);
		
		return assignmentList;
    }
	
	@GetMapping("/{classId}/students")
	public List<List<String>> getStudentList(@PathVariable long classId) { 
		// class를 수강하고 있는 student list
		List<List<String>> studentList = new ArrayList<List<String>>();
		
		//임시
		List<String> student1 = new ArrayList<String>();
		List<String> student2 = new ArrayList<String>();
		List<String> student3 = new ArrayList<String>();
		List<String> student4 = new ArrayList<String>();
		
		student1.add("전유영");
		student1.add("20201015");
		studentList.add(student1);
		student2.add("전유영");
		student2.add("20201012");
		studentList.add(student2);
		student3.add("전유영");
		student3.add("20201013");
		studentList.add(student3);
		student4.add("전유영");
		student4.add("20201014");
		studentList.add(student4);
		
		return studentList;
    }
	@PostMapping("/{classId}/students/{userId}/{code}")
	public List<String> join(@PathVariable long classId, @PathVariable long userId, @PathVariable String code) { 
		// class 수강 등록
		return null;
    }
	@DeleteMapping("/{classId}/students/{userId}")
	public List<String> withdraw(@PathVariable long classId, @PathVariable long userId) { 
		// class 수강 탈퇴
		return null;
    }
	
	
}

class ClassResponse {
	private long classId;
	private int divide;
	private String className, professor, period;
	
	public ClassResponse() {};
	
	public long getClassId() {
		return classId;
	}
	public void setClassId(long classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getProfessor() {
		return professor;
	}
	public void setProfessor(String professor) {
		this.professor = professor;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public int getDivide() {
		return divide;
	}
	public void setDivide(int divide) {
		this.divide = divide;
	}
}