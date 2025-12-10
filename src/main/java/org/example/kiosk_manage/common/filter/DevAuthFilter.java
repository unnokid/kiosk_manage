package org.example.kiosk_manage.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DevAuthFilter extends OncePerRequestFilter {

    @Value("${dev-auth.password}")
    private String devPassword;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        // 인증 예외 경로
        if (path.startsWith("/api/v1/admin/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        String pass = request.getHeader("X-DEV-PASS");

        if (!devPassword.equals(pass)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
}