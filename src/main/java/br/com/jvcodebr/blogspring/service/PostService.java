package br.com.jvcodebr.blogspring.service;

import br.com.jvcodebr.blogspring.dto.post.PostCreateDTO;
import br.com.jvcodebr.blogspring.dto.post.PostDTO;
import br.com.jvcodebr.blogspring.entity.PostEntity;
import br.com.jvcodebr.blogspring.entity.UserEntity;
import br.com.jvcodebr.blogspring.exception.CustomException;
import br.com.jvcodebr.blogspring.repository.PostRepository;
import br.com.jvcodebr.blogspring.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    public PostDTO create(PostCreateDTO postCreateDTO) throws CustomException {
        PostEntity postEntity = objectMapper.convertValue(postCreateDTO, PostEntity.class);
        UserEntity userEntity = userService.getUserFromToken();
        postEntity.setUser(userEntity);
        postEntity.setPostDate(LocalDateTime.now());
        return convertEntityToDTO(postRepository.save(postEntity));
    }

    public List<PostDTO> findAll() {
        return postRepository.findAllByOrderByPostDateDesc().stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    public List<PostDTO> findAllByUser(String nickname) throws CustomException {
        UserEntity user = userService.findByNickname(nickname);
        return postRepository.findAllByUserIdOrderByPostDateDesc(user.getId()).stream()
                .map(this::convertEntityToDTO)
                .toList();
    }

    public void delete(Integer postId) throws CustomException {
        String emailFromToken = tokenService.getEmailFromToken();

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException("Post not found!", HttpStatus.NOT_FOUND));

        if (!postEntity.getUser().getEmail().equals(emailFromToken)) {
            throw new CustomException("Not authorized!", HttpStatus.FORBIDDEN);
        }

        postRepository.deleteById(postEntity.getId());
    }

    private PostDTO convertEntityToDTO(PostEntity postEntity) {
        PostDTO postDTO = objectMapper.convertValue(postEntity, PostDTO.class);
        postDTO.setUserNickname(postEntity.getUser().getNickname());
        return postDTO;
    }
}
