package com.smartviet.base.salary.controllers.handler;


import com.smartviet.base.salary.common.Constants;
import com.smartviet.base.salary.common.ResponseMessage;
import com.smartviet.base.salary.configs.ResourceConfig;
import com.smartviet.base.salary.dto.common.ExecutionResult;
import com.smartviet.base.salary.exceptions.AuthException;
import com.smartviet.base.salary.exceptions.LogicException;
import com.smartviet.base.salary.exceptions.PermissionDeniedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LogicException.class)
    public ResponseEntity<ExecutionResult<?>> handleLogicException(LogicException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getKeyMessage(), ex.getDescription(), true);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ExecutionResult<?>> handlePermissionDeniedException(PermissionDeniedException ex) {
        String message = ResourceConfig.getResourceMessage(ResponseMessage.Authentication.PERMISSION_DENIED);
        return buildResponse(HttpStatus.FORBIDDEN, ResponseMessage.Authentication.PERMISSION_DENIED, message,true);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExecutionResult<?>> handleAuthException(AuthException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getKeyMessage(), ex.getDescription(), true);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExecutionResult<?>> handleAuthenticationException(AuthenticationException ex) {
        String message = ResourceConfig.getResourceMessage(ResponseMessage.Authentication.UNAUTHORIZED);
        return buildResponse(HttpStatus.UNAUTHORIZED, ResponseMessage.Authentication.UNAUTHORIZED, message, true);
    }

    @ExceptionHandler({NoResourceFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ExecutionResult<?>> handleNotFoundException(Exception ex) {
        String message = ResourceConfig.getResourceMessage(ResponseMessage.Common.NOT_FOUND);
        return buildResponse(HttpStatus.NOT_FOUND, ResponseMessage.Common.NOT_FOUND, message, true);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExecutionResult<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String msgKey = error.getDefaultMessage();
                    return ResourceConfig.getResourceMessage(msgKey != null ? msgKey : ResponseMessage.Common.INVALID_REQUEST);
                })
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, ResponseMessage.Common.INVALID_REQUEST, message, true);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ExecutionResult<?>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        String message = ex.getAllErrors().stream()
                .map(error -> {
                    String msgKey = error.getDefaultMessage();
                    return ResourceConfig.getResourceMessage(msgKey != null ? msgKey : ResponseMessage.Common.INVALID_REQUEST);
                })
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, ResponseMessage.Common.INVALID_REQUEST, message, true);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExecutionResult<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .map(ResourceConfig::getResourceMessage)
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, ResponseMessage.Common.INVALID_REQUEST, message, true);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExecutionResult<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String message = ResourceConfig.getResourceMessage(ResponseMessage.Common.METHOD_NOT_SUPPORTED);
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, ResponseMessage.Common.METHOD_NOT_SUPPORTED, message, true);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExecutionResult<?>> handleUnhandledException(Throwable ex) {
        log.error("Unhandled exception occurred", ex);
        String message = ResourceConfig.getResourceMessage(ResponseMessage.Common.SYSTEM_ERROR);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseMessage.Common.SYSTEM_ERROR, message, false);
    }

    private ResponseEntity<ExecutionResult<?>> buildResponse(
            HttpStatus status,
            String keyMessage,
            String message,
            boolean isHandled) {
        if (isHandled) {
            log.warn("Handled exception: {}", message);
        }
        ExecutionResult<?> response = new ExecutionResult<>();
        response.setResponseCode(Constants.ExecutionCode.ERROR);
        response.setKeyMessage(keyMessage);
        response.setDescription(message);
        return ResponseEntity.status(status).body(response);
    }

}
