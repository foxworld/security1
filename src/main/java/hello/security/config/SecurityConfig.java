package hello.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import hello.security.oauth.PrincipalOAuth2UserService;


@Configuration
@EnableWebSecurity // 스피링 시큐리티 필터가 필터체인에 등록된다
/*
 * 특정 주소 접권권한 및 인증을 위한 어노테이션 활성화
 * secured 어노테이션 활성화 @Secured("ROLE_ADMIN")
 * preAuthorize 어노테이션활성화
 */
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) 
public class SecurityConfig {
	
	@Autowired private PrincipalOAuth2UserService principalOAuth2UserService; 

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				.requestMatchers(new AntPathRequestMatcher("/h2-console/*"))
	            .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);

		// login 상태가 아니면 로그인페이지로 변경
		http.formLogin(form -> form.loginPage("/loginForm")
				.loginProcessingUrl("/login") // login 주소가 호출되면 시크리티가 낚아채서 대신 로그인을 진행함
				.defaultSuccessUrl("/")
				.permitAll());
		
		/*
		 * 구글로그인이 완료된 뒤에 후처리 
		 * 1.코드받기(인증)
		 * 2.엑세스토큰(권한)
		 * 3.사용자프로필가져오고
		 * 4.해당 정보로 회워가입을 자동으로 처리
		 * 4-1. 기존정보(이메일,전화번호,이름,아이디)
		 * 4-2. 추가적이 필요한값이 필요할경우 집주소 등 추가 등록
		 * TIP:코드X, (엑세스토큰+사용자프로필정보) 
		 */
		http.oauth2Login(oauth2 -> oauth2
				.loginPage("/loginForm")
				.userInfoEndpoint(user -> user
						.userService(principalOAuth2UserService))
				); 
		
		// 인증없이 허용 URL 설정
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
				.requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll()
				);
		
		
		return http.build();
	}



}
