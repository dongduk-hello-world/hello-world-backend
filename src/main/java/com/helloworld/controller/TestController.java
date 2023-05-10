package com.helloworld.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestController {
	@GetMapping("/{testId}")
    public List<String> get(@PathVariable long testId) {
		// test의 정보 가져오기
		return null;
    }
	
	@PostMapping("/{testId}/submits")
    public List<String> submit(@PathVariable long testId) {
		// 문제 풀이 제출
		// 세션에는 최근 N개를 저장해 둠. 별개로 DB와 비교하여 최고점이면 DB insert
		return null;
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
