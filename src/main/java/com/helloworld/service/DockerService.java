package com.helloworld.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class DockerService {

	public String getId(String tag) {
		String output = "";
		Map<Integer, String> result = new HashMap<>();
		try {
			String cmd = "docker ps -a";
			result = terminal(cmd);
			output = result.get(0);
			String[] dockers = output.split("\n");
			for(String docker: dockers) {
				if(docker.contains(tag)) {
					String[] d = docker.split(" ");
					return d[0];
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "none";
	}
	
	public String build(String filename, String tag) {
		Map<Integer, String> result = new HashMap<>();
		try {
			String cmd = "docker build ";
			if(tag != null) {
				cmd += "-t " + tag + " ";
			}
			if(filename != null) {
				cmd += "-f " + filename;
			}
			result = terminal(cmd);
			if(result.get(0) == null) {
				int key = 0;
				for(Integer k : result.keySet()) {
					key = k;
				}
				throw new Exception(key + ": " + result.get(key));
			}
			String id = this.getId(tag);
			return id;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "none";
	}
	
	public String test(String type, String code, long testId, long testCaseId, String submitId, String userId) {
    	String path = new ClassPathResource("docker").getPath() + "/";
    	String spath = userId + "/" + testId + "/" + submitId + "/" + testCaseId;
    	String tpath = path + spath;
    	File main;
    	File mkdir = new File(tpath);
    	mkdir.mkdirs();
    	switch(type) {
    		case "python":
    	    	main = new File(tpath + "/Main.py");
    	    	try {
    	    		BufferedWriter writer = new BufferedWriter(new FileWriter(main));
    	    		writer.write("import sys\r\n" + "sys.stdin = open('input.txt')\r\n");
    	    		writer.write(code);
    	    		writer.close();
    	    	} catch (IOException e) {
    	    		e.printStackTrace();
    	    	}
    			break;
    		case "java":
    	    	main = new File(tpath + "/Main.java");
    	    	try {
    	    		BufferedWriter writer = new BufferedWriter(new FileWriter(main));
    	    		String newCode = "";
    	    		for(String c: code.split("\n")) {
    	    			if(c.contains("public static void main")) {
    	    				newCode += c.replace("{", "") + " throws Exception {\r\n";
    	    				newCode += "System.setIn(new FileInputStream(\"input.txt\"));\r\n";
    	    			} else {
    	    				newCode += c + "\r\n";
    	    			}
    	    		}
    	    		writer.write(newCode);
    	    		writer.close();
    	    	} catch (IOException e) {
    	    		e.printStackTrace();
    	    	}
    			break;
    		case "c":
    	    	main = new File(tpath + "/Main.c");
    	    	try {
    	    		BufferedWriter writer = new BufferedWriter(new FileWriter(main));
    	    		String newCode ="";
    	    		for(String c: code.split("\n")) {
    	    			if(c.contains("main") && c.contains("(")) {
    	    				newCode += c + "\r\n";
    	    				newCode += "freopen(\"input.txt\", \"r\", stdin);\r\n";
    	    			} else {
    	    				newCode += c + "\r\n";
    	    			}
    	    		}
    	    		writer.write(newCode);
    	    		writer.close();
    	    	} catch (IOException e) {
    	    		e.printStackTrace();
    	    	}
    			break;
    		default:
    			break;
    	}
    	Map<Integer, String> result = null;
    	
    	try {
    		result = terminal("pwd");
    	} catch(Exception e) {
    		result = terminal("cmd /c cd ,");
    	}
    	String output = "[실행결과없음]";
    	String cmd = "docker run --rm -v " + result.get(0).replace("\n", "") + "/" + tpath + ":/usr/src/" + spath + "/" + " -w /usr/src/" + spath;
    	switch(type) {
    		case "python":
    			result = terminal(cmd + " python:3 python Main.py");
        		if(result.get(0) == null) {
        			int key = 0;
        			for(Integer k : result.keySet()) {
        				key = k;
        			}
        			output = result.get(key);
        		} else {
        			output = result.get(0);
        		}
    			break;
    		case "java":
    			result = terminal(cmd + " openjdk:8 javac Main.java", true);
    			output = result.get(0);
    			if(result.get(0) == null) {
    				int key = 0;
    				for(Integer k : result.keySet()) {
    					key = k;
    				}
    				output = result.get(key);
    			} else if(!output.toUpperCase().contains("ERROR") && !output.toUpperCase().contains("EXCEPTION")) {
        			result = terminal(cmd + " openjdk:8 java Main", true);
        			if(result.get(0) == null) {
        				int key = 0;
        				for(Integer k : result.keySet()) {
        					key = k;
        				}
        				output = result.get(key);
            		} else {
            			output = result.get(0);
            		}
    			}
    			break;
    		case "c":
    			result = terminal(cmd + " gcc:4.9 gcc -o main main.c", true);
    			output = result.get(0);
    			if(result.get(0) == null) {
    				int key = 0;
    				for(Integer k : result.keySet()) {
    					key = k;
    				}
    				output = result.get(key);
    			} else if(!output.toUpperCase().contains("ERROR") && !output.toUpperCase().contains("EXCEPTION")) {
        			result = terminal(cmd + " gcc:4.9 ./main", true);
        			if(result.get(0) == null) {
        				int key = 0;
        				for(Integer k : result.keySet()) {
        					key = k;
        				}
        				output = result.get(key);
            		} else {
            			output = result.get(0);
            		}
    			}
    			break;
    	}
    	return output;
	}
	
	public Map<Integer, String> run(List<String> cmd) {
		return run(cmd, false);
	}
	
	public Map<Integer, String> run(List<String> cmd, boolean skip) {
		Map<Integer, String> res = new HashMap<>();
		int code = 0;
		
		ProcessBuilder builder = new ProcessBuilder("/bin/bash");
		builder.command(cmd);
		builder.redirectErrorStream(true);

		try {
			Process process = builder.start();
			code = process.waitFor();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line;
			StringBuilder sb = new StringBuilder();
			if((line=stdout.readLine()) != null) {
				sb.append(line + "\n");
				while((line=stdout.readLine())!=null) {
					sb.append(line + "\n");
				}
				System.out.println("stdout: " + sb.toString().trim());
			} else {
				while((line=stderr.readLine())!=null) {
					sb.append(line + "\n");
				}
				System.out.println("stderr: " + sb.toString().trim());
			}
			String result = sb.toString().trim();
			if(skip) {
				code = 0;
			}
			res.put(code, result);
			if(code != 0) {
				throw new Exception(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public Map<Integer, String> terminal(String cmd, String script) {
		String[] split = cmd.split(" ");
		List<String> cmds = new ArrayList<>();
		for(String c : split) {
			cmds.add(c);
		}
		cmds.add(script);
		System.out.println(cmds);
		return run(cmds);
	}
	
	public Map<Integer, String> terminal(String cmd, boolean skip) {
		String[] split = cmd.split(" ");
		List<String> cmds = new ArrayList<>();
		for(String c : split) {
			cmds.add(c);
		}
		System.out.println(cmds);
		return run(cmds, skip);
	}
	
	public Map<Integer, String> terminal(String cmd) {
		return terminal(cmd, false);
	}
	
	public Map<Integer, String> terminal(List<String> cmd) {
		List<String> cmds = new ArrayList<>();
		cmds.addAll(cmd);
		System.out.println(cmds);
		return run(cmds);
	}
}
