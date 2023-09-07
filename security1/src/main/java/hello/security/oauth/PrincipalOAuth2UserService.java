package hello.security.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService{
	
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.debug("userRequest={}", userRequest);
		log.debug("ClientRegistration={}", userRequest.getClientRegistration());  // 어떤OAuth로 로그인했는지 확인
		log.debug("getAccessToken={}", userRequest.getAccessToken().getTokenValue());
		log.debug("getClientRegistration={}", userRequest.getClientRegistration().getClientId());

		/* 
		 * 구글로그인버튼 클릭->구글로그인창->로그인완료->code리턴(OAuth-Client 라이브러리)-> AccexxToken요청
		 * userRequest 정보 -> loadUser함수(회원프로필 받아야함)->구글로부터 회원프로필
		 */
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		log.debug("loadUser={}", super.loadUser(userRequest).getAttributes());
		return super.loadUser(userRequest); 
	}
}
