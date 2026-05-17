package com.security;

import com.model.User;
import com.model.AuthProvider;
import com.model.Role;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String ADMIN_EMAIL = "markdoan38@gmail.com";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setDisplayName(name);
            user.setAvatarUrl(picture);
            user.setProvider(AuthProvider.GOOGLE);
            user.setProviderId((String) attributes.get("sub"));

            // 🔥 QUAN TRỌNG: Gán role ADMIN nếu email khớp
            if (ADMIN_EMAIL.equals(email)) {
                user.setRole(Role.ADMIN);
                System.out.println("✅ Đã cấp quyền ADMIN cho: " + email);
            } else {
                user.setRole(Role.USER);
            }

            user = userRepository.save(user);
        } else {
            // Nếu user đã tồn tại, kiểm tra lại role
            if (ADMIN_EMAIL.equals(email) && user.getRole() != Role.ADMIN) {
                user.setRole(Role.ADMIN);
                user = userRepository.save(user);
                System.out.println("✅ Đã nâng cấp lên ADMIN cho: " + email);
            }
        }

        return new CustomOAuth2User(oAuth2User, user);
    }
}