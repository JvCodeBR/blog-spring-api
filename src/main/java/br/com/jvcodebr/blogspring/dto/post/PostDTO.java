package br.com.jvcodebr.blogspring.dto.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDTO extends PostCreateDTO {
    private Integer id;
    private String userNickname;
    private LocalDateTime postDate;
}
