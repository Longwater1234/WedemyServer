package com.davistiba.wedemyserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public MyUserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomOAuthUserService oAuthUserService;

    @Autowired
    private CustomOauthSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().httpBasic()
                .and().oauth2Login().userInfoEndpoint().userService(oAuthUserService)
                .and().successHandler(successHandler)
                .and().authorizeRequests()
                .antMatchers("/index.html", "/", "/auth/**", "/login/**").permitAll()
                .antMatchers(HttpMethod.GET, "/courses/**", "/objectives/**", "/lessons/**").permitAll()
                .antMatchers("/profile/**", "/user/**").hasAuthority("ROLE_USER")
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();

        http.sessionManagement().sessionFixation().newSession().maximumSessions(1);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

}

