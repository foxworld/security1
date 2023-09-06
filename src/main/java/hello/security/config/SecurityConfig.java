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

//		http.formLogin(form -> form.loginPage("/login").permitAll());

		// 인증없이 허용 URL 설정
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/user/**").authenticated()
                //.requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                //.requestMatchers("/admin/**").hasRole("ADMIN")
                //.anyRequest().permitAll()
				);
		
		
		return http.build();
	}

}
