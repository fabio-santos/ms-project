package com.vkncode.project.config;

import com.vkncode.project.domain.entity.User;
import com.vkncode.project.domain.repository.UserRepository;
import com.vkncode.project.domain.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final AuthService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal (HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String tokenWithoutBearer = getTokenWithoutBearer(request);

        boolean isValid = tokenService.isTokenValid(tokenWithoutBearer);

        if (isValid) {
            doAuthUser(tokenWithoutBearer);
        }

        filterChain.doFilter(request, response);
    }

    private void doAuthUser (String tokenWithoutBearer) {
        Long userId = this.tokenService.getSub(tokenWithoutBearer);
        User user = this.userRepository.findById(userId).orElseThrow(RuntimeException::new);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenWithoutBearer (HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7);
    }
}
