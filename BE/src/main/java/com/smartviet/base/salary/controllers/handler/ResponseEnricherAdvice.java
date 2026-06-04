package com.smartviet.base.salary.controllers.handler;


import com.smartviet.base.salary.dto.common.ExecutionResult;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.sql.Timestamp;

@RestControllerAdvice
public class ResponseEnricherAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        if (body instanceof ResponseEntity<?> respEntity) {
            Object inner = respEntity.getBody();
            if (inner instanceof ExecutionResult<?> er) {
                er.setTimestamp(new Timestamp(System.currentTimeMillis()));
                if (request instanceof ServletServerHttpRequest ssr) {
                    er.setPath(ssr.getServletRequest().getRequestURI());
                }
            }
            return respEntity;
        }
        if (body instanceof ExecutionResult<?> er) {
            er.setTimestamp(new Timestamp(System.currentTimeMillis()));
            if (request instanceof ServletServerHttpRequest ssr) {
                er.setPath(ssr.getServletRequest().getRequestURI());
            }
        }
        return body;
    }

}


