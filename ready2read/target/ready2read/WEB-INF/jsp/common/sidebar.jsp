<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/catalog" class="sidebar-brand">Ready 2 Read</a>
    <div class="sidebar-username">@${sessionScope.username}</div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/catalog"
           class="nav-link<c:if test="${activePage == 'catalog'}"> active</c:if>">Catalog</a>
        <a href="${pageContext.request.contextPath}/reading-list"
           class="nav-link<c:if test="${activePage == 'reading-list'}"> active</c:if>">My Reading List</a>
        <a href="${pageContext.request.contextPath}/my-reviews"
           class="nav-link<c:if test="${activePage == 'my-reviews'}"> active</c:if>">My Reviews</a>
        <a href="${pageContext.request.contextPath}/profile"
           class="nav-link<c:if test="${activePage == 'profile'}"> active</c:if>">My Profile</a>
    </nav>

    <div class="sidebar-logout">
        <a href="${pageContext.request.contextPath}/logout">Log Out</a>
    </div>
</div>
