package com.smartviet.base.salary.dto.requests;

import com.smartviet.base.salary.common.ResponseMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateRequest {
    @NotBlank(message = ResponseMessage.Authentication.MISSING_USERNAME)
    @Size(max = 100, message = ResponseMessage.Authentication.INVALID_USERNAME_OR_PASSWORD)
    String username;
    @NotBlank(message = ResponseMessage.Authentication.MISSING_PASSWORD)
    @Size(max = 100, message = ResponseMessage.Authentication.INVALID_USERNAME_OR_PASSWORD)
    String password;
}
