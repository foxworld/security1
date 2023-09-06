package hello.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity // 스피링 시큐리티 필터가 필터체인에 등록된다
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				.requestMatchers(new AntPathRequestMatcher("/h2-console/*"))
	            .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));
	}
	

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);

		// login 상태가 아니면 로그인페이지로 변경
		http.formLogin(form -> form.loginPage("/loginForm").permitAll());

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
