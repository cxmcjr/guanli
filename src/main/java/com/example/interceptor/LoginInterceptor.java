package com.example.interceptor;

import com.example.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        if (path.startsWith("/login") || path.startsWith("/register")
                || path.startsWith("/css/") || path.startsWith("/js/")
                || path.startsWith("/images/") || path.startsWith("/error")) {
            return true;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("/login");
            return false;
        }

        String role = user.getRole();

        if (path.startsWith("/admin/") && !"admin".equals(role)) {
            response.sendRedirect("/");
            return false;
        }

        if (path.startsWith("/competition/review") || path.startsWith("/innovation/review")
                || path.startsWith("/software/review") || path.startsWith("/paper/review")) {
            if (!"secretary".equals(role) && !"leader".equals(role) && !"admin".equals(role)) {
                response.sendRedirect("/");
                return false;
            }
        }

        if (path.startsWith("/competition/finalReview") || path.startsWith("/innovation/finalReview")
                || path.startsWith("/software/finalReview") || path.startsWith("/paper/finalReview")) {
            if (!"leader".equals(role) && !"admin".equals(role)) {
                response.sendRedirect("/");
                return false;
            }
        }

        return true;
    }
}
