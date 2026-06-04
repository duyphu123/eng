package com.smartviet.base.salary.exceptions;


import com.smartviet.base.salary.common.Constants;
import com.smartviet.base.salary.configs.ResourceConfig;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {

    private final String description;
    private final String keyMessage;

    public AuthException(String keyMessage) {
        super(ResourceConfig.getResourceMessage(Constants.Language.EN, keyMessage));
        this.keyMessage = keyMessage;
        this.description = ResourceConfig.getResourceMessage(keyMessage);
    }

    public AuthException(String keyMessage, Object... params) {
        super(String.format(ResourceConfig.getResourceMessage(Constants.Language.EN, keyMessage), params));
        this.keyMessage = keyMessage;
        this.description = String.format(ResourceConfig.getResourceMessage(keyMessage), params);
    }

    @Override
    public String getMessage() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
