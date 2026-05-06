<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="sidebar">
    <a href="${pageContext.request.contextPath}/admin/catalog" class="sidebar-brand">Ready 2 Read</a>
    <div class="sidebar-username">Admin Panel</div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin/catalog"
           class="nav-link<c:if test="${activePage == 'adminCatalog'}"> active</c:if>">Book Catalog</a>
    </nav>

    <div class="sidebar-logout">
        <a href="${pageContext.request.contextPath}/logout">Log Out</a>
    </div>
</div>
