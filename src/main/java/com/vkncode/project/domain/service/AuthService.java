package com.vkncode.project.domain.service;

import com.vkncode.project.domain.entity.User;
import com.vkncode.project.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${jwt.expiration.local}")
    private String expiration;

    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateJwt(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date();
        Date tokenExpiration = new Date(now.getTime() + Long.parseLong(expiration));

        String stringJwt = Jwts.builder()
                .setIssuer("app-seguranca.mesttra.com")
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(tokenExpiration)
                .signWith(KEY)
                .compact();

        this.isTokenValid(stringJwt);

        return stringJwt;

    }

    public boolean isTokenValid (String tokenWithoutBearer) {

        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(KEY).build();
            jwtParser.parseClaimsJws(tokenWithoutBearer);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Long getSub (String tokenWithoutBearer) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(KEY).build();
        Claims payload = jwtParser.parseClaimsJws(tokenWithoutBearer).getBody();

        String subject = payload.getSubject();
        return Long.parseLong(subject);
    }
}
