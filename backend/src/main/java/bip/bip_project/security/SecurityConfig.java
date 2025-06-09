package bip.bip_project.security;

import bip.bip_project.service.auth.CustomOAuth2SuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String createUserPath = "/users/createUser";
    private static final String createUserVerify2fa = "/users/createUser/register-verify-email";

    private static final String authenticatePath = "/users/authenticate";
    private static final String verify2faPath = "/users/authenticate/auth-verify-2fa";


    // private static final String oauth2DefaultSuccessPath = "/oauth2/success";
    JwtRequestFilter jwtRequestFilter;
    CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                // Ручная обработка ошибок — не редиректим на /login при 403/401
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                })
                .and()

                // Настройка прав доступа
                .authorizeRequests()
                .antMatchers(
                        "/users/createUser",
                        "/users/authenticate",
                        "/users/authenticate/auth-verify-2fa",
                        "/users/createUser/register-verify-email"
                ).permitAll()

                .antMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).hasRole("ADMIN")

                .anyRequest().authenticated()
                .and()

                // OAuth2 логика — используется только при отсутствии JWT
                .oauth2Login()
                .successHandler(customOAuth2SuccessHandler)
                .and()

                // Сессии нужны только для OAuth
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        // JWT фильтр — обрабатывает все запросы ДО UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
