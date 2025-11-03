<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Upload New Material</title>
</head>
<body>
    <h1>Upload New Study Material</h1>
    <p><a href="MaterialController?command=LIST">Back to List</a></p>

    <form action="MaterialController" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="command" value="UPLOAD" />

        <label for="title">Title (e.g., Chapter 5 Notes):</label><br>
        <input type="text" id="title" name="title" required><br><br>
        
        <label for="course">Course/Category:</label><br>
        <input type="text" id="course" name="course" required><br><br>
        
        <label for="description">Description:</label><br>
        <textarea id="description" name="description"></textarea><br><br>

        <label for="file">Select File:</label><br>
        <input type="file" id="file" name="file" required><br><br>
        
        <input type="submit" value="Upload Material">
    </form>
</body>
</html>