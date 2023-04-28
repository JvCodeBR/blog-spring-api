package br.com.jvcodebr.blogspring.dto.post;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostCreateDTO {
    @NotBlank
    private String content;
}
