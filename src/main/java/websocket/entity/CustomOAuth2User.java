package websocket.entity;

import websocket.entity.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final Long userId;  // 우리 DB의 사용자 ID
    private final OAuth2User oauth2User;
    private final String role;
    private final String provider;

    public CustomOAuth2User(Long userId, OAuth2User oauth2User, User.Role role, String provider) {
        this.userId = userId;
        this.oauth2User = oauth2User;
        this.role = role.name();
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getName() {
        return provider + "_" + oauth2User.getName();
    }
}
