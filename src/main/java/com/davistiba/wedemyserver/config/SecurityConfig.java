package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.service.CustomOAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    /*
    IF YOU DON'T WANT TO USE _COOKIES_ FOR SESSIONS, SIMPLY UNCOMMENT THIS BLOCK BELOW TO USE a SPECIAL HEADER
    "X-AUTH-TOKEN" (expires too) INSTEAD. BUT YOU WILL ALSO NEED TO *MANUALLY* CONFIGURE YOUR FRONTEND TO STORE
    AND RE-USE THIS TOKEN AFTER SUCCESSFUL LOGIN.
     */
    /*----------------------------------------------------
      @Bean
      public HttpSessionIdResolver sessionIdResolver() {
          //SET EXPIRE TIME IN application.yml, similar to cookies:: `session.cookie.max-age`
          return HeaderHttpSessionIdResolver.xAuthToken();
      }
    //----------------------------------------------------*/

    @Autowired
    private CustomOAuthUserService googleOauthService;

    @Autowired
    private CustomAuthSuccessHandler successHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().httpBasic()
                .and().oauth2Login().userInfoEndpoint().oidcUserService(googleOauthService)
                .and().successHandler(successHandler)
                .and().authorizeHttpRequests((authz) ->
                        authz.antMatchers("/index.html", "/", "/auth/**", "/favicon.ico", "/login/**").permitAll()
                                .antMatchers(HttpMethod.GET, "/courses/**", "/objectives/**", "/lessons/**", "/reviews/**").permitAll()
                                .antMatchers("/profile/**", "/user/**").hasAuthority("ROLE_STUDENT")
                                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated())
                .apply(new MyCustomFilterSetup());

        //SESSION and CSRF (you may disable CSRF)
        return http.csrf().disable()
                .sessionManagement(s -> s.maximumSessions(2).and().sessionFixation().newSession()).build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    public class MyCustomFilterSetup extends AbstractHttpConfigurer<MyCustomFilterSetup, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilterAt(new CustomLoginFilter(authManager, successHandler), UsernamePasswordAuthenticationFilter.class);
        }
    }
}

