<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile — Ready 2 Read</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .profile-section {
            background: white;
            border: 1px solid #e0dbd3;
            border-radius: 6px;
            padding: 1.25rem 1.5rem;
            margin-bottom: 1.25rem;
            max-width: 480px;
        }
        .profile-section h3 {
            font-size: 1rem;
            margin: 0 0 1rem;
            color: #2c2c2c;
        }
        .profile-field {
            display: grid;
            grid-template-columns: 120px 1fr;
            gap: 0.4rem 0.75rem;
            font-size: 0.88rem;
            margin-bottom: 0.5rem;
        }
        .profile-field-label { color: #999; }
        .profile-field-value { color: #2c2c2c; font-weight: bold; }
        .danger-section {
            background: white;
            border: 1px solid #f5c6cb;
            border-radius: 6px;
            padding: 1.25rem 1.5rem;
            max-width: 480px;
        }
        .danger-section h3 {
            font-size: 1rem;
            margin: 0 0 0.5rem;
            color: #922;
        }
        .danger-section p {
            font-size: 0.85rem;
            color: #666;
            margin: 0 0 1rem;
        }
        .btn-danger-solid {
            background: #922;
            color: white;
            border: none;
            padding: 0.55rem 1.2rem;
            font-size: 0.88rem;
            font-family: Georgia, serif;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn-danger-solid:hover { background: #7a1c1c; }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />

<div class="main-content">
    <div class="catalog-area">
        <h2 class="page-title">My Profile</h2>

        <!-- Profile Info -->
        <div class="profile-section">
            <h3>Account Info</h3>
            <div class="profile-field">
                <span class="profile-field-label">Username</span>
                <span class="profile-field-value">${user.username}</span>
            </div>
            <div class="profile-field">
                <span class="profile-field-label">Email</span>
                <span class="profile-field-value">${user.email}</span>
            </div>
            <div class="profile-field">
                <span class="profile-field-label">Member since</span>
                <span class="profile-field-value">${formattedJoinDate}</span>
            </div>
        </div>

        <!-- Reset Password -->
        <div class="profile-section">
            <h3>Reset Password</h3>

            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/profile">
                <input type="hidden" name="action" value="resetPassword">
                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password" name="newPassword" id="newPassword" autocomplete="new-password">
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" name="confirmPassword" id="confirmPassword" autocomplete="new-password">
                </div>
                <button type="submit" class="btn btn-primary">Reset Password</button>
            </form>
        </div>

        <!-- Danger Zone -->
        <div class="danger-section">
            <h3>Danger Zone</h3>
            <p>Deleting your account is permanent and cannot be undone.</p>
            <form method="post" action="${pageContext.request.contextPath}/profile"
                  onsubmit="return confirm('Are you sure you want to stop reading? :(')">
                <input type="hidden" name="action" value="deleteAccount">
                <button type="submit" class="btn-danger-solid">Delete Account</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
