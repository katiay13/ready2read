<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    if (session.getAttribute("username") == null ||
            !"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/catalog");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Catalog — Ready 2 Read</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        /* ===== ADMIN TABLE ===== */
        .admin-content {
            flex: 1;
            overflow-y: auto;
            padding: 1.5rem 2rem;
            display: flex;
            flex-direction: column;
        }

        .admin-top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.25rem;
        }

        .book-table {
            width: 100%;
            border-collapse: collapse;
            font-size: 0.88rem;
            background: white;
            border-radius: 6px;
            overflow: hidden;
            border: 1px solid #e0dbd3;
        }

        .book-table thead {
            background: #f0ebe3;
        }

        .book-table th {
            text-align: left;
            padding: 0.7rem 0.9rem;
            font-size: 0.8rem;
            color: #666;
            font-weight: bold;
            border-bottom: 1px solid #e0dbd3;
        }

        .book-table td {
            padding: 0.7rem 0.9rem;
            border-bottom: 1px solid #f0ebe3;
            vertical-align: middle;
        }

        .book-table tbody tr:last-child td {
            border-bottom: none;
        }

        .book-table tbody tr:hover {
            background: #faf8f5;
        }

        .book-table tbody tr.selected-row {
            background: #f0f7f2;
        }

        .book-table .col-title   { min-width: 180px; }
        .book-table .col-author  { min-width: 140px; }
        .book-table .col-genre   { min-width: 100px; }
        .book-table .col-year    { min-width: 70px; }
        .book-table .col-rating  { min-width: 90px; }
        .book-table .col-actions { min-width: 130px; white-space: nowrap; }

        .table-title {
            font-weight: bold;
            color: #2c2c2c;
        }

        .table-secondary {
            color: #666;
        }

        .table-actions {
            display: flex;
            gap: 0.4rem;
        }

        /* ===== ADMIN DETAIL PANEL ===== */
        .admin-detail-panel {
            width: 360px;
            min-width: 360px;
            border-left: 1px solid #e0dbd3;
            overflow-y: auto;
            padding: 1.5rem;
            background: white;
        }

        .detail-panel-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 0.75rem;
        }

        .detail-panel-header .detail-panel-title {
            margin: 0;
            flex: 1;
            padding-right: 0.75rem;
        }

        input[type="number"] {
            width: 100%;
            padding: 0.5rem 0.7rem;
            font-size: 0.88rem;
            font-family: Georgia, serif;
            border: 1px solid #ccc;
            border-radius: 4px;
            background: white;
            color: #2c2c2c;
        }

        input[type="number"]:focus {
            outline: none;
            border-color: #3d6b4f;
        }

        .panel-form-actions {
            display: flex;
            gap: 0.5rem;
            margin-top: 1rem;
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/admin/common/adminSidebar.jsp" />

<div class="main-content">

    <!-- ===== BOOK LIST AREA ===== -->
    <div class="admin-content">

        <div class="admin-top-bar">
            <h2 class="page-title" style="margin:0;">Book Catalog</h2>
            <a href="${pageContext.request.contextPath}/admin/catalog?action=add"
               class="btn btn-primary">Add Book</a>
        </div>

        <c:if test="${not empty param.success}">
            <div class="alert alert-success">${param.success}</div>
        </c:if>

        <!-- Book Table -->
        <table class="book-table">
            <thead>
                <tr>
                    <th class="col-title">Title</th>
                    <th class="col-author">Author</th>
                    <th class="col-genre">Genre</th>
                    <th class="col-year">Year</th>
                    <th class="col-rating">Avg Rating</th>
                    <th class="col-actions">Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="book" items="${books}">
                    <c:set var="isSelected" value="${selectedBookID == book.bookID}"/>
                    <tr class="${isSelected ? 'selected-row' : ''}">
                        <td class="col-title table-title">${book.title}</td>
                        <td class="col-author table-secondary">${book.author}</td>
                        <td class="col-genre table-secondary">${book.genre}</td>
                        <td class="col-year table-secondary">${book.publishedYear}</td>
                        <td class="col-rating">
                            <c:set var="r" value="${bookRatings[book.bookID]}"/>
                            <c:choose>
                                <c:when test="${r > 0}">
                                    <span class="stars" style="font-size:0.8rem;">
                                        <fmt:formatNumber value="${r}" minFractionDigits="1" maxFractionDigits="1"/>
                                    </span>
                                    <span class="table-secondary">/5</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="table-secondary">No ratings</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="col-actions">
                            <div class="table-actions">
                                <c:url value="/admin/catalog" var="editUrl">
                                    <c:param name="page" value="${currentPage}"/>
                                    <c:param name="selectedBookID" value="${book.bookID}"/>
                                    <c:param name="action" value="edit"/>
                                </c:url>
                                <a href="${editUrl}" class="btn btn-secondary btn-sm">Edit</a>

                                <form method="post"
                                      action="${pageContext.request.contextPath}/admin/books/delete"
                                      onsubmit="return confirm('Are you sure you want to remove this book? This will also delete all associated reviews and reading list entries.')">
                                    <input type="hidden" name="bookID"      value="${book.bookID}">
                                    <input type="hidden" name="currentPage" value="${currentPage}">
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty books}">
                    <tr>
                        <td colspan="6" style="text-align:center; color:#999; padding:2rem;">
                            No books in the catalog.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <!-- Pagination -->
        <div class="pagination" style="margin-top:1rem;">
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <c:url value="/admin/catalog" var="prevUrl">
                        <c:param name="page" value="${currentPage - 1}"/>
                    </c:url>
                    <a href="${prevUrl}">&larr; Previous</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">&larr; Previous</span>
                </c:otherwise>
            </c:choose>

            <span class="pagination-label">Page ${currentPage} of ${totalPages}</span>

            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <c:url value="/admin/catalog" var="nextUrl">
                        <c:param name="page" value="${currentPage + 1}"/>
                    </c:url>
                    <a href="${nextUrl}">Next &rarr;</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">Next &rarr;</span>
                </c:otherwise>
            </c:choose>
        </div>

    </div>

    <!-- ===== ADMIN DETAIL PANEL ===== -->
    <c:if test="${action == 'add' or selectedBook != null}">
    <div class="admin-detail-panel">

        <!-- ===== ADD MODE ===== -->
        <c:if test="${action == 'add'}">
            <div class="detail-panel-header">
                <h2 class="detail-panel-title">Add New Book</h2>
            </div>
            <c:if test="${not empty param.error}">
                <div class="alert alert-error">${param.error}</div>
            </c:if>
            <c:url value="/admin/catalog" var="cancelAddUrl">
                <c:param name="page" value="${currentPage}"/>
            </c:url>
            <form method="post" action="${pageContext.request.contextPath}/admin/books/add">
                <input type="hidden" name="currentPage" value="${currentPage}">
                <div class="form-group">
                    <label for="add-title">Title *</label>
                    <input type="text" id="add-title" name="title">
                </div>
                <div class="form-group">
                    <label for="add-author">Author *</label>
                    <input type="text" id="add-author" name="author">
                </div>
                <div class="form-group">
                    <label for="add-genre">Genre</label>
                    <input type="text" id="add-genre" name="genre">
                </div>
                <div class="form-group">
                    <label for="add-year">Published Year</label>
                    <input type="number" id="add-year" name="publishedYear">
                </div>
                <div class="form-group">
                    <label for="add-isbn">ISBN</label>
                    <input type="text" id="add-isbn" name="isbn">
                </div>
                <div class="form-group">
                    <label for="add-desc">Description</label>
                    <textarea id="add-desc" name="description"></textarea>
                </div>
                <div class="panel-form-actions">
                    <button type="submit" class="btn btn-primary">Save Book</button>
                    <a href="${cancelAddUrl}" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </c:if>

        <!-- ===== EDIT MODE ===== -->
        <c:if test="${action == 'edit' and selectedBook != null}">
            <div class="detail-panel-header">
                <h2 class="detail-panel-title">Edit Book</h2>
            </div>
            <c:if test="${not empty param.error}">
                <div class="alert alert-error">${param.error}</div>
            </c:if>
            <c:url value="/admin/catalog" var="cancelEditUrl">
                <c:param name="page" value="${currentPage}"/>
                <c:param name="selectedBookID" value="${selectedBook.bookID}"/>
            </c:url>
            <form method="post" action="${pageContext.request.contextPath}/admin/books/edit">
                <input type="hidden" name="bookID" value="${selectedBook.bookID}">
                <input type="hidden" name="currentPage" value="${currentPage}">
                <div class="form-group">
                    <label for="edit-title">Title *</label>
                    <input type="text" id="edit-title" name="title" value="<c:out value="${selectedBook.title}"/>">
                </div>
                <div class="form-group">
                    <label for="edit-author">Author *</label>
                    <input type="text" id="edit-author" name="author" value="<c:out value="${selectedBook.author}"/>">
                </div>
                <div class="form-group">
                    <label for="edit-genre">Genre</label>
                    <input type="text" id="edit-genre" name="genre" value="<c:out value="${selectedBook.genre}"/>">
                </div>
                <div class="form-group">
                    <label for="edit-year">Published Year</label>
                    <input type="number" id="edit-year" name="publishedYear" value="${selectedBook.publishedYear}">
                </div>
                <div class="form-group">
                    <label for="edit-isbn">ISBN</label>
                    <input type="text" id="edit-isbn" name="isbn" value="<c:out value="${selectedBook.isbn}"/>">
                </div>
                <div class="form-group">
                    <label for="edit-desc">Description</label>
                    <textarea id="edit-desc" name="description"><c:out value="${selectedBook.description}"/></textarea>
                </div>
                <div class="panel-form-actions">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="${cancelEditUrl}" class="btn btn-secondary">Cancel</a>
                </div>
            </form>

            <!-- Average Rating (visible while editing) -->
            <div class="detail-section">
                <h3 class="detail-section-title">Average Rating</h3>
                <c:choose>
                    <c:when test="${avgRating > 0}">
                        <span class="stars">
                            <c:forEach var="i" begin="1" end="5">
                                <c:choose>
                                    <c:when test="${i <= avgRating}">&#9733;</c:when>
                                    <c:otherwise>&#9734;</c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </span>
                        <span class="avg-rating-value">
                            <fmt:formatNumber value="${avgRating}" minFractionDigits="1" maxFractionDigits="1"/>/5
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="avg-rating-value">No ratings yet</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- User Reviews (read-only, visible while editing) -->
            <div class="detail-section">
                <h3 class="detail-section-title">User Reviews</h3>
                <c:choose>
                    <c:when test="${empty bookReviews}">
                        <p style="font-size:0.85rem; color:#999;">No reviews yet for this book.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="review" items="${bookReviews}">
                            <div class="review-card">
                                <div class="review-meta">
                                    <span class="review-username">${review.username}</span>
                                    <span class="review-rating">
                                        <c:forEach var="i" begin="1" end="5">
                                            <c:choose>
                                                <c:when test="${i <= review.rating}">&#9733;</c:when>
                                                <c:otherwise>&#9734;</c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        ${review.rating}/5
                                    </span>
                                </div>
                                <p class="review-text"><c:out value="${review.reviewText}"/></p>
                                <div class="review-meta">
                                    <span>${review.dateCreated.toLocalDate()}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- ===== VIEW MODE ===== -->
        <c:if test="${empty action and selectedBook != null}">

            <!-- A) Book Info -->
            <div class="detail-panel-header">
                <h2 class="detail-panel-title">${selectedBook.title}</h2>
                <c:url value="/admin/catalog" var="editBookUrl">
                    <c:param name="page" value="${currentPage}"/>
                    <c:param name="selectedBookID" value="${selectedBook.bookID}"/>
                    <c:param name="action" value="edit"/>
                </c:url>
                <a href="${editBookUrl}" class="btn btn-secondary btn-sm" style="flex-shrink:0;">Edit Book</a>
            </div>

            <p class="detail-panel-author">by ${selectedBook.author}</p>
            <div class="detail-meta">
                <span>Genre</span>     <span>${selectedBook.genre}</span>
                <span>Published</span> <span>${selectedBook.publishedYear}</span>
                <span>ISBN</span>      <span>${selectedBook.isbn}</span>
            </div>
            <p class="detail-description">${selectedBook.description}</p>

            <!-- B) Average Rating -->
            <div class="detail-section">
                <h3 class="detail-section-title">Average Rating</h3>
                <c:choose>
                    <c:when test="${avgRating > 0}">
                        <span class="stars">
                            <c:forEach var="i" begin="1" end="5">
                                <c:choose>
                                    <c:when test="${i <= avgRating}">&#9733;</c:when>
                                    <c:otherwise>&#9734;</c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </span>
                        <span class="avg-rating-value">
                            <fmt:formatNumber value="${avgRating}" minFractionDigits="1" maxFractionDigits="1"/>/5
                        </span>
                    </c:when>
                    <c:otherwise>
                        <span class="avg-rating-value">No ratings yet</span>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- C) Public Reviews (read-only) -->
            <div class="detail-section">
                <h3 class="detail-section-title">User Reviews</h3>
                <c:choose>
                    <c:when test="${empty bookReviews}">
                        <p style="font-size:0.85rem; color:#999;">No reviews yet for this book.</p>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="review" items="${bookReviews}">
                            <div class="review-card">
                                <div class="review-meta">
                                    <span class="review-username">${review.username}</span>
                                    <span class="review-rating">
                                        <c:forEach var="i" begin="1" end="5">
                                            <c:choose>
                                                <c:when test="${i <= review.rating}">&#9733;</c:when>
                                                <c:otherwise>&#9734;</c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                        ${review.rating}/5
                                    </span>
                                </div>
                                <p class="review-text"><c:out value="${review.reviewText}"/></p>
                                <div class="review-meta">
                                    <span>${review.dateCreated.toLocalDate()}</span>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

        </c:if>

    </div>
    </c:if>

</div>

</body>
</html>
