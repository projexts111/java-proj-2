<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Upload New Material</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <style>
        body { background-color: #f4f7ff; }
        .upload-card { max-width: 700px; margin-top: 50px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); }
    </style>
</head>
<body>
    <div class="container">
        <div class="card upload-card mx-auto rounded-3">
            <div class="card-header bg-primary text-white">
                <h3 class="mb-0">
                    <i class="fas fa-file-upload me-2"></i>Upload New Study Material
                </h3>
            </div>
            <div class="card-body p-4">
                <div class="mb-4">
                    <a href="MaterialController?command=LIST" class="btn btn-outline-secondary btn-sm">
                        <i class="fas fa-arrow-left me-1"></i> Back to Dashboard
                    </a>
                </div>

                <form action="MaterialController" method="POST" enctype="multipart/form-data">
                    <input type="hidden" name="command" value="UPLOAD" />

                    <div class="row g-3 mb-3">
                        <div class="col-md-7">
                            <label for="title" class="form-label fw-bold">Title</label>
                            <input type="text" class="form-control" id="title" name="title" placeholder="e.g., Chapter 3 Midterm Review" required>
                        </div>
                        
                        <div class="col-md-5">
                            <label for="course" class="form-label fw-bold">Course/Category</label>
                            <input type="text" class="form-control" id="course" name="course" placeholder="e.g., Java MVC, Data Structures" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label fw-bold">Description (Optional)</label>
                        <textarea class="form-control" id="description" name="description" rows="3" placeholder="Brief summary of the material content..."></textarea>
                    </div>

                    <div class="mb-4">
                        <label for="file" class="form-label fw-bold">Select File (Max 10MB)</label>
                        <input class="form-control" type="file" id="file" name="file" required>
                        <div class="form-text">Supported formats: PDF, DOCX, TXT, etc.</div>
                    </div>
                    
                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary btn-lg shadow-sm">
                            <i class="fas fa-paper-plane me-2"></i> Upload Material
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
