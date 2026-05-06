<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign In — Ready 2 Read</title>
    <style>
        * { box-sizing: border-box; }
        body {
            font-family: Georgia, serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            background-color: #f9f5f0;
        }
        .container {
            width: 100%;
            max-width: 400px;
            padding: 2rem;
            text-align: center;
        }
        h1 {
            font-size: 2rem;
            margin-bottom: 0.25rem;
            color: #2c2c2c;
        }
        h1 a {
            text-decoration: none;
            color: inherit;
        }
        h2 {
            font-size: 1.1rem;
            font-weight: normal;
            color: #666;
            margin-bottom: 1.5rem;
        }
        .alert {
            padding: 0.75rem 1rem;
            border-radius: 4px;
            margin-bottom: 1rem;
            font-size: 0.95rem;
            text-align: left;
        }
        .alert-error {
            background-color: #fdecea;
            color: #922;
            border: 1px solid #f5c6cb;
        }
        .alert-success {
            background-color: #e8f5e9;
            color: #2e6b3e;
            border: 1px solid #c3e6cb;
        }
        form {
            display: flex;
            flex-direction: column;
            gap: 0.75rem;
        }
        input[type="text"], input[type="password"] {
            padding: 0.65rem 0.85rem;
            font-size: 1rem;
            font-family: Georgia, serif;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 100%;
        }
        input[type="text"]:focus, input[type="password"]:focus {
            outline: none;
            border-color: #3d6b4f;
        }
        button[type="submit"] {
            padding: 0.75rem;
            font-size: 1rem;
            font-family: Georgia, serif;
            background-color: #3d6b4f;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button[type="submit"]:hover {
            background-color: #2f5540;
        }
        .footer-link {
            margin-top: 1.25rem;
            font-size: 0.9rem;
            color: #666;
        }
        .footer-link a {
            color: #3d6b4f;
            text-decoration: none;
        }
        .footer-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1><a href="${pageContext.request.contextPath}/">Ready 2 Read</a></h1>
    <h2>Sign in to your account</h2>

    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
        <div class="alert alert-error"><%= error %></div>
    <% } %>

    <% String success = (String) request.getAttribute("success"); %>
    <% if (success != null) { %>
        <div class="alert alert-success"><%= success %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <input type="text" name="username" placeholder="Username"
               value="<%= request.getParameter("username") != null ? request.getParameter("username") : "" %>"
               required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Sign In</button>
    </form>

    <p class="footer-link">Don't have an account?
        <a href="${pageContext.request.contextPath}/register">Register here</a>
    </p>
</div>
</body>
</html>
