package hello.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.security.auth.PrincipalDetails;
import hello.security.model.User;
import hello.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {
	@Autowired private UserRepository repository;
	@Autowired private BCryptPasswordEncoder passwordEncoder; 
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
		log.debug("PrincipalDetails====================================");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		log.debug("authentication={}", principalDetails.getUser());
		log.debug("UserDetails={}", userDetails.getUser());
		return "세션정보확인하기";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth) {
		log.debug("oauth2User====================================");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		log.debug("authentication={}", oauth2User.getAttributes());
		log.debug("oauth2User={}", oauth.getAttributes());
		return "세션정보확인하기";
	}

	
	//localhost:8080/
	//localhost:8080
	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본폴드 src/main/resource/
		// 뷰리졸버설정 : template(prefix),, mustache(suffix) 생략가능
		return "index";
	}

	@GetMapping("user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		log.debug("principalDetails={}", principalDetails.getUser());
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

	@GetMapping("login")
	public  String loginForm() {
		return "loginForm";
	}

	@GetMapping("join")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("join")
	public String join(User user) {
		log.debug("user={}", user);
		user.setRoles("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		repository.save(user);
		return "redirect:/login";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}	

	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}		
}
