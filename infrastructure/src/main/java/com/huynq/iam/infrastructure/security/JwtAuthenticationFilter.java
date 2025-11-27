package com.huynq.iam.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtParser jwtParser;

    public JwtAuthenticationFilter(SecretKey secretKey) {
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7).trim();
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            Authentication authentication = buildAuthentication(claims, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private Authentication buildAuthentication(Claims claims, HttpServletRequest request) {
        Long userId = claims.get("uid", Long.class);
        String externalId = claims.get("externalId", String.class);
        String sessionId = claims.get("sid", String.class);
        List<String> roles = claims.get("roles", List.class);
        Set<String> scopes = parseScopes(claims.get("scope", String.class));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (roles != null) {
            roles.stream()
                    .filter(role -> role != null && !role.isBlank())
                    .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));
        }
        scopes.forEach(scope -> authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope)));

        AuthenticatedUser principal = new AuthenticatedUser(userId, externalId, sessionId, scopes);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    private Set<String> parseScopes(String scopeClaim) {
        if (scopeClaim == null || scopeClaim.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(scopeClaim.split("\\s+"))
                .filter(scope -> scope != null && !scope.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }
}

