package com.webecommerce.springboot.security.oauth;

import com.webecommerce.springboot.entity.AuthenticationProvider;
import com.webecommerce.springboot.entity.UserEntity;
import com.webecommerce.springboot.repository.UserRepository;
import com.webecommerce.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        Optional<UserEntity> oUser = Optional.ofNullable(userRepository.findByEmail(email));
        if(oUser.isPresent()) {
            userService.updateUserAfterOAuth2LoginSuccess(oUser.get(), oAuth2User.getName(), AuthenticationProvider.GOOGLE);
        } else {
            userService.createNewUserAfterOAuth2LoginSuccess(email, oAuth2User.getName(), AuthenticationProvider.GOOGLE);
        }
        redirectStrategy.sendRedirect(request, response, "/");
    }
}
