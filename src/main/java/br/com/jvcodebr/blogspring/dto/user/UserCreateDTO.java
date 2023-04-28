package br.com.jvcodebr.blogspring.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserCreateDTO {
    @NotBlank
    private String nickname;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
