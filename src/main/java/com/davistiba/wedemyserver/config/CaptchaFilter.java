package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.dto.CaptchaResponse;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Priority;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
@WebFilter(urlPatterns = "/auth/register")
@Priority(1)
public class CaptchaFilter extends GenericFilterBean {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Environment ENV = new StandardEnvironment();
    private static final Logger logger = LoggerFactory.getLogger(CaptchaFilter.class);
    private final RestTemplate restTemplate = new RestTemplateBuilder().rootUri("https://hcaptcha.com").build();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        String token = request.getParameter("responseToken");
        if (token == null || token.isBlank()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", Objects.requireNonNull(ENV.getProperty("HCAPTCHA_SECRET_KEY")));
        requestBody.add("response", token);
        requestBody.add("remoteip", request.getRemoteAddr());
        requestBody.add("sitekey", Objects.requireNonNull(ENV.getProperty("HCAPTCHA_CLIENT_KEY")));

        var formEntity = new HttpEntity<>(requestBody, headers);

        var responseEntity = restTemplate.postForEntity("/siteverify", formEntity, CaptchaResponse.class);
        CaptchaResponse captchaResponse = responseEntity.getBody();
        logger.info(captchaResponse.toString());
        if (captchaResponse.getSuccess()) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(400);
        String jsonRes = mapper.writeValueAsString(new MyCustomResponse(captchaResponse.getErrorCodes().toString(), false));
        response.getWriter().println(jsonRes);
        response.getWriter().flush();
    }

}
