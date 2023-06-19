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
import com.helloworld.domain.TestCase;
import com.helloworld.service.DockerService;
import com.helloworld.service.FileService;
import com.helloworld.service.SubmitService;
import com.helloworld.service.TestService;

@RestController
@RequestMapping("/tests")
public class TestController {
	
	@Autowired FileService fileService;
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
    	Submit submit = new Submit();
    	String submitId = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    	String userId = (String) session.getAttribute("email");
		if(userId == null) {
			userId = "test";
		}
		long seqId = (long) session.getAttribute("user_id");
		
		com.helloworld.domain.Test test = testService.getTest(testId);
		List<TestCase> testcase = testService.getTestCaseList(testId);
		String path = new ClassPathResource("docker").getPath() + "/" + userId + "/" + testId + "/" + submitId + "/";
		String error = "";
		float score = 0;

		long start = System.currentTimeMillis();
		for(TestCase tc: testcase) {
			long testCaseId = tc.getTestCaseId();
			String tpath = path + testCaseId;
			File input = new File(tpath + "/input.txt");
			try {
	    		BufferedWriter writer = new BufferedWriter(new FileWriter(input));
	    		writer.write(tc.getInput());
	    		writer.close();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
			String output = dockerService.test(type, code, testId, testCaseId, submitId, userId);		
			if(output.equals(tc.getOutput())) {
				score += test.getScore() / testcase.size();
			}
			if(output.contains("error")) {
				error += output + "\n";
			}
		}
		long runTime = System.currentTimeMillis() - start;
		
		File main = null;
		switch(type) {
			case "python":
		    	main = new File(path + "Main.py");
				break;
			case "java":
		    	main = new File(path + "Main.java");
				break;
			case "c":
		    	main = new File(path + "Main.c");
				break;
			default:
				break;
		}
    	try {
    		BufferedWriter writer = new BufferedWriter(new FileWriter(main));
    		writer.write(code);
    		writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
		
		com.helloworld.domain.File codeFile = new com.helloworld.domain.File();
		codeFile.setName(main.getName());
		codeFile.setPath(main.getPath());
		long fileId = fileService.insert(codeFile);
		codeFile.setFileId(fileId);
		
		submit.setSubmitId(Long.parseLong(submitId));
		submit.setSubmitorId(seqId);
		submit.setAssignmentId(test.getAssignmentId());
		submit.setTestId(testId);
		submit.setLanguageType(type);
		submit.setRuntime(runTime);
		submit.setScore(score);
		submit.setFile(codeFile);
		submitService.insert(submit);
		
    	model.put("error", error);
    	model.put("score", score);
    	model.put("code", code);
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
