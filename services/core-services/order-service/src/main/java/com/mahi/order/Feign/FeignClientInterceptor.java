package com.mahi.order.Feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader(AUTHORIZATION_HEADER);
            if (authHeader != null && !requestTemplate.headers().containsKey(AUTHORIZATION_HEADER)) {
                requestTemplate.header(AUTHORIZATION_HEADER, authHeader);
            }
            
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if(correlationId != null && !requestTemplate.headers().containsKey(CORRELATION_ID_HEADER)) {
                requestTemplate.header(CORRELATION_ID_HEADER, correlationId);
            }
        }
    }
}
