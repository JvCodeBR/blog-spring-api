package br.com.jvcodebr.blogspring.controller;

import br.com.jvcodebr.blogspring.dto.post.PostCreateDTO;
import br.com.jvcodebr.blogspring.dto.post.PostDTO;
import br.com.jvcodebr.blogspring.exception.CustomException;
import br.com.jvcodebr.blogspring.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name = "Post")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDTO> create(@RequestBody PostCreateDTO postCreateDTO) throws CustomException {
        return new ResponseEntity<>(postService.create(postCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> findAll() {
        return new ResponseEntity<>(postService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer postId) throws CustomException {
        postService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
