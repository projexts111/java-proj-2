package com.app.controller;

import com.app.dao.AppDAO;
import com.app.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import org.postgresql.util.PSQLException; // Import to catch specific DB errors

@WebServlet("/RegisterController")
public class RegisterController extends HttpServlet {
    private AppDAO appDAO;

    public void init() throws ServletException {
        // Initialize DAO to ensure DB connection is ready
        appDAO = new AppDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Just forwards to the registration page for display
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); 
        newUser.setRole(role);

        try {
            // Attempt to add the user to the database
            appDAO.addUser(newUser);

            // Set success message and forward back to the registration page
            request.setAttribute("success", "Account created successfully for user '" + username + "'.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            
        } catch (PSQLException e) {
            // Catch the specific PostgreSQL error for unique constraint violation (23505)
            if (e.getSQLState().equals("23505")) {
                request.setAttribute("error", "The username '" + username + "' already exists. Please choose another.");
            } else {
                request.setAttribute("error", "Database error during registration: " + e.getMessage());
            }
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (SQLException e) {
            // Catch other general SQL errors
            request.setAttribute("error", "SQL error during registration: " + e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
