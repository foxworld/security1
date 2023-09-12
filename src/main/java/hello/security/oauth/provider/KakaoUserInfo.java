package hello.security.oauth.provider;

import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{
	
	private final Map<String, Object> attributes; //getAttributes()

	@Override
	public String getProviderId() {
		// TODO Auto-generated method stub
		return (String) attributes.get("id");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}
	
	

}
