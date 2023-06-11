package com.helloworld.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helloworld.service.DockerService;

@RestController
@RequestMapping("/tests")
public class TestController {
	
	@Autowired DockerService dockerService;
	
	@GetMapping("/{testId}")
    public List<String> get(@PathVariable long testId) {
		// test의 정보 가져오기
		return null;
    }

	// 문제 풀이 제출
	// 세션에는 최근 N개를 저장해 둠. 별개로 DB와 비교하여 최고점이면 DB insert
    @PostMapping("/{testId}/submits")
    public ResponseEntity<Map<String, Object>> submit (	HttpServletRequest request,
    														@RequestParam String type,
    														@RequestParam String code,
    														@PathVariable long testId, Map<String, Object> model ) {
    	HttpSession session = request.getSession();
    	String submitId = new SimpleDateFormat("yyyyMMdd-HHmmss").format(Calendar.getInstance().getTime());
    	String userId = (String) session.getAttribute("user_id");
    	String path = new ClassPathResource("docker").getPath() + "/";
    	String end = "";
		List<String> require = new ArrayList<>();
    	switch(type) {
    		case "python":
    			end = ".py";
    			for(String c: code.split("\n")) {
    				if(c.startsWith("import") || (c.startsWith("from"))) {
    					require.add(c.split(" ")[1]);
    				}
    			}
    	    	String rpath = path + "/" + userId + "/requirements.txt";
    	    	File rfile = new File(rpath);
    	    	try {
    	    		BufferedWriter writer = new BufferedWriter(new FileWriter(rfile));
    	    		writer.write(code);
    	    		writer.close();
    	    	} catch (IOException e) {
    	    		e.printStackTrace();
    	    	}
    			break;
    		case "java":
    			end = ".java";
    			for(String c: code.split("\n")) {
    				if(c.startsWith("import")) {
    					require.add(c.split(" ")[1]);
    				}
    			}
    			break;
    		case "c":
    			end = ".c";
    			for(String c: code.split("\n")) {
    				if(c.startsWith("#include")) {
    					require.add(c.split(" ")[1]);
    				}
    			}
    			break;
    		default:
    			break;
    	}
    	String dpath = path + "/" + userId + "/" + testId + "_" + submitId + end;
    	File dfile = new File(dpath);
    	try {
    		BufferedWriter writer = new BufferedWriter(new FileWriter(dfile));
    		writer.write(code);
    		writer.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
		String tag = userId + "_" + type;
		String containerId = dockerService.getId(tag);
    	if(containerId.equals("none")) {
        	String cpath = new ClassPathResource("docker").getPath() + "/" + userId + "/Dockerfile_" + type;
        	File cfile = new File(cpath);
        	try {
        		BufferedWriter writer = new BufferedWriter(new FileWriter(cfile));
        		switch(type) {
	        		case "python":
		        		writer.write("FROM python:3\n");
		        		writer.write("WORKDIR /usr/src/app\n");
		        		if(!require.isEmpty()) {
		        			writer.write("COPY ./requirements.txt ./\n");
		        			writer.write("RUN pip install --no-cache-dir -r requirements.txt\r\n");
		        		}
		        		break;
	        		case "java":
	        			writer.write("FROM openjdk:8\n");
	        			writer.write("WORKDIR /usr/src/app\n");
        		}
        		writer.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}        	
    		containerId = dockerService.build(cpath, tag);
    	} else {
    		Map<Integer, String> result = dockerService.terminal(containerId, "docker run -i --rm --name " + tag + " -w /usr/src/myapp python:3 python " + dfile);
    		String output = result.get(0);
        	model.put("output", output);
    	}
    	return ResponseEntity.ok(model);
    }
	
	
	@GetMapping("/{testId}/submits")
    public List<String> getSubjectList(@PathVariable long userId) {
		// DB에 있는 최고점 제출 정보 + session에 있는 최근 N개의 제출 정보
		return null;
    }
	
	@PostMapping("/{testId}/submits/{index}")
    public List<String> submit(@PathVariable long testId, @PathVariable int index) {
		// index에 해당하는 제출 정보의 코드 확인.
		// 0이면 DB에 있는 최고점. 1 ~ N 이면 세션에 있는 index로 
		return null;
    }
}
