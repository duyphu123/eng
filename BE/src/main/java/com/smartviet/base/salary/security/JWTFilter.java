package com.smartviet.base.salary.security;

import com.smartviet.base.salary.common.Constants;
import com.smartviet.base.salary.dto.UserInfoResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String clientIp = getClientIp(request);
        String origin = request.getHeader("Origin");
        log.info("API called: [{}] {} | IP: {} | Origin: {}", method, uri, clientIp, origin);
        String internalKey = request.getHeader("x-internal-api-key");
        String userId = request.getHeader("x-user-id");
        String username = request.getHeader("x-user-name");
        String fullName = request.getHeader("x-user-full-name");
        String staffCode = request.getHeader("x-user-staff-code");
        String rolesHeader = request.getHeader("x-user-roles");
//        if (StringUtils.isBlank(internalKey) || !internalKey.equals(internalApiKey)) {
//            log.warn("Invalid or missing internal API key from gateway");
//            setErrorCode(request, Constants.ExecutionCode.Authentication.UNAUTHORIZED);
//            chain.doFilter(request, response);
//            return;
//        }
        if (StringUtils.isBlank(username) || StringUtils.isBlank(rolesHeader)) {
            log.warn("Missing user information in gateway headers");
            setErrorCode(request, Constants.ExecutionCode.Authentication.UNAUTHORIZED);
            chain.doFilter(request, response);
            return;
        }
        List<String> roles = Arrays.stream(rolesHeader.replaceAll("[\\[\\] ]", "").split(","))
                .filter(s -> !s.isEmpty()).toList();
        if (roles.isEmpty()) {
            log.warn("No valid roles found for user: {}", username);
            setErrorCode(request, Constants.ExecutionCode.Authentication.PERMISSION_DENIED);
            chain.doFilter(request, response);
            return;
        }
        boolean hasRole = roles.stream()
                .map(String::trim)
                .anyMatch(Constants.Security.Role.toList::contains);
//        if (!hasRole) {
//            log.warn("User {} does not have valid role", username);
//            setErrorCode(request, Constants.ExecutionCode.Authentication.PERMISSION_DENIED);
//            chain.doFilter(request, response);
//            return;
//        }
        request.setAttribute(Constants.Security.Claims.USER_ID, userId);
        request.setAttribute(Constants.Security.Claims.USERNAME, username);
        request.setAttribute(Constants.Security.Claims.FULL_NAME, fullName);
        request.setAttribute(Constants.Security.Claims.ROLES, rolesHeader);
        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.trim()))
                    .collect(Collectors.toList());
            UserInfoResponse principal = UserInfoResponse.builder()
                    .userId(userId)
                    .username(username)
                    .fullName(fullName)
                    .staffCode(staffCode)
                    .roles(roles)
                    .build();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        chain.doFilter(request, response);
    }

    private void setErrorCode(HttpServletRequest request, String code) {
        request.setAttribute(Constants.ExecutionCode.Authentication.KEY, code);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0];
    }

}