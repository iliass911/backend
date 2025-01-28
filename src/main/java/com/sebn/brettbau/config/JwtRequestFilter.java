package com.sebn.brettbau.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        
        final String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        // Detailed logging
        log.info("Processing request: {} {}", request.getMethod(), request.getRequestURI());
        log.info("Authorization Header: {}", requestTokenHeader);

        // Check if header starts with Bearer
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                log.info("Username extracted: {}", username);
            } catch (Exception e) {
                log.error("Unable to parse JWT token", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
                return;
            }
        }

        // Validate & set context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                if (jwtTokenUtil.validateToken(jwtToken)) {
                    String role = jwtTokenUtil.getRoleFromToken(jwtToken);
                    log.info("Role extracted: {}", role);

                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Authentication set successfully");
                } else {
                    log.warn("Token validation failed");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Validation Failed");
                    return;
                }
            } catch (Exception e) {
                log.error("Authentication process failed", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Exclude authentication and public endpoints
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || 
               request.getMethod().equals("OPTIONS");
    }
}