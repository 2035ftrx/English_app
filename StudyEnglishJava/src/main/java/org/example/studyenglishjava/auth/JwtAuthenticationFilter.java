package org.example.studyenglishjava.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.studyenglishjava.config.GlobalExceptionHandler;
import org.example.studyenglishjava.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtUtils, UserService userService   ) {
        this.jwtService = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");

            String prefix = "Bearer ";
            if (authorization == null || !authorization.startsWith(prefix)) {
//                 filterChain.doFilter(request, response);
//                 return;
                throw new UnauthorizedException("Unauthorized");
            }

            String jwtToken = authorization.substring(prefix.length());
            Optional<Claims> claimsOptional = jwtService.getAllClaimsFromToken(jwtToken);

            if (claimsOptional.isEmpty() || !jwtService.isJwtValid(claimsOptional.get())) {
                throw new UnauthorizedException("Unauthorized");
            }

            Claims claims = claimsOptional.get();
            String username = claims.getSubject();
            UserPrincipal user = userService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            GlobalExceptionHandler.sendJsonErrorResponse(
                    response,
                    "Unauthorized or token expired.",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }
}