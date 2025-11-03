<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Uploaded Materials</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f7ff; }
        .data-table th { background-color: #3f51b5; color: white; }
        .data-table td, .data-table th { vertical-align: middle; }
        .action-icon { margin: 0 5px; }
        .header-section { padding: 20px 0; }
        @media (max-width: 768px) {
            .table-responsive { border: none !important; }
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid container-lg">
            <a class="navbar-brand" href="MaterialController?command=LIST">
                <i class="fas fa-layer-group me-2"></i>Study Material Sharing Platform
            </a>
            <div class="d-flex">
                <span class="navbar-text me-3 text-white-50 d-none d-md-inline">
                    User: <c:out value="${sessionScope.user.username}" />
                </span>
                <a href="AuthController?command=LOGOUT" class="btn btn-outline-light btn-sm">
                    <i class="fas fa-sign-out-alt me-1"></i> Logout
                </a>
            </div>
        </div>
    </nav>

    <div class="container-lg my-5">
        <div class="header-section d-flex justify-content-between align-items-center mb-4">
            <h1 class="text-primary fs-3">Your Uploaded Study Materials</h1>
            <a href="MaterialController?command=LOAD" class="btn btn-success btn-lg shadow-sm">
                <i class="fas fa-upload me-2"></i> Upload New Material
            </a>
        </div>

        <c:choose>
            <c:when test="${empty MATERIALS_LIST}">
                <div class="alert alert-info text-center p-4" role="alert">
                    <i class="fas fa-info-circle me-2"></i> You have not uploaded any materials yet. Get started now!
                </div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive bg-white shadow-sm rounded-3">
                    <table class="table table-striped table-hover data-table mb-0">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Course</th>
                                <th class="d-none d-md-table-cell">File Name</th>
                                <th class="d-none d-lg-table-cell">Date</th>
                                <th class="text-center">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="material" items="${MATERIALS_LIST}">
                                <tr>
                                    <td><c:out value="${material.id}" /></td>
                                    <td><span class="text-truncate" style="max-width: 150px; display: inline-block;"><c:out value="${material.title}" /></span></td>
                                    <td><span class="badge bg-secondary"><c:out value="${material.course}" /></span></td>
                                    <td class="d-none d-md-table-cell text-truncate" style="max-width: 250px;"><c:out value="${material.fileName}" /></td>
                                    <td class="d-none d-lg-table-cell"><c:out value="${material.uploadDate}" /></td>
                                    <td class="text-center">
                                        <a href="MaterialController?command=DOWNLOAD&materialId=<c:out value='${material.id}' />" 
                                           class="btn btn-sm btn-info text-white action-icon" 
                                           title="Download">
                                            <i class="fas fa-download"></i>
                                        </a> 
                                        <a href="MaterialController?command=DELETE&materialId=<c:out value='${material.id}' />" 
                                           onclick="return confirm('WARNING: Are you sure you want to permanently delete this material?');"
                                           class="btn btn-sm btn-danger action-icon"
                                           title="Delete">
                                            <i class="fas fa-trash-alt"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>

