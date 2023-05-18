package br.com.jvcodebr.blogspring.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class PostCreateDTO {
    @NotBlank
    private String content;
}
