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
        // Initialize DAO to establish DB connection on startup
        appDAO = new AppDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");
        if (command == null) {
            command = "LOGIN";
        }
        
        try {
            switch (command) {
                case "LOGIN":
                    loginUser(request, response);
                    break;
                case "LOGOUT":
                    logoutUser(request, response);
                    break;
                default:
                    // FIX: Forward to login page with leading slash
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            // Handle DB exceptions gracefully
            request.setAttribute("error", "A database error occurred: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");

        if ("LOGOUT".equals(command)) {
            logoutUser(request, response);
        } else {
            // FIX: Use leading slash '/' to ensure the path to login.jsp starts 
            // from the web application root (Context Root). This solves the 404 error.
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        User user = appDAO.validateUser(username, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            // Redirect to the Material List Controller after successful login
            response.sendRedirect("MaterialController?command=LIST");
        } else {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
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
