package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.service.CustomOAuthUserService;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
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
        return new BCryptPasswordEncoder(12);
    }


    /*
    IF YOU DON'T WANT TO USE _COOKIES_ FOR SESSIONS, UNCOMMENT THIS BLOCK BELOW TO USE a SPECIAL HEADER
    "X-AUTH-TOKEN" INSTEAD. BUT YOU WILL ALSO NEED TO *MANUALLY* CONFIGURE YOUR FRONTEND TO STORE
    AND RE-USE THIS TOKEN AFTER SUCCESSFUL LOGIN.
     */
    /*----------------------------------------------------
      @Bean
      public HttpSessionIdResolver sessionIdResolver() {
          return HeaderHttpSessionIdResolver.xAuthToken();
      }
    //----------------------------------------------------*/

    @Autowired
    private CustomOAuthUserService googleOauthService;

    @Autowired
    private CustomOauthSuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().httpBasic()
                .and().oauth2Login().userInfoEndpoint().oidcUserService(googleOauthService)
                .and().successHandler(successHandler)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .antMatchers("/index.html", "/", "/auth/**", "/favicon.ico", "/login/**").permitAll()
                .antMatchers(HttpMethod.GET, "/courses/**", "/objectives/**", "/lessons/**", "/reviews/**").permitAll()
                .antMatchers("/profile/**", "/user/**").hasAuthority("ROLE_STUDENT")
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();

        //SESSION and CSRF
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and().sessionManagement().maximumSessions(2);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

}

