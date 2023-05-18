package br.com.jvcodebr.blogspring.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateDTO {
    @NotBlank
    private String nickname;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
