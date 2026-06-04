package com.smartviet.base.salary.security;


import com.smartviet.base.salary.common.Constants;
import com.smartviet.base.salary.configs.ResourceConfig;
import com.smartviet.base.salary.dto.common.ExecutionResult;
import com.smartviet.base.salary.utils.DataUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

@Component
public class AuthHandler implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ae) throws IOException {
        String errorCode = (String) request.getAttribute(Constants.ExecutionCode.Authentication.KEY);
        errorCode = StringUtils.isBlank(errorCode) ? Constants.ExecutionCode.Authentication.UNAUTHORIZED : errorCode;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        ExecutionResult<Boolean> result = ExecutionResult.<Boolean>builder()
                .responseCode(Constants.ExecutionCode.SUCCESS)
                .data(false)
                .build();
        switch (errorCode) {
            case Constants.ExecutionCode.Authentication.PERMISSION_DENIED:
                result.setKeyMessage(Constants.ExecutionCode.Authentication.PERMISSION_DENIED);
                result.setDescription(ResourceConfig.getResourceMessage("authentication.permission.denied"));
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(Objects.requireNonNull(DataUtils.objectToJson(result)));
                break;
            case Constants.ExecutionCode.Authentication.EXP_TOKEN:
                result.setKeyMessage(Constants.ExecutionCode.Authentication.EXP_TOKEN);
                result.setDescription(ResourceConfig.getResourceMessage("authentication.session.expired"));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(Objects.requireNonNull(DataUtils.objectToJson(result)));
                break;
            default:
                result.setKeyMessage(Constants.ExecutionCode.Authentication.UNAUTHORIZED);
                result.setDescription(ResourceConfig.getResourceMessage("authentication.unauthorized"));
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(Objects.requireNonNull(DataUtils.objectToJson(result)));
                break;
        }
    }

}