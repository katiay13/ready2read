<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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
    <title>Catalog — Ready 2 Read</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />

<div class="main-content">

    <!-- ===== CATALOG AREA ===== -->
    <div class="catalog-area">
<h2 class="page-title">Catalog</h2>

<!-- Search Bar -->
<form method="get" action="${pageContext.request.contextPath}/catalog" class="catalog-search">
    <input type="hidden" name="page" value="1">

    <c:if test="${not empty selectedGenre}">
        <input type="hidden" name="genre" value="${selectedGenre}">
    </c:if>

    <input type="text"
           name="query"
           value="${query}"
           placeholder="Search by title, author, or ISBN..."
           class="catalog-search-input">

    <button type="submit" class="btn btn-primary">Search</button>

    <c:if test="${not empty query}">
        <a href="${pageContext.request.contextPath}/catalog" class="btn btn-secondary">Clear</a>
    </c:if>
</form>

<!-- Genre Filter -->
<div class="genre-filter">
            <c:url value="/catalog" var="allUrl">
                <c:param name="page" value="1"/>
            </c:url>
            <a href="${allUrl}" class="genre-btn${empty selectedGenre ? ' active' : ''}">All</a>

            <c:forEach var="g" items="${genres}">
                <c:url value="/catalog" var="genreUrl">
                    <c:param name="page" value="1"/>
                    <c:param name="genre" value="${g}"/>
                </c:url>
                <a href="${genreUrl}"
                   class="genre-btn${selectedGenre == g ? ' active' : ''}">${g}</a>
            </c:forEach>
        </div>

        <!-- Book Grid -->
        <div class="book-grid">
            <c:forEach var="book" items="${books}">
                <c:url value="/catalog" var="bookUrl">
                    <c:param name="page" value="${currentPage}"/>
                    <c:if test="${not empty selectedGenre}">
                        <c:param name="genre" value="${selectedGenre}"/>
                    </c:if>
                    <c:param name="selectedBookID" value="${book.bookID}"/>
                </c:url>
                <a href="${bookUrl}"
                   class="book-card${selectedBookID == book.bookID ? ' selected' : ''}">
                    <p class="book-card-title">${book.title}</p>
                    <p class="book-card-author">${book.author}</p>
                    <p class="book-card-genre">${book.genre}</p>
                    <c:set var="r" value="${bookRatings[book.bookID]}"/>
                    <c:if test="${r > 0}">
                        <p class="book-card-rating">
                            <c:forEach var="i" begin="1" end="5">
                                <c:choose>
                                    <c:when test="${i <= r}">&#9733;</c:when>
                                    <c:otherwise>&#9734;</c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <fmt:formatNumber value="${r}" minFractionDigits="1" maxFractionDigits="1"/>
                        </p>
                    </c:if>
                </a>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <div class="pagination">
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <c:url value="/catalog" var="prevUrl">
                        <c:param name="page" value="${currentPage - 1}"/>
                        <c:if test="${not empty selectedGenre}">
                            <c:param name="genre" value="${selectedGenre}"/>
                        </c:if>
                        <c:if test="${not empty query}">
                                <c:param name="query" value="${query}"/>
                        </c:if>
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
                    <c:url value="/catalog" var="nextUrl">
                        <c:param name="page" value="${currentPage + 1}"/>
                        <c:if test="${not empty selectedGenre}">
                            <c:param name="genre" value="${selectedGenre}"/>
                        </c:if>
                        <c:if test="${not empty query}">
                                <c:param name="query" value="${query}"/>
                        </c:if>
                    </c:url>
                    <a href="${nextUrl}">Next &rarr;</a>
                </c:when>
                <c:otherwise>
                    <span class="disabled">Next &rarr;</span>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- ===== DETAIL PANEL ===== -->
    <c:if test="${selectedBook != null}">
    <div class="detail-panel">

        <!-- A) Book Info -->
        <h2 class="detail-panel-title">${selectedBook.title}</h2>
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

        <!-- C) Reading List -->
        <div class="detail-section">
            <h3 class="detail-section-title">Reading List</h3>
            <c:choose>
                <c:when test="${readingListEntry == null}">
                    <form method="post"
                          action="${pageContext.request.contextPath}/reading-list/add">
                        <input type="hidden" name="bookID"        value="${selectedBook.bookID}">
                        <input type="hidden" name="currentPage"   value="${currentPage}">
                        <input type="hidden" name="selectedGenre" value="${selectedGenre}">
                        <div class="form-group">
                            <label for="addStatus">Status</label>
                            <select name="status" id="addStatus">
                                <option value="want_to_read">Want to Read</option>
                                <option value="currently_reading">Currently Reading</option>
                                <option value="finished">Finished</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Add to Reading List</button>
                    </form>
                </c:when>
                <c:otherwise>
                    <p style="margin:0 0 0.65rem;">
                        <c:choose>
                            <c:when test="${readingListEntry.status.value == 'want_to_read'}">
                                <span class="badge badge-want">Want to Read</span>
                            </c:when>
                            <c:when test="${readingListEntry.status.value == 'currently_reading'}">
                                <span class="badge badge-reading">Currently Reading</span>
                            </c:when>
                            <c:when test="${readingListEntry.status.value == 'finished'}">
                                <span class="badge badge-finished">Finished</span>
                            </c:when>
                        </c:choose>
                    </p>

                    <form method="post"
                          action="${pageContext.request.contextPath}/reading-list/update"
                          style="margin-bottom:0.5rem;">
                        <input type="hidden" name="entryID"       value="${readingListEntry.entryID}">
                        <input type="hidden" name="currentPage"   value="${currentPage}">
                        <input type="hidden" name="selectedGenre" value="${selectedGenre}">
                        <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                        <div class="form-group">
                            <label for="updateStatus">Update Status</label>
                            <select name="status" id="updateStatus">
                                <option value="want_to_read"
                                    ${readingListEntry.status.value == 'want_to_read' ? 'selected' : ''}>
                                    Want to Read</option>
                                <option value="currently_reading"
                                    ${readingListEntry.status.value == 'currently_reading' ? 'selected' : ''}>
                                    Currently Reading</option>
                                <option value="finished"
                                    ${readingListEntry.status.value == 'finished' ? 'selected' : ''}>
                                    Finished</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-secondary btn-sm">Update Status</button>
                    </form>

                    <form method="post"
                          action="${pageContext.request.contextPath}/reading-list/remove"
                          onsubmit="return confirm('Remove this book from your reading list?')">
                        <input type="hidden" name="entryID"       value="${readingListEntry.entryID}">
                        <input type="hidden" name="currentPage"   value="${currentPage}">
                        <input type="hidden" name="selectedGenre" value="${selectedGenre}">
                        <button type="submit" class="btn btn-danger btn-sm">Remove from Reading List</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- D) Reviews -->
        <div class="detail-section">
            <h3 class="detail-section-title">Reviews</h3>

            <% String reviewError = (String) session.getAttribute("reviewError");
               if (reviewError != null) { session.removeAttribute("reviewError"); } %>
            <% if (reviewError != null) { %>
                <p style="color:#c0392b; font-size:0.875rem; margin-bottom:0.75rem;"><%= reviewError %></p>
            <% } %>

            <c:forEach var="review" items="${bookReviews}">
                <c:choose>
                    <c:when test="${review.userID == sessionScope.userID}">
                        <!-- Current user's review — highlighted with edit/delete -->
                        <div class="review-card user-review">
                            <div class="review-meta">
                                <span class="review-username">${review.username} (You)</span>
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
                            <div class="review-actions">
                                <button type="button" class="btn btn-secondary btn-sm"
                                        onclick="toggleEditForm(${review.reviewID})">Edit</button>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/reviews/delete"
                                      onsubmit="return confirm('Delete your review?')"
                                      style="display:inline;">
                                    <input type="hidden" name="reviewID"      value="${review.reviewID}">
                                    <input type="hidden" name="currentPage"   value="${currentPage}">
                                    <input type="hidden" name="selectedGenre" value="${selectedGenre}">
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                            </div>
                            <div id="editForm-${review.reviewID}" class="edit-form" style="display:none;">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/reviews/update">
                                    <input type="hidden" name="reviewID"       value="${review.reviewID}">
                                    <input type="hidden" name="currentPage"    value="${currentPage}">
                                    <input type="hidden" name="selectedGenre"  value="${selectedGenre}">
                                    <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                                    <div class="form-group">
                                        <label for="editRating-${review.reviewID}">Rating</label>
                                        <select name="rating" id="editRating-${review.reviewID}">
                                            <c:forEach var="i" begin="1" end="5">
                                                <option value="${i}"
                                                    ${review.rating == i ? 'selected' : ''}>${i}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="editText-${review.reviewID}">Review</label>
                                        <textarea name="reviewText"
                                                  id="editText-${review.reviewID}"><c:out value="${review.reviewText}"/></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary btn-sm">Save Changes</button>
                                </form>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Other users' reviews -->
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
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- Add review form (only if user hasn't reviewed this book) -->
            <c:if test="${userReview == null}">
                <h4 style="margin:0.75rem 0 0.5rem; font-size:0.9rem;">Add Your Review</h4>
                <form method="post"
                      action="${pageContext.request.contextPath}/reviews/add">
                    <input type="hidden" name="bookID"        value="${selectedBook.bookID}">
                    <input type="hidden" name="currentPage"   value="${currentPage}">
                    <input type="hidden" name="selectedGenre" value="${selectedGenre}">
                    <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                    <div class="form-group">
                        <label for="addRating">Rating</label>
                        <select name="rating" id="addRating">
                            <option value="1">1 — Poor</option>
                            <option value="2">2 — Fair</option>
                            <option value="3">3 — Good</option>
                            <option value="4">4 — Great</option>
                            <option value="5" selected>5 — Excellent</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="addReviewText">Review</label>
                        <textarea name="reviewText" id="addReviewText"
                                  placeholder="Write your review..."></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit Review</button>
                </form>
            </c:if>
        </div>

    </div>
    </c:if>

</div>

<script>
function toggleEditForm(reviewID) {
    const form = document.getElementById('editForm-' + reviewID);
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
}
</script>

</body>
</html>
