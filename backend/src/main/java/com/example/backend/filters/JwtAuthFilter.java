package com.example.backend.filters;

import com.example.backend.services.users.JwtService;
import com.example.backend.services.users.JwtUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtService jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException, java.io.IOException {
        System.out.println("request");
        System.out.println(request);
        response.setHeader("request-url", request.getRequestURI());
        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        Integer userId = null;
        String jwtToken = null;
        Claims claims = null;
        System.out.println("Hello Filter");
        if (requestTokenHeader != null) {
            System.out.println("requestTokenHeader != null");
            if (requestTokenHeader.startsWith("Bearer")) {
                System.out.println("requestTokenHeader.startsWith(\"Bearer\")");
                jwtToken = requestTokenHeader.substring(7);
                try {
                    jwtUtils.verifyToken(jwtToken);
                } catch (SignatureException e) {
//                    throw new ResponseStatusException(
//                            HttpStatus.UNAUTHORIZED,
//                            "Invalid JWT signature"
//                    );
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT signature");
                    return;
                }
                claims = jwtUtils.extractClaims(jwtToken);
                System.out.println("claims " + claims);
                System.out.println(claims);
                if (jwtUtils.isExpired(claims)) {
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "JWT token has expired"
                    );
                }
                if (!jwtUtils.isValidClaims(claims) || !"ACCESS_TOKEN".equals(claims.get("typ" , String.class))) {

                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid JWT access token"
                    );
                }
                    userId = claims.get("id", Integer.class);
                } else {
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "JWT Token does not begin with Bearer String"
                    );
                }
            }
            System.out.println("check Authentication");
            Authentication authentication = SecurityContextHolder
                    .getContext().getAuthentication();
            System.out.println("authentication = " + authentication);
            System.out.println("userId = " + userId);
            if (userId != null && authentication == null) {
                UserDetails userDetails = this.jwtUserDetailsService.loadUserById(userId);
                System.out.println("userDetails = " + userDetails);
                if (userDetails == null || !userDetails.getUsername().equals(claims.get("nickname",String.class))) {
                    throw new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid JWT Token"
                    );
                }
                System.out.println("set UsernamePasswordAuthenticationToken");
                UsernamePasswordAuthenticationToken upAuthToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                upAuthToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(upAuthToken);
                authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Authentication: " + authentication);

//                String path = request.getRequestURI(); // /itb-mshop/v2/sellers/4/sale-items
//                String[] parts = path.split("/");
//                if (parts.length > 4) {
//                    try {
//                        Integer sellerIdInPath = Integer.valueOf(parts[4]);
//                        if (!sellerIdInPath.equals(userId)) {
//                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden: cannot access other seller's items");
//                            return;
//                        }
//                    } catch (NumberFormatException ignored) {
//                        // ไม่ใช่เลข → ปล่อยผ่าน
//                    }
//                }
//            } else {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token does not begin with Bearer String");
//                return;
//            }

            chain.doFilter(request, response);
        }
    }

