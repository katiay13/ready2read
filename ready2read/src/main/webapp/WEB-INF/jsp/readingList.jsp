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
    <title>My Reading List — Ready 2 Read</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />

<div class="main-content">

    <!-- ===== READING LIST AREA ===== -->
    <div class="catalog-area">
        <h2 class="page-title">My Reading List</h2>

        <c:choose>
            <c:when test="${empty entries}">
                <p class="empty-list-msg">
                    Your reading list is empty.
                    <a href="${pageContext.request.contextPath}/catalog">Browse the catalog</a>
                    to add books!
                </p>
            </c:when>
            <c:otherwise>
                <div class="reading-list-entries">
                    <c:forEach var="entry" items="${entries}">
                        <c:url value="/reading-list" var="entryUrl">
                            <c:param name="selectedBookID" value="${entry.bookID}"/>
                        </c:url>
                        <a href="${entryUrl}"
                           class="reading-list-row${selectedBookID == entry.bookID ? ' selected' : ''}">
                            <div class="reading-list-row-info">
                                <p class="book-card-title">${entry.bookTitle}</p>
                                <p class="book-card-author">${entry.bookAuthor}</p>
                            </div>
                            <div class="reading-list-row-meta">
                                <c:choose>
                                    <c:when test="${entry.status.value == 'want_to_read'}">
                                        <span class="badge badge-want">Want to Read</span>
                                    </c:when>
                                    <c:when test="${entry.status.value == 'currently_reading'}">
                                        <span class="badge badge-reading">Currently Reading</span>
                                    </c:when>
                                    <c:when test="${entry.status.value == 'finished'}">
                                        <span class="badge badge-finished">Finished</span>
                                    </c:when>
                                </c:choose>
                                <span class="reading-list-date">${entry.dateAdded.toLocalDate()}</span>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
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
                        <input type="hidden" name="bookID"         value="${selectedBook.bookID}">
                        <input type="hidden" name="currentPage"    value="1">
                        <input type="hidden" name="selectedGenre"  value="">
                        <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                        <input type="hidden" name="source"         value="readingList">
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
                        <input type="hidden" name="entryID"        value="${readingListEntry.entryID}">
                        <input type="hidden" name="currentPage"    value="1">
                        <input type="hidden" name="selectedGenre"  value="">
                        <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                        <input type="hidden" name="source"         value="readingList">
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
                        <input type="hidden" name="currentPage"   value="1">
                        <input type="hidden" name="selectedGenre" value="">
                        <input type="hidden" name="source"        value="readingList">
                        <button type="submit" class="btn btn-danger btn-sm">Remove from Reading List</button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- D) Reviews -->
        <div class="detail-section">
            <h3 class="detail-section-title">Reviews</h3>

            <c:forEach var="review" items="${bookReviews}">
                <c:choose>
                    <c:when test="${review.userID == sessionScope.userID}">
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
                                    <input type="hidden" name="reviewID"       value="${review.reviewID}">
                                    <input type="hidden" name="currentPage"    value="1">
                                    <input type="hidden" name="selectedGenre"  value="">
                                    <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                                    <input type="hidden" name="source"         value="readingList">
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                            </div>
                            <div id="editForm-${review.reviewID}" class="edit-form">
                                <form method="post"
                                      action="${pageContext.request.contextPath}/reviews/update">
                                    <input type="hidden" name="reviewID"       value="${review.reviewID}">
                                    <input type="hidden" name="currentPage"    value="1">
                                    <input type="hidden" name="selectedGenre"  value="">
                                    <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                                    <input type="hidden" name="source"         value="readingList">
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

            <c:if test="${userReview == null}">
                <h4 style="margin:0.75rem 0 0.5rem; font-size:0.9rem;">Add Your Review</h4>
                <form method="post"
                      action="${pageContext.request.contextPath}/reviews/add">
                    <input type="hidden" name="bookID"         value="${selectedBook.bookID}">
                    <input type="hidden" name="currentPage"    value="1">
                    <input type="hidden" name="selectedGenre"  value="">
                    <input type="hidden" name="selectedBookID" value="${selectedBook.bookID}">
                    <input type="hidden" name="source"         value="readingList">
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
