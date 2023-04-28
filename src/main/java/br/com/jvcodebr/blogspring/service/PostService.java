package br.com.jvcodebr.blogspring.service;

import br.com.jvcodebr.blogspring.dto.post.PostCreateDTO;
import br.com.jvcodebr.blogspring.dto.post.PostDTO;
import br.com.jvcodebr.blogspring.entity.PostEntity;
import br.com.jvcodebr.blogspring.entity.UserEntity;
import br.com.jvcodebr.blogspring.exception.CustomException;
import br.com.jvcodebr.blogspring.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public PostDTO create(PostCreateDTO postCreateDTO) throws CustomException {
        PostEntity postEntity = objectMapper.convertValue(postCreateDTO, PostEntity.class);
        UserEntity userEntity = userService.getUserFromToken();
        postEntity.setUser(userEntity);
        postEntity.setPostDate(LocalDateTime.now());
        return convertEntityToDTO(postRepository.save(postEntity));
    }

    public List<PostDTO> findAll() {
        return postRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    private PostDTO convertEntityToDTO(PostEntity postEntity) {
        PostDTO postDTO = objectMapper.convertValue(postEntity, PostDTO.class);
        postDTO.setUserNickname(postEntity.getUser().getNickname());
        return postDTO;
    }
}
