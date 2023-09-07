package hello.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hello.security.model.User;
import hello.security.repository.UserRepository;


/*
 * 시큐리티 설정에서 loginProcessUrl("/login");
 * login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername으로  함수가 실행
 */
@Service
public class PrincipalDetailsService implements UserDetailsService {
	@Autowired private UserRepository userRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);  
		
		return null;
	}

}
