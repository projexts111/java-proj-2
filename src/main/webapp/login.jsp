<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Study Material Platform</title>
</head>
<body>
    <h1>Staff Login</h1>

    <% 
        // Display error message if present
        String error = (String) request.getAttribute("error");
        if (error != null) {
            out.println("<p style='color: red;'><strong>Error:</strong> " + error + "</p>");
        }
    %>

    <form action="AuthController" method="POST">
        <input type="hidden" name="command" value="LOGIN" />
        
        <label for="username">Username:</label><br>
        <input type="text" id="username" name="username" required><br><br>
        
        <label for="password">Password:</label><br>
        <input type="password" id="password" name="password" required><br><br>
        
        <input type="submit" value="Sign In">
    </form>
    
    <p>Use default credentials: **Username: staff, Password: password**</p>
</body>
</html>
