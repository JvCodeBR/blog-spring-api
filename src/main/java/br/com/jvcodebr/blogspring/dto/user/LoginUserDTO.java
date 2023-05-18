package br.com.jvcodebr.blogspring.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginUserDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
