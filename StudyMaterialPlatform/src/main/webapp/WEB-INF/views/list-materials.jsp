<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>My Uploaded Materials</title>
</head>
<body>
    <h2>Welcome, <c:out value="${sessionScope.user.username}" />!</h2>
    
    <p>
        <a href="MaterialController?command=LOAD">Upload New Material</a> |
        <a href="AuthController?command=LOGOUT">Logout</a>
    </p>

    <h3>Your Uploaded Materials (Data Isolation Enforced)</h3>

    <c:choose>
        <c:when test="${empty MATERIALS_LIST}">
            <p>You have not uploaded any materials yet.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="5">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Course</th>
                        <th>File Name</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="material" items="${MATERIALS_LIST}">
                        <tr>
                            <td><c:out value="${material.title}" /></td>
                            <td><c:out value="${material.course}" /></td>
                            <td><c:out value="${material.fileName}" /></td>
                            <td><c:out value="${material.uploadDate}" /></td>
                            <td>
                                <a href="MaterialController?command=DOWNLOAD&materialId=<c:out value='${material.id}' />">Download</a> | 
                                <a href="MaterialController?command=DELETE&materialId=<c:out value='${material.id}' />" 
                                   onclick="return confirm('Are you sure you want to delete this material?');">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>