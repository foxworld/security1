package hello.security.oauth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import hello.security.auth.PrincipalDetails;
import hello.security.model.User;
import hello.security.oauth.provider.FacebookUserInfo;
import hello.security.oauth.provider.GoogleUserInfo;
import hello.security.oauth.provider.NaverUserInfo;
import hello.security.oauth.provider.OAuth2UserInfo;
import hello.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService{
//	@Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired private UserRepository repository;
	
	//구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.debug("userRequest={}", userRequest);
		log.debug("ClientRegistration={}", userRequest.getClientRegistration());  // 어떤OAuth로 로그인했는지 확인
		log.debug("getAccessToken={}", userRequest.getAccessToken().getTokenValue());
		log.debug("getClientRegistration={}", userRequest.getClientRegistration().getClientId());

		OAuth2User oauth2User = super.loadUser(userRequest);
		/* 
		 * 구글로그인버튼 클릭->구글로그인창->로그인완료->code리턴(OAuth-Client 라이브러리)-> AccexxToken요청
		 * userRequest 정보 -> loadUser함수(회원프로필 받아야함)->구글로부터 회원프로필
		 */
		log.debug("loadUser={}", oauth2User.getAttributes());
		
		OAuth2UserInfo oauth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			oauth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			oauth2UserInfo = new FacebookUserInfo(oauth2User.getAttributes());
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			oauth2UserInfo = new NaverUserInfo((Map<String, Object>)oauth2User.getAttributes().get("response"));
		} else {
			log.debug("구글,페이스,네이버 로그인 가능");
		}
		
		String provider = oauth2UserInfo.getProvider();
		String providerId = oauth2UserInfo.getProviderId();
		String username = provider+"_"+providerId; 
//		String password = bCryptPasswordEncoder.encode("foxworld");
		String email = oauth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User userEntity = repository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
				.username(username)
//				.password(password)
				.email(email)
				.role(role)
				.provider(provider)
				.providerId(providerId)
				.build();
			repository.save(userEntity);
		} 
		
		return new PrincipalDetails(userEntity, oauth2User.getAttributes()); 
	}
}
