package com.helloworld.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.domain.Submit;
import com.helloworld.domain.Test;
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
	
	// 알고리즘 검증
	@GetMapping("/session/{score}")
	public ResponseEntity<Map<Integer, Float>> get(HttpServletRequest request, @PathVariable float score) {
		HttpSession session = request.getSession();
		Map<Integer, Float> data = (Map<Integer, Float>) session.getAttribute("top");		
		if(data == null) {
			data = new HashMap<>();
			data.put(0, score);
			session.setAttribute("top", data);
		} else {
			data.put(data.size(), score);
			List<Float> list = new ArrayList<>(data.values());
			Collections.sort(list);
			data = new HashMap<>();
			for(int i = 0; i < list.size() && i < 5; i++) {
				data.put(i, list.get(i));
			}
			session.setAttribute("top", data);
		}
		return ResponseEntity.ok(data);
	}
	
	// test의 정보 가져오기
	@GetMapping("/{testId}")
    public ResponseEntity<TestResponseByTest> get(@PathVariable long testId) {
		com.helloworld.domain.Test test = testService.getTest(testId);
		TestResponseByTest resultTest = new TestResponseByTest();
		if(test != null) {			
			resultTest.setName(test.getName());
			resultTest.setDescription(test.getDescription());
			resultTest.setScore(test.getScore());
			List<TestCaseResponseByTest> resultTestCase = new ArrayList<>();
			for(TestCase tc: test.getTestCaseList()) {
				TestCaseResponseByTest testcase = new TestCaseResponseByTest();
				testcase.setInput(tc.getInput());
				testcase.setOutput(tc.getOutput());
				resultTestCase.add(testcase);
			}
			resultTest.setTestcases(resultTestCase);
		}
		return ResponseEntity.ok(resultTest);
    }

	// 문제 풀이 제출
	// 세션에는 최근 N개를 저장해 둠. 별개로 DB와 비교하여 최고점이면 DB insert
    @PostMapping("/{testId}/submits")
    @ResponseStatus(HttpStatus.OK)
    public void submit(HttpServletRequest request, @RequestBody TestSubmitRequest req, @PathVariable long testId) {
		String type = req.getLanguage();
		String code = req.getCode();

		System.out.println(">>>>>>>>> code를 제출");
		System.out.println("testId: " + testId);
		System.out.println("language: " + type);
		System.out.println("<code>\n" + code);

    	HttpSession session = request.getSession();
    	Submit submit = new Submit();
    	String containerId = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    	String userId = (String) session.getAttribute("uid");
		long seqId = (long) session.getAttribute("user_id");
		
		com.helloworld.domain.Test test = testService.getTest(testId);
		List<TestCase> testcase = testService.getTestCaseList(testId);
		String path = new ClassPathResource("docker").getPath() + "/" + userId + "/" + testId + "/" + containerId + "/";
		String error = "";
		float score = 0;

		long start = System.currentTimeMillis();
		for(TestCase tc: testcase) {
			long testCaseId = tc.getTestCaseId();
			String tpath = path + testCaseId;
	    	File mkdir = new File(tpath);
	    	mkdir.mkdirs();
			File input = new File(tpath + "/input.txt");
	    	System.out.println(input.getPath());
			try {
	    		BufferedWriter writer = new BufferedWriter(new FileWriter(input));
	    		writer.write(tc.getInput());
	    		writer.close();
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
			String output = dockerService.test(type, code, testId, testCaseId, containerId, userId);
			if(output.equals(tc.getOutput())) {
				score += test.getScore() / testcase.size();
			}
			if(error.isEmpty() && (output.toUpperCase().contains("ERROR") || output.toUpperCase().contains("EXCEPTION"))) {
				error = output;
			}
		}
		long runTime = System.currentTimeMillis() - start;
		
		File main = null;
		long fileSize = 0;
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
			fileSize = Files.size(Paths.get(main.getPath()));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}

    	String sessionId = "test#" + testId;
		Map<Integer, TestSubmitSession> data = null;
		try {
			data = (Map<Integer, TestSubmitSession>) session.getAttribute(sessionId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		TestSubmitSession ts = new TestSubmitSession();
		ts.setSubmitTime(containerId);
		ts.setRunTime(runTime);
		ts.setCode(code);
		ts.setLanguage(type);
		ts.setErrorMsg(error);
		ts.setFileSize(fileSize);
		ts.setScore(score);
		
		if(data == null) {
			data = new HashMap<>();
			data.put(0, ts);
			session.setAttribute(sessionId, data);
		} else {
			data.put(data.size(), ts);
			List<TestSubmitSession> list = new ArrayList<>(data.values());
			Collections.sort(list);
			data = new HashMap<>();
			for(int i = 0; i < list.size() && i < 5; i++) {
				data.put(i, list.get(i));
			}
			session.setAttribute(sessionId, data);
			System.out.println(list);
			System.out.println(data);
		}
		
		if(data.get(0).getScore() == ts.getScore()) {
			List<Submit> s = submitService.getSubmitListByAssignmentIdAndUserIdAndTestId(test.getAssignmentId(), seqId, testId);
			if(!s.isEmpty()) {
				submit.setSubmitId(s.get(0).getSubmitId());
			}
			com.helloworld.domain.File codeFile = new com.helloworld.domain.File();
			codeFile.setName(main.getName());
			codeFile.setPath(main.getPath());
			long fileId = fileService.insert(codeFile);
			codeFile.setFileId(fileId);

			submit.setSubmitorId(seqId);
			submit.setAssignmentId(test.getAssignmentId());
			submit.setTestId(testId);
			submit.setLanguageType(type);
			submit.setRuntime(runTime);
			submit.setScore(score);
			submit.setFile(codeFile);
			submit.setContainerId(Long.parseLong(containerId));
			submitService.insert(submit);
		}
    }
	
	// DB에 있는 최고점 제출 정보 + session에 있는 최근 N개의 제출 정보
	@GetMapping("/{testId}/submits")
    public ResponseEntity<Map<String, Object>> getSubjectList(HttpServletRequest request, @PathVariable long testId, Map<String, Object> model) {
    	HttpSession session = request.getSession();
    	String sessionId = "test#" + testId;
    	Test test = testService.getTest(testId);
		Map<Integer, TestSubmitSession> data = null;
		try {
			data = (Map<Integer, TestSubmitSession>) session.getAttribute(sessionId);
			System.out.println(data);
		} catch(Exception e) {
			e.printStackTrace();
		}
		List<TestSubmitSession> submits = new ArrayList<>();
		HighScoreResponse highScore = new HighScoreResponse();
		
		if(data != null && data.size() > 0) {
			for(int i = 0; i < 5; i++) {
				if(data.get(i) == null) {
					break;
				}
				submits.add(data.get(i));
			}
			TestSubmitSession top = data.get(0);
			CodeResponse code = new CodeResponse();
			code.setCode(top.getCode());
			code.setError(top.getErrorMsg());
			code.setLanguage(top.getLanguage());
			highScore.setScore(top.getScore() + "/" + test.getScore());
			highScore.setCode(code);
		}
		
		model.put("highScore", highScore);
		model.put("submits", submits);
		return ResponseEntity.ok(model);
    }

	// index에 해당하는 제출 정보의 코드 확인.
	// 0이면 DB에 있는 최고점. 1 ~ N 이면 세션에 있는 index로 
	@GetMapping("/{testId}/submits/{index}")
    public ResponseEntity<CodeResponse> submit(HttpServletRequest request, @PathVariable long testId, @PathVariable int index) {
		HttpSession session = request.getSession();
		String sessionId = "test#" + testId;
		Map<Integer, TestSubmitSession> data = null;
		try {
			data = (Map<Integer, TestSubmitSession>) session.getAttribute(sessionId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		CodeResponse response = new CodeResponse();
		if(data != null && data.get(index) != null) {
			TestSubmitSession result = data.get(index);
			response.setCode(result.getCode());
			response.setError(result.getErrorMsg());
			response.setLanguage(result.getLanguage());
		}
		return ResponseEntity.ok(response);
    }
}

class HighScoreResponse {
	private String score;
	private CodeResponse code;
	
	public HighScoreResponse() {}
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public CodeResponse getCode() {
		return code;
	}
	public void setCode(CodeResponse code) {
		this.code = code;
	}
}
class CodeResponse {
	private String language, code, error;

	public CodeResponse() {}
	
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
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}

class TestResponseByTest {
	private String name, description;
	private float score;
	private List<TestCaseResponseByTest> testcases;
	
	public TestResponseByTest() {}
	
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
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public List<TestCaseResponseByTest> getTestcases() {
		return testcases;
	}
	public void setTestcases(List<TestCaseResponseByTest> testcases) {
		this.testcases = testcases;
	}
}

class TestCaseResponseByTest {
	private String input, output;

	public TestCaseResponseByTest() {}
	
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
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

class TestSubmitSession implements Comparable<TestSubmitSession> {
	private long runTime;
	private float fileSize, score;
	private String submitTime, language, errorMsg, code;
	
	public TestSubmitSession() {}
	
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	public float getFileSize() {
		return fileSize;
	}
	public void setFileSize(float fileSize) {
		this.fileSize = fileSize;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public int compareTo(TestSubmitSession ts) {
		if(this.score < ts.getScore()) {
			return 1;
		} else if (this.score > ts.getScore()) {
			return -1;
		} else {
			return -this.submitTime.compareTo(ts.getSubmitTime());
		}
	}
}