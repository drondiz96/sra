package bip.bip_project.service.auth;

import bip.bip_project.exception.user.UserNotFoundException;
import bip.bip_project.model.user.UserDto;
import bip.bip_project.security.JwtUtil;
import bip.bip_project.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
            userService.getUserByEmail(email);
        } catch (UserNotFoundException ex) {
            userService.createUser(new UserDto(name, "ROLE_USER", null, email));
            userService.setCreateViaGoogle(email);  // устанавливаем что акк создан через Google
            userService.confirmEmail(email);
        }

        // Генерация токена
        String jwt = jwtUtil.generateToken(userService.getUserByEmail(email));

        // Установка токена в HttpOnly cookie
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Необходимо поставить true в проде для HTTPS
        cookie.setPath("/"); // Означает что браузер будет прикреплять этот cookie ко всем HTTP-запросам, отправляемым на этот домен, вне зависимости от пути
        cookie.setMaxAge(60 * 60 * 2); // на 2 часика
        response.addCookie(cookie);

        // Редирект на фронт
        response.sendRedirect("http://localhost:3000/welcome");
    }
}
