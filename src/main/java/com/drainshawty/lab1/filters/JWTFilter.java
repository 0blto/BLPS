package com.drainshawty.lab1.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.filter.GenericFilterBean;
import com.drainshawty.lab1.security.JWTUtil;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import java.util.Optional;


public class JWTFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        val token = jwtUtil.resolveToken((HttpServletRequest) request);
        if (token != null && jwtUtil.validateToken(token))
            Optional.ofNullable(jwtUtil.getAuthentication(token)).ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth));
        chain.doFilter(request, response);
    }
}
