package com.app.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/MaterialController", "/views/*", "/index.jsp"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        String loginURI = req.getContextPath() + "/AuthController";
        String loginPageURI = req.getContextPath() + "/login.jsp";

        boolean isLoginRequest = req.getRequestURI().equals(loginURI) || req.getRequestURI().equals(loginPageURI);

        if (loggedIn || isLoginRequest) {
            chain.doFilter(request, response);
        } else {
            // User is not logged in, redirect to login page
            res.sendRedirect(loginPageURI);
        }
    }
}