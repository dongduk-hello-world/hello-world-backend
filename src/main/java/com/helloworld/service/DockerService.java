package com.helloworld.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public String test(String id, String type, String input) {
		String output = "";
		Map<Integer, String> result = new HashMap<>();
		try {
			switch(type) {
				case "c":
					result = terminal("docker exec -i " + id + " /bin/bash -c ", "\"cd ~/test/c && echo -e '" + input + "' >> test.c\"");
					result = terminal("docker exec -i " + id + " /bin/bash -c ", "\"cd ~/test/c && gcc test.c -o test.o\"");
					result = terminal("docker exec -i " + id + " /bin/bash -c ", "\"cd ~/test/c && ./test.o\"");
					output = result.get(0);
					break;
				case "java":
					result = terminal("docker exec -i " + id + " /bin/bash -c 'cd ~/test/java && echo -e '" + input + "' >> test.java'");
					result = terminal("docker exec -i " + id + " /bin/bash -c 'cd ~/test/java && javac -d Test.java'");
					result = terminal("docker exec -i " + id + " /bin/bash -c 'cd ~/test/java && java Test.class'");
					output = result.get(0);
					break;
				case "python":
					result = terminal("docker exec -i " + id + " /bin/bash -c \"rm ~/test/python test.py\"");
					result = terminal("docker exec -i " + id + " /bin/bash -c \"cd ~/test/python && echo -e '" + input + "' >> test.py\"");
					result = terminal("docker exec -i " + id + " /bin/bash -c \"cd ~/test/python && python test.py\"");
					output = result.get(0);
					break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public Map<Integer, String> run(List<String> cmd) {
		return run(cmd, false);
	}
	
	public Map<Integer, String> run(List<String> cmd, boolean skip) {
		Map<Integer, String> res = new HashMap<>();
		int code = 0;
		
		ProcessBuilder builder = new ProcessBuilder("C:\\Windows\\System32\\cmd.exe");
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
				System.out.println("stdout: " + sb.toString());
			} else {
				while((line=stderr.readLine())!=null) {
					sb.append(line + "\n");
				}
				System.out.println("stderr: " + sb.toString());
			}
			String result = sb.toString();
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
	
	public Map<Integer, String> terminal(String cmd) {
		String[] split = cmd.split(" ");
		List<String> cmds = new ArrayList<>();
		for(String c : split) {
			cmds.add(c);
		}
		System.out.println(cmds);
		return run(cmds);
	}
	
	public Map<Integer, String> terminal(List<String> cmd) {
		List<String> cmds = new ArrayList<>();
		cmds.addAll(cmd);
		System.out.println(cmds);
		return run(cmds);
	}
	
	public Map<Integer, String> exec(String id, List<String> cmd) {
		List<String> cmds = new ArrayList<>();
		cmds.add("docker");
		cmds.add("exec");
		cmds.add("-i");
		cmds.add(id);
		cmds.addAll(cmd);
		return terminal(cmds);
	}
	
	public Map<Integer, String> exec(String id, String[] cmd) {
		List<String> cmds = new ArrayList<>();
		cmds.add("docker");
		cmds.add("exec");
		cmds.add("-i");
		cmds.add(id);
		for(String c: cmd) {
			cmds.add(c);
		}
		return terminal(cmds);
	}
}
