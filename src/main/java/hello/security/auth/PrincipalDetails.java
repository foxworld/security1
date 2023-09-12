package hello.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import hello.security.model.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
/* 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다
 * 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다(Security ContextHolder에 저장)
 * 오브젝트 => Authentication 타입객체
 *           - User정보가 존재
 *           User오브젝트타입 -> UserDetails 타입개체
 *           
 * Security Session => Authentication => UserDetails
 */
 
@Slf4j
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
	private User user; //콤포지션
	private Map<String, Object> attributes;
	
	// 일반로그인일때 사용
	public PrincipalDetails(User user) {
		this.user = user;
	}

	// OAuth2 로그인할때 사용
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	
	// 해당 User의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				return user.getRoles();
			}
		});
					
		return collection;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 1년동안 회원이 로그인을 안하면 휴면계정으로 하기 위함
		// 현재시간-로긴시간 => 1년초과하면 return false;
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {

		return attributes;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
