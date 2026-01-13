package kr.java.pr1mary.service;

import kr.java.pr1mary.entity.CustomOAuth2User;
import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        User.Auth auth = User.Auth.valueOf(provider.toUpperCase());

        String name;
        String email;

        if ("google".equals(provider)) {
            name = oauth2User.getAttribute("name");
            email = oauth2User.getAttribute("email");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = oauth2User.getAttribute("kakao_account");
            String kakaoEmail = (String) kakaoAccount.get("email");

            if (kakaoEmail == null) {
                // 이메일 정보가 없는 경우, 카카오 ID를 기반으로 고유 이메일 생성
                email = oauth2User.getAttribute("id").toString() + "@kakao.com";
            } else {
                email = kakaoEmail;
            }

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                name = (String) profile.get("nickname");
            } else {
                name = "kakao_user";
            }
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 로그인 제공자입니다: " + provider);
        }

        final String finalName = name;
        final String finalEmail = email;

        User user = userAccountRepository
                .findByAuthAndEmail(auth, finalEmail)
                .orElseGet(() -> createOAuth2User(auth, finalName, finalEmail));

        return new CustomOAuth2User(
                user.getId(),
                oauth2User,
                user.getRole(),
                provider
        );
    }

    private User createOAuth2User(User.Auth auth, String name, String email) {
        User newUser = new User(
                email,
                name,
                User.Role.STUDENT,
                auth
        );

        return userAccountRepository.save(newUser);
    }
}
