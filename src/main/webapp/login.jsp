<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Login - Material Platform</title>
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
        .login-card {
            width: 100%;
            max-width: 400px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border-radius: 12px;
        }
        .form-floating label {
            color: #6c757d;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-4">
                <div class="card login-card">
                    <div class="card-header bg-primary text-white text-center rounded-top-2">
                        <h4 class="mb-0">
                            <i class="fas fa-book-reader me-2"></i>Staff Login
                        </h4>
                    </div>
                    <div class="card-body p-4">
                        <p class="text-muted text-center mb-4">Access the Study Material Portal</p>
                        
                        <% 
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                                out.println("<div class='alert alert-danger' role='alert'><strong>Error:</strong> " + error + "</div>");
                            }
                        %>

                        <form action="AuthController" method="POST">
                            <input type="hidden" name="command" value="LOGIN" />
                            
                            <div class="form-floating mb-3">
                                <input type="text" class="form-control" id="username" name="username" placeholder="Username" required>
                                <label for="username">Username</label>
                            </div>
                            
                            <div class="form-floating mb-4">
                                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                                <label for="password">Password</label>
                            </div>
                            
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-lg">Sign In</button>
                            </div>
                        </form>
                    </div>
                    <div class="card-footer text-center text-muted bg-light rounded-bottom-2">
                        <small>Don't have an account? <a href="register.jsp" class="text-primary fw-bold">Sign In Here</a></small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
