package com.example.backend.filters;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Component
@Order(1)
public class ResourcePermissionFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException, java.io.IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getRequestURI().matches("/api/resources/\\S+")) {
            Principal principal = request.getUserPrincipal();

            if (principal == null) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        " You don't have permission to access this resource"
                );
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}