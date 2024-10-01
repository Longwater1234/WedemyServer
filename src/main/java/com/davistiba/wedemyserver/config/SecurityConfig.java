package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.models.UserRole;
import com.davistiba.wedemyserver.service.CustomOAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
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
        return http.cors(Customizer.withDefaults()).httpBasic(Customizer.withDefaults())
                .oauth2Login(x -> x.userInfoEndpoint(config -> config.oidcUserService(googleOauthService)).successHandler(successHandler))
                .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringRequestMatchers("/oauth2/**", "/auth/**"))
                .sessionManagement(s -> s.maximumSessions(2))
                .authorizeHttpRequests((authz) ->
                        authz.requestMatchers("/index.html", "/", "/auth/**", "/favicon.ico", "/login/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/courses/**", "/objectives/**", "/lessons/**", "/reviews/**").permitAll()
                                .requestMatchers("/profile/**", "/user/**").hasAuthority(UserRole.ROLE_STUDENT.name())
                                .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                                .requestMatchers("/admin/**").hasAuthority(UserRole.ROLE_ADMIN.name())
                                .anyRequest().authenticated())
                .with(new MyCustomFilterSetup(successHandler), x -> {
                }).build();
    }

    @Bean
    @Order(1)
    @Profile(value = "debug")
    public SecurityFilterChain securityFilterChainDebug(HttpSecurity http) throws Exception {
        // Disable CSRF and Allow all paths
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    static class MyCustomFilterSetup extends AbstractHttpConfigurer<MyCustomFilterSetup, HttpSecurity> {
        private final CustomAuthSuccessHandler customAuthSuccessHandler;

        public MyCustomFilterSetup(CustomAuthSuccessHandler successHandler) {
            this.customAuthSuccessHandler = successHandler;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilterAt(new CustomLoginHandler(authManager, customAuthSuccessHandler), UsernamePasswordAuthenticationFilter.class);
        }
    }
}

