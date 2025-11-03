<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Redirecting...</title>
</head>
<body>
<%
    // Server-side redirect to the login page
    // The browser is immediately told to navigate to login.jsp
    response.sendRedirect("login.jsp");
%>
</body>
</html>
