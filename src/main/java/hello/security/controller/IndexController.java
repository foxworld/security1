package hello.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.security.model.User;
import hello.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {
	@Autowired private UserRepository repository;
	@Autowired private BCryptPasswordEncoder passwordEncoder; 
	
	//localhost:8080/
	//localhost:8080
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본폴드 src/main/resource/
		// 뷰리졸버설정 : template(prefix),, mustache(suffix) 생략가능
		return "index";
	}


	@GetMapping("user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("manager")
	public @ResponseBody String manager() {
		return "manager";
	}	

	@GetMapping("admin")
	public @ResponseBody String admin() {
		return "admin";
	}	

	@GetMapping("loginForm")
	public  String loginForm() {
		return "loginForm";
	}

	@GetMapping("joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("join")
	public String join(User user) {
		log.debug("user={}", user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		repository.save(user);
		return "redirect:/loginForm";
	}

	@GetMapping("joginProc")
	public @ResponseBody String joginProc() {
		return "회원가입완료";
	}	
	
}
