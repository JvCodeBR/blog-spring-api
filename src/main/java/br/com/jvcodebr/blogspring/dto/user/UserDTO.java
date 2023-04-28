package br.com.jvcodebr.blogspring.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Integer id;
    private String nickname;
    private String email;
    private Boolean active;
    private Set<String> roles;

}
