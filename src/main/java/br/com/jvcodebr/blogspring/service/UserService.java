package br.com.jvcodebr.blogspring.service;

import br.com.jvcodebr.blogspring.dto.user.LoginUserDTO;
import br.com.jvcodebr.blogspring.dto.user.UserCreateDTO;
import br.com.jvcodebr.blogspring.dto.user.UserDTO;
import br.com.jvcodebr.blogspring.entity.RoleEntity;
import br.com.jvcodebr.blogspring.entity.UserEntity;
import br.com.jvcodebr.blogspring.exception.CustomException;
import br.com.jvcodebr.blogspring.repository.RoleRepository;
import br.com.jvcodebr.blogspring.repository.UserRepository;
import br.com.jvcodebr.blogspring.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public UserEntity findByEmail(String email) throws CustomException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    public UserEntity findByNickname(String nickname) throws CustomException {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    public String auth(LoginUserDTO login) throws CustomException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getPassword()
                );
        try {
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            return tokenService.getToken(authentication);
        } catch (BadCredentialsException e) {
            throw new CustomException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public UserDTO register(UserCreateDTO userCreateDTO) throws CustomException {
        String email = userCreateDTO.getEmail();
        String nickname = userCreateDTO.getNickname();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException("Email already exists", HttpStatus.CONFLICT);
        }
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new CustomException("Nickname already exists", HttpStatus.CONFLICT);
        }

        UserEntity userEntity = objectMapper.convertValue(userCreateDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        userEntity.setActive(true);
        userEntity.setRoles(Set.of(roleRepository.findById(2).get()));

        return entityToDTO(userRepository.save(userEntity));
    }

    public UserEntity getUserFromToken() throws CustomException {
        String email = tokenService.getEmailFromToken();
        return this.findByEmail(email);
    }

    public UserDTO getUserDtoFromToken() throws CustomException {
        String email = tokenService.getEmailFromToken();
        return entityToDTO(this.findByEmail(email));
    }

    private UserDTO entityToDTO(UserEntity userEntity) {
        UserDTO userDTO = objectMapper.convertValue(userEntity, UserDTO.class);
        userDTO.setRoles(userEntity.getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toSet()));

        return userDTO;
    }

}
