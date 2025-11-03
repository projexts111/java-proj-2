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
    
    // FINAL CORRECT JSP PATH
    private static final String LOGIN_JSP = "/WEB-INF/views/login.jsp";

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
                    // Correct forward path for the views folder
                    request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
                    break;
            }
        } catch (SQLException e) {
            // Handle DB exceptions gracefully
            request.setAttribute("error", "A database error occurred: " + e.getMessage());
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = request.getParameter("command");

        if ("LOGOUT".equals(command)) {
            logoutUser(request, response);
        } else {
            // Correct forward path for the views folder
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
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
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
        }
    }

    private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Redirect back to AuthController which handles the forward to login.jsp
        response.sendRedirect("AuthController");
    }
}
