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

@WebServlet("/reviews/add")
public class ReviewAddServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userID = (Integer) session.getAttribute("userID");
        int bookID = Integer.parseInt(req.getParameter("bookID"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String reviewText = req.getParameter("reviewText");
        String page = req.getParameter("currentPage");
        String genre = req.getParameter("selectedGenre");
        String selectedBookID = req.getParameter("selectedBookID");
        String source = req.getParameter("source");

        String redirectURL;
        if ("readingList".equals(source)) {
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
            req.getSession().setAttribute("reviewError", "Review text cannot be empty.");
            resp.sendRedirect(redirectURL);
            return;
        }

        Review existing = reviewDAO.getReviewByUserAndBook(userID, bookID);
        if (existing == null) {
            reviewDAO.addReview(new Review(0, userID, bookID, rating, reviewText.trim(), null, null));
        }

        resp.sendRedirect(redirectURL);
    }
}
