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
    <title>My Reviews — Ready 2 Read</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />

<div class="main-content">
    <div class="catalog-area">
        <h2 class="page-title">My Ratings &amp; Reviews</h2>

        <c:choose>
            <c:when test="${empty reviews}">
                <p class="empty-list-msg">
                    You haven't written any reviews yet.
                    <a href="${pageContext.request.contextPath}/catalog">Browse the catalog to get started</a>
                </p>
            </c:when>
            <c:otherwise>
                <c:forEach var="review" items="${reviews}">
                    <div class="review-card user-review">
                        <div class="review-meta">
                            <span class="review-username">${review.bookTitle}</span>
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
                            <span>
                                ${formattedDates[review.reviewID]}
                                <c:if test="${review.dateModified != null}">
                                    <em style="color:#999;">(edited)</em>
                                </c:if>
                            </span>
                            <div class="review-actions" style="margin-top:0;">
                                <button type="button" class="btn btn-secondary btn-sm"
                                        onclick="toggleEditForm(${review.reviewID})">Edit</button>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/reviews/delete"
                                      onsubmit="return confirm('Are you sure you want to delete this review?')"
                                      style="display:inline;">
                                    <input type="hidden" name="reviewID" value="${review.reviewID}">
                                    <input type="hidden" name="source"   value="myReviews">
                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                </form>
                            </div>
                        </div>

                        <div id="editForm-${review.reviewID}" class="edit-form">
                            <form method="post"
                                  action="${pageContext.request.contextPath}/reviews/update">
                                <input type="hidden" name="reviewID" value="${review.reviewID}">
                                <input type="hidden" name="source"   value="myReviews">
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
                                <div style="display:flex; gap:0.5rem;">
                                    <button type="submit" class="btn btn-primary btn-sm">Save Changes</button>
                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="toggleEditForm(${review.reviewID})">Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
function toggleEditForm(reviewID) {
    const form = document.getElementById('editForm-' + reviewID);
    form.style.display = form.style.display === 'none' ? 'block' : 'none';
}
</script>

</body>
</html>
