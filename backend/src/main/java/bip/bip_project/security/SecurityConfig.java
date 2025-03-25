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
        http.csrf().disable().authorizeRequests().antMatchers(createUserPath, authenticatePath, verify2faPath, createUserVerify2fa).permitAll()
                .anyRequest().authenticated()
                //.anyRequest().permitAll()
                .and()
                    .oauth2Login().successHandler(customOAuth2SuccessHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
