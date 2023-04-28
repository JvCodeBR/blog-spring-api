package br.com.jvcodebr.blogspring.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginUserDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
