package com.app.controller;

import com.app.dao.AppDAO;
import com.app.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AuthController")
public class AuthController extends HttpServlet {
    private AppDAO appDAO;

    public void init() throws ServletException {
        appDAO = new AppDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if (command == null) {
            command = "LOGIN";
        }
        
        switch (command) {
            case "LOGIN":
                loginUser(request, response);
                break;
            case "LOGOUT":
                logoutUser(request, response);
                break;
            default:
                response.sendRedirect("login.jsp");
                break;
        }
    }
    
    // Simple redirect for GET requests to main page
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if ("LOGOUT".equals(command)) {
            logoutUser(request, response);
        } else {
            response.sendRedirect("MaterialController?command=LIST");
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            User user = appDAO.validateUser(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                // Redirect to the main listing page after successful login
                response.sendRedirect("MaterialController?command=LIST");
            } else {
                request.setAttribute("error", "Invalid username or password.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error during login. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
}