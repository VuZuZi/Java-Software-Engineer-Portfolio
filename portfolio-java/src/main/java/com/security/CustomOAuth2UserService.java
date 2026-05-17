package com.security;

import com.model.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.model.AuthProvider;
import java.util.Map;

import static java.security.AuthProvider.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String avatar = (String) attributes.get("picture");

        // Kiểm tra user đã tồn tại chưa
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            // Tạo user mới
            user = new User();
            user.setEmail(email);
            user.setDisplayName(name);
            user.setAvatarUrl(avatar);
            user.setProvider(AuthProvider.GOOGLE);
            user.setProviderId((String) attributes.get("sub"));
            user = userRepository.save(user);
        }

        return new CustomOAuth2User(oAuth2User, user);
    }
}
