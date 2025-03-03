package com.davistiba.wedemyserver.config;

import com.davistiba.wedemyserver.dto.CaptchaResponse;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Priority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.stream.Stream;

@Component
@WebFilter(urlPatterns = {"/auth/register", "/auth/login"})
@Priority(1)
@Profile(value = "prod")
public class HCaptchaFilter extends OncePerRequestFilter {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Environment ENV = new StandardEnvironment();
    private static final Logger logger = LoggerFactory.getLogger(HCaptchaFilter.class);
    private static final RestTemplate restTemplate = new RestTemplateBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .rootUri("https://hcaptcha.com").build();

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, @NotNull FilterChain filterChain)
            throws IOException, ServletException {

        String token = request.getParameter("responseToken");
        String requestPath = request.getRequestURI().substring(request.getContextPath().length());

        if (Stream.of("/auth/register", "/auth/login").noneMatch(requestPath::equalsIgnoreCase)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token == null || token.isBlank()) {
            this.abortWithError(response, new MyCustomResponse("Captcha token missing", false));
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", ENV.getProperty("HCAPTCHA_SECRET_KEY"));
        requestBody.add("response", token);
        requestBody.add("remoteip", request.getRemoteAddr());
        requestBody.add("sitekey", ENV.getProperty("HCAPTCHA_CLIENT_KEY"));

        var formEntity = new HttpEntity<>(requestBody, headers);
        logger.info("formEntity {}", formEntity.getBody());

        var responseEntity = restTemplate.postForEntity("/siteverify", formEntity, CaptchaResponse.class);
        CaptchaResponse captchaResponse = responseEntity.getBody();
        logger.info("captcha Response {}", captchaResponse);
        if (captchaResponse == null || !captchaResponse.getSuccess()) {
            assert captchaResponse != null;
            MyCustomResponse payload = new MyCustomResponse(String.valueOf(captchaResponse.getErrorCodes()), false);
            this.abortWithError(response, payload);
        } else {
            //ALL IS GOOD!
            filterChain.doFilter(request, response);
        }
    }


    private void abortWithError(HttpServletResponse response, MyCustomResponse payload) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println(mapper.writeValueAsString(payload));
        response.getWriter().flush();
    }


}
