package com.helloworld.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.domain.Assignment;
import com.helloworld.domain.Lecture;
import com.helloworld.domain.User;
import com.helloworld.service.AssignmentService;
import com.helloworld.service.LectureService;
import com.helloworld.service.UserService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/classes")
public class ClassController {
	
	@Autowired LectureService lectureService;
	@Autowired UserService userService;
	@Autowired AssignmentService assignmentService;
	
	// class list 반환
	// 기본적으로 전체, query string에 따라 검색해서
	@GetMapping
    public ResponseEntity<Map<String, Object>> getClassList(@RequestParam(required=false) String term, @RequestParam(required=false) String professor, @RequestParam(required=false) String language, Map<String, Object> model) {
		List<ClassResponse> classes = new ArrayList<>();
		Set<Lecture> set = new HashSet<>();
		if(term != null) {
			List<Lecture> tList = lectureService.findByFilterterm(term);
			set.addAll(tList);
		}
		if(professor != null) {
			List<Lecture> pList = lectureService.findByFilterprofessor(professor);
			set.addAll(pList);
		}
		if(language != null) {
			List<Lecture> lList = lectureService.findByFilterlanguage(language);
			set.addAll(lList);
		}
		for(Lecture l: set) {
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
	
	@Transactional
	@PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void add(HttpServletRequest request, @RequestBody ClassRequest req) {
		// request body에 있는 정보로 class 등록
		HttpSession session = request.getSession();
		long userId = (long) session.getAttribute("user_id");
		
		Lecture l = new Lecture();
		l.setProfessor_id(userId);
		l.setName(req.getName());;
		l.setDescription(req.getDescription());
		l.setInvite_code(req.getInvite_code());
		l.setPeriod(req.getPeriod());
		l.setDivide(req.getDivide());
		
		lectureService.insertLectureAndId(l);
    }

	// class의 상세 정보
	@GetMapping("/{classId}")
    public ResponseEntity<ClassResponse> get(@PathVariable long classId) {
		ClassResponse response = new ClassResponse();
		Lecture l = lectureService.getLecture(classId);
		User u = userService.getUser(l.getProfessor_id());
		response.setClassId(classId);
		response.setClassName(l.getName());
		response.setDivide(l.getDivide());
		response.setPeriod(l.getPeriod());
		response.setProfessor(u.getName());
		return ResponseEntity.ok(response);
    }
	
	@Transactional
    @ResponseStatus(HttpStatus.OK)
	@PutMapping("/{classId}")
	public void update(@RequestBody ClassRequest req, @PathVariable long classId) {
		// class 정보 변경
		Lecture l = lectureService.getLecture(classId);
		l.setName(req.getName());
		l.setPeriod(req.getPeriod());
		l.setDescription(req.getDescription());
		l.setInvite_code(req.getInvite_code());
		
		lectureService.updateLecture(l);
    }
	@DeleteMapping("/{classId}")
	public void delete(@PathVariable long classId) { 
		// class 삭제
		Lecture l = lectureService.getLecture(classId);
		lectureService.deleteLecture(l);
    }
	
	@Transactional
	@GetMapping("/{classId}/assignments")
	public List<Assignment> getAssignmentList(@PathVariable long classId) { 
		// class에 개설된 assignment list
		List<Assignment> assignments = assignmentService.findByLectureId(classId);
		return assignments;
    }
	
	@Transactional
	@GetMapping("/{classId}/students")
	public List<User> getStudentList(@PathVariable long classId) { 
		// class를 수강하고 있는 student list
		List<User> student = lectureService.getStudent(classId);
		return student;
    }
	@Transactional
	@PostMapping("/{classId}/students/{userId}/{code}")
	public void join(@PathVariable long classId, @PathVariable long userId, @PathVariable String code) { 
		// 초대 코드 일치할 때 작성 필요
		lectureService.signUpLecture(userId, classId);
    }
	@Transactional
	@DeleteMapping("/{classId}/students/{userId}")
	public void withdraw(@PathVariable long classId, @PathVariable long userId) { 
		// class 수강 탈퇴
		System.out.println("값 도달" + classId + ", " +  userId);
		lectureService.withdrawStudent(userId, classId);
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

class ClassRequest {
	private long class_id;
	private long professor_id;
	
	private int divide;
	private String invite_code;
	private String name;
	private String description;
	private String period;
	
	public ClassRequest() {}

	public long getClass_id() {
		return class_id;
	}

	public void setClass_id(long class_id) {
		this.class_id = class_id;
	}

	public long getProfessor_id() {
		return professor_id;
	}

	public void setProfessor_id(long professor_id) {
		this.professor_id = professor_id;
	}

	public int getDivide() {
		return divide;
	}

	public void setDivide(int divide) {
		this.divide = divide;
	}

	public String getInvite_code() {
		return invite_code;
	}

	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
}