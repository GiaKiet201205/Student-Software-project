package com.sgu.admission.config;

import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        
        // Skip login page, static files, authenticate endpoint
        if (requestUri.equals("/login") || 
            requestUri.equals("/") ||
            requestUri.equals("/authenticate") ||
            requestUri.startsWith("/static/") ||
            requestUri.startsWith("/css/") ||
            requestUri.startsWith("/js/")) {
            return true;
        }
        
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("cccd") != null) {
            return true;
        }
        
        // Redirect to login
        response.sendRedirect("/login");
        return false;
    }
}
