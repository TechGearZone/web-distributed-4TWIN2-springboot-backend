package com.esprit.ms.user.filter;

import com.esprit.ms.user.security.JwtAuthenticationToken;
import com.esprit.ms.user.security.JwtTokenProvider;
import com.esprit.ms.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, java.io.IOException {

        logger.debug("Processing request for: {}", request.getRequestURI());

        try {
            String token = extractToken(request);

            if (token != null) {
                if (!jwtTokenProvider.validateToken(token)) {
                    sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                String email = jwtTokenProvider.getUsernameFromToken(token);
                UserDetails userDetails = userService.loadUserByUsername(email);

                var authentication = new JwtAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Authentication error", ex);
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        logger.debug("No JWT token found in request");
        return null;
    }

    private void sendError(HttpServletResponse response, int code, String message) {
        try {
            response.sendError(code, message);
        } catch (java.io.IOException e) {
            logger.error("Failed to send error response", e);
            throw new RuntimeException("Failed to send error response", e);
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        logger.debug("Checking shouldNotFilter for path: {}", path); // Add this line
        return path.startsWith("/api/v1/auth/");
    }
}