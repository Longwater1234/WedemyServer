package com.davistiba.wedemyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class WedemyserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(WedemyserverApplication.class, args);
	}

	@Configuration
	@EnableWebSecurity
	@Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().httpBasic().and().authorizeRequests()
					.antMatchers("/index.html", "/", "/auth/**", "/login")
					.permitAll().anyRequest().authenticated();
		}
	}

}
