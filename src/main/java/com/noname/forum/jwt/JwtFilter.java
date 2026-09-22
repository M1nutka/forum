package com.noname.forum.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.noname.forum.users.User;
import com.noname.forum.users.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component 
@RequiredArgsConstructor 
public class JwtFilter extends  OncePerRequestFilter{
    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateAccessToken(token)){
            final Claims claims = jwtProvider.getAccessClaims(token);
            String username = claims.getSubject();
            Optional<User> user = userService.getByUsername(username);
            var authentication = new UsernamePasswordAuthenticationToken(
                user.get(),
                null,
                user.get().getAuthorities()
            );
            authentication.setDetails(new  WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")){
            return  bearer.substring(7);
        }
        return null;
    } 
}