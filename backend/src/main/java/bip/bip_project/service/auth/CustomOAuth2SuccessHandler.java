package bip.bip_project.service.auth;

import bip.bip_project.exception.user.UserNotFoundException;
import bip.bip_project.model.user.User;
import bip.bip_project.model.user.UserDto;
import bip.bip_project.security.JwtUtil;
import bip.bip_project.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public CustomOAuth2SuccessHandler(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        try {
            User user = userService.getUserByEmail(email);
        } catch (UserNotFoundException ex) {
            userService.createUser(new UserDto(name, "ROLE_USER", null, email));
            userService.setCreateViaGoogle(email);  // устанавливаем что акк создан через Google
            userService.confirmEmail(email);
        }
        // надо перейти по http://localhost:8080/oauth2/authorization/google
        String jwt = jwtUtil.generateToken(userService.getUserByEmail(email));
        response.sendRedirect("/oauth2/success?token=" + jwt);
    }
}
