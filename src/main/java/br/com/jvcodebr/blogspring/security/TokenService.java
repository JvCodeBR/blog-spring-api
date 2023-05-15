package br.com.jvcodebr.blogspring.security;

import br.com.jvcodebr.blogspring.entity.UserEntity;
import br.com.jvcodebr.blogspring.exception.CustomException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private final String BEARER = "Bearer ";
    private final String ROLES_KEY = "roles";

    public String getToken(Authentication authentication) {

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        Date issuedAt =
                Date.from(LocalDateTime.now()
                        .atZone(ZoneId.systemDefault()).toInstant());

        Date expiration =
                Date.from(LocalDateTime.now()
                        .plusMonths(Long.parseLong(this.expiration))
                .atZone(ZoneId.systemDefault()).toInstant());


        String token = Jwts.builder()
                .claim(Claims.ID, userEntity.getEmail())
                .claim(ROLES_KEY, userEntity.getRoles().stream()
                        .map(roleEntity -> roleEntity.getRole())
                        .toList())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return BEARER + token;
    }

    public UsernamePasswordAuthenticationToken isValid(String token) throws CustomException {
        if (token == null) {
            return null;
        }

        token = token.replace(BEARER, "");

        try {

            Claims keys = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            String email = keys.get(Claims.ID, String.class);
            List<String> roles = keys.get(ROLES_KEY, List.class);

            List<SimpleGrantedAuthority> roleList = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new UsernamePasswordAuthenticationToken(email,
                    null, roleList);

        } catch (ClaimJwtException | MalformedJwtException e) {
            throw new CustomException("Invalid token!", HttpStatus.FORBIDDEN);
        }


    }

    public String getEmailFromToken() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();
    }
}
