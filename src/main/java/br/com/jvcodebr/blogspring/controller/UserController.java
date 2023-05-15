package br.com.jvcodebr.blogspring.controller;

import br.com.jvcodebr.blogspring.dto.user.LoginUserDTO;
import br.com.jvcodebr.blogspring.dto.user.UserCreateDTO;
import br.com.jvcodebr.blogspring.dto.user.UserDTO;
import br.com.jvcodebr.blogspring.entity.UserEntity;
import br.com.jvcodebr.blogspring.exception.CustomException;
import br.com.jvcodebr.blogspring.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody LoginUserDTO loginUserDTO) throws CustomException {
        return ResponseEntity.ok(userService.auth(loginUserDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserCreateDTO userCreateDTO) throws CustomException {
        return new ResponseEntity<>(userService.register(userCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping("/logged-user")
    public ResponseEntity<UserDTO> getLoggedUser() throws CustomException {
        return new ResponseEntity<>(userService.getUserDtoFromToken(), HttpStatus.OK);
    }
}
