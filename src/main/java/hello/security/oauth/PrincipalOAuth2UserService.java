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
		log.debug("getClientRegistration={}", userRequest.getClientRegistration());
		log.debug("getAccessToken={}", userRequest.getAccessToken().getTokenValue());
		log.debug("getClientRegistration={}", userRequest.getClientRegistration().getClientId());
		log.debug("loadUser={}", super.loadUser(userRequest).getAttributes());
		return super.loadUser(userRequest); 
	}
}
