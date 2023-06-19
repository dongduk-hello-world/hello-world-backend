package com.helloworld.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.domain.Submit;
import com.helloworld.service.DockerService;
import com.helloworld.service.SubmitService;
import com.helloworld.service.TestService;

@RestController
@RequestMapping("/tests")
public class TestController {
	
	@Autowired TestService testService;
	@Autowired SubmitService submitService;
	@Autowired DockerService dockerService;

	// test의 정보 가져오기
	@GetMapping("/{testId}")
    public ResponseEntity<com.helloworld.domain.Test> get(@PathVariable long testId) {
		com.helloworld.domain.Test result = testService.getTest(testId);
		return ResponseEntity.ok(result);
    }

	// 문제 풀이 제출
	// 세션에는 최근 N개를 저장해 둠. 별개로 DB와 비교하여 최고점이면 DB insert
    @PostMapping("/{testId}/submits")
    public ResponseEntity<Map<String, Object>> submit (	HttpServletRequest request,
														@RequestBody TestSubmitRequest req,
    													@PathVariable long testId, Map<String, Object> model ) {
		String type = req.getLanguage();
		String code = req.getCode();

		System.out.println(">>>>>>>>> code를 제출");
		System.out.println("testId: " + testId);
		System.out.println("language: " + type);
		System.out.println("<code>\n" + code);

    	HttpSession session = request.getSession();
    	String submitId = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    	String userId = (String) session.getAttribute("user_id");
		if(userId == null) {
			userId = "test";
		}
    	String path = new ClassPathResource("docker").getPath() + "/";
    	String end = "";
    	switch(type) {
    		case "python":
    			end = ".py";
    			/*
    			List<String> require = new ArrayList<>();
    			for(String c: code.split("\n")) {
    				if(c.startsWith("import") || (c.startsWith("from"))) {
    					require.add(c.split(" ")[1]);
    				}
    			}
    	    	String rpath = path + userId + "/requirements.txt";
    	    	File rfile = new File(rpath);
    	    	try {
    	    		BufferedWriter writer = new BufferedWriter(new FileWriter(rfile));
    	    		writer.write(code);
    	    		writer.close();
    	    	} catch (IOException e) {
    	    		e.printStackTrace();
    	    	}
    	    	*/
    			break;
    		case "java":
    			end = ".java";
    			break;
    		case "c":
    			end = ".c";
    			break;
    		default:
    			break;
    	}
    	String spath = userId + "/" + testId + "/" + submitId;
    	String tpath = path + spath;
    	String dpath = tpath + "/Main" + end;
    	File mkdir = new File(tpath);
    	File dfile = new File(dpath);
    	mkdir.mkdirs();
    	try {
    		BufferedWriter writer = new BufferedWriter(new FileWriter(dfile));
    		writer.write(code);
    		writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	Map<Integer, String> result = null;
    	result = dockerService.terminal("pwd");
    	String output = "[실행결과없음]";
    	String cmd = "docker run --rm -v " + result.get(0).replace("\n", "") + "/" + tpath + ":/usr/src/" + spath + "/" + " -w /usr/src/" + spath;
    	switch(type) {
    		case "python":
    			result = dockerService.terminal(cmd + " python:3 python Main.py");
        		if(result.get(0) == null) {
        			int key = 0;
        			for(Integer k : result.keySet()) {
        				key = k;
        			}
        			output = "[error] " + result.get(key) + "\ncode:" + key;
        		} else {
        			output = "[실행결과]" + result.get(0) + "\n";
        		}
    			break;
    		case "java":
    			result = dockerService.terminal(cmd + " openjdk:8 javac Main.java", true);
    			if(result.get(0) == null) {
    				int key = 0;
    				for(Integer k : result.keySet()) {
    					key = k;
    				}
    				output = "[error] " + result.get(key) + "\ncode:" + key;
    			} else {
        			result = dockerService.terminal(cmd + " openjdk:8 java Main", true);
        			if(result.get(0) == null) {
        				int key = 0;
        				for(Integer k : result.keySet()) {
        					key = k;
        				}
        				output = "[error] " + result.get(key) + "\ncode:" + key;
        			} else {
        				output = "[실행결과]" + result.get(0) + "\n";
        			}
    			}
    			break;
    		case "c":
    			result = dockerService.terminal(cmd + " gcc:4.9 gcc -o main main.c", true);
    			if(result.get(0) == null) {
    				int key = 0;
    				for(Integer k : result.keySet()) {
    					key = k;
    				}
    				output = "[error] " + result.get(key) + "\ncode:" + key;
    			} else {
        			result = dockerService.terminal(cmd + " gcc:4.9 ./main", true);
        			if(result.get(0) == null) {
        				int key = 0;
        				for(Integer k : result.keySet()) {
        					key = k;
        				}
        				output = "[error] " + result.get(key) + "\ncode:" + key;
        			} else {
        				output = "[실행결과]" + result.get(0) + "\n";
        			}
    			}
    			break;
    	}
    	// testcase 불러오기
    	// input 넣고 output 체크하기
    	
    	model.put("output", output);
    	return ResponseEntity.ok(model);
    }
	
	// DB에 있는 최고점 제출 정보 + session에 있는 최근 N개의 제출 정보
	@GetMapping("/{testId}/submits")
    public ResponseEntity<Map<Integer, Submit>> getSubjectList(HttpServletRequest request, @PathVariable long testId) {
    	HttpSession session = request.getSession();
    	String userId = (String) session.getAttribute("user_id");
    	List<Submit> submits = new ArrayList<>();
		List<Submit> ssubmit = (List<Submit>) session.getAttribute("submit_" + testId);
		List<Submit> dsubmit = submitService.getSubmitListByTestIdAndUserId(testId, userId);
		submits.addAll(ssubmit);
		submits.addAll(dsubmit);
		Collections.sort(submits, Collections.reverseOrder());
		Map<Integer, Submit> result = new HashMap<>();
		int index = 0;
		for(Submit s: submits) {
			result.put(index++, s);
		}
		session.setAttribute("submit_" + testId, result);
		return ResponseEntity.ok(result);
    }

	// index에 해당하는 제출 정보의 코드 확인.
	// 0이면 DB에 있는 최고점. 1 ~ N 이면 세션에 있는 index로 
	@GetMapping("/{testId}/submits/{index}")
    public ResponseEntity<Submit> submit(HttpServletRequest request, @PathVariable long testId, @PathVariable int index) {
		Map<Integer, Submit> submitMap = getSubjectList(request, testId).getBody();
		return ResponseEntity.ok(submitMap.get(index));
    }
}

class TestSubmitRequest {
	String language;
	String code;
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
