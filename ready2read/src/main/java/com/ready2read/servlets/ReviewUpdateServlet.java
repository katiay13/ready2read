package com.ready2read.servlets;

import com.ready2read.dao.ReviewDAO;
import com.ready2read.models.Review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/reviews/update")
public class ReviewUpdateServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int reviewID = Integer.parseInt(req.getParameter("reviewID"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String reviewText = req.getParameter("reviewText");
        String page = req.getParameter("currentPage");
        String genre = req.getParameter("selectedGenre");
        String selectedBookID = req.getParameter("selectedBookID");
        String source = req.getParameter("source");

        String redirectURL;
        if ("myReviews".equals(source)) {
            redirectURL = req.getContextPath() + "/my-reviews";
        } else if ("readingList".equals(source)) {
            redirectURL = req.getContextPath() + "/reading-list";
            if (selectedBookID != null && !selectedBookID.isEmpty()) {
                redirectURL += "?selectedBookID=" + selectedBookID;
            }
        } else {
            redirectURL = req.getContextPath() + "/catalog?page=" + (page != null ? page : "1");
            if (genre != null && !genre.isEmpty()) {
                redirectURL += "&genre=" + genre;
            }
            if (selectedBookID != null && !selectedBookID.isEmpty()) {
                redirectURL += "&selectedBookID=" + selectedBookID;
            }
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            resp.sendRedirect(redirectURL);
            return;
        }

        reviewDAO.updateReview(new Review(reviewID, 0, 0, rating, reviewText.trim(), null, null));

        resp.sendRedirect(redirectURL);
    }
}
