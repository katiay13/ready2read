<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session.getAttribute("username") != null) {
        response.sendRedirect(request.getContextPath() + "/catalog");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Ready 2 Read</title>
    <style>
        body {
            font-family: Georgia, serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f9f5f0;
        }
        .container {
            text-align: center;
        }
        h1 {
            font-size: 3rem;
            margin-bottom: 0.25rem;
            color: #2c2c2c;
        }
        p {
            font-size: 1.1rem;
            color: #666;
            margin-bottom: 2rem;
        }
        .btn {
            display: inline-block;
            padding: 0.75rem 2rem;
            margin: 0.5rem;
            font-size: 1rem;
            text-decoration: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-primary {
            background-color: #3d6b4f;
            color: white;
        }
        .btn-secondary {
            background-color: white;
            color: #3d6b4f;
            border: 2px solid #3d6b4f;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Ready 2 Read</h1>
        <p>Track your reading. Share your reviews.</p>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Sign In</a>
        <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary">Register</a>
    </div>
</body>
</html>
