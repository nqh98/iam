package com.huynq.iam.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Lightweight authorization filter that validates whether the authenticated user
 * has the OAuth scope required for the requested endpoint.
 */
@Component
public class ScopeAuthorizationFilter extends OncePerRequestFilter {

    private final Map<RequestMatcher, String> requiredScopes;

    public ScopeAuthorizationFilter() {
        this.requiredScopes = Map.of(
                new AntPathRequestMatcher("/api/v1/users/**", HttpMethod.GET.name()), "iam:user:read",
                new AntPathRequestMatcher("/api/v1/users/**", HttpMethod.POST.name()), "iam:user:write",
                new AntPathRequestMatcher("/api/v1/users/**", HttpMethod.PUT.name()), "iam:user:write",
                new AntPathRequestMatcher("/api/v1/users/**", HttpMethod.DELETE.name()), "iam:user:write",
                new AntPathRequestMatcher("/api/v1/roles/**", HttpMethod.GET.name()), "iam:role:read",
                new AntPathRequestMatcher("/api/v1/roles/**", HttpMethod.POST.name()), "iam:role:write",
                new AntPathRequestMatcher("/api/v1/roles/**", HttpMethod.PUT.name()), "iam:role:write",
                new AntPathRequestMatcher("/api/v1/roles/**", HttpMethod.DELETE.name()), "iam:role:write"
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Optional<String> requiredScope = resolveRequiredScope(request);
        if (requiredScope.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing Bearer token");
            return;
        }

        if (!hasScope(authentication, requiredScope.get())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient scope");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveRequiredScope(HttpServletRequest request) {
        return requiredScopes.entrySet().stream()
                .filter(entry -> entry.getKey().matches(request))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private boolean hasScope(Authentication authentication, String requiredScope) {
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toSet());
        return authorities.contains("SCOPE_" + requiredScope);
    }
}

