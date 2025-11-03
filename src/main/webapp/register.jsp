<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Registration - Material Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .register-card {
            width: 100%;
            max-width: 500px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border-radius: 12px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card register-card">
                    <div class="card-header bg-success text-white text-center rounded-top-2">
                        <h4 class="mb-0">
                            <i class="fas fa-user-plus me-2"></i>New Staff Registration
                        </h4>
                    </div>
                    <div class="card-body p-4">
                        <p class="text-muted text-center mb-4">Create your account to access the dashboard.</p>
                        
                        <% 
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                                out.println("<div class='alert alert-danger' role='alert'><strong>Error:</strong> " + error + "</div>");
                            }
                            String success = (String) request.getAttribute("success");
                            if (success != null) {
                                out.println("<div class='alert alert-success' role='alert'><strong>Success!</strong> " + success + " <a href='login.jsp'>Go to Login</a></div>");
                            }
                        %>

                        <form action="RegisterController" method="POST">
                            <div class="row g-3">
                                <div class="col-12">
                                    <label for="username" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="username" name="username" required>
                                </div>
                                
                                <div class="col-md-6">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                </div>
                                
                                <div class="col-md-6">
                                    <label for="department" class="form-label">Department/Role</label>
                                    <select class="form-select" id="department" name="role" required>
                                        <option value="STAFF" selected>Staff User</option>
                                        <option value="ADMIN">Administrator</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="d-grid mt-4">
                                <button type="submit" class="btn btn-success btn-lg">
                                    <i class="fas fa-check-circle me-1"></i> Register Account
                                </button>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer text-center text-muted bg-light rounded-bottom-2">
                        <small>Already registered? <a href="login.jsp">Go to Login</a></small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
