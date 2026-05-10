package com.ready2read.servlets;

import com.ready2read.dao.ReviewDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/reviews/delete")
public class ReviewDeleteServlet extends HttpServlet {

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
        int reviewID = Integer.parseInt(req.getParameter("reviewID"));
        String page = req.getParameter("currentPage");
        String genre = req.getParameter("selectedGenre");
        String selectedBookID = req.getParameter("selectedBookID");
        String source = req.getParameter("source");

        reviewDAO.deleteReview(reviewID, userID);

        if ("myReviews".equals(source)) {
            resp.sendRedirect(req.getContextPath() + "/my-reviews");
        } else if ("readingList".equals(source)) {
            String redirectURL = req.getContextPath() + "/reading-list";
            if (selectedBookID != null && !selectedBookID.isEmpty()) {
                redirectURL += "?selectedBookID=" + selectedBookID;
            }
            resp.sendRedirect(redirectURL);
        } else {
            String redirectURL = req.getContextPath() + "/catalog?page=" + (page != null ? page : "1");
            if (genre != null && !genre.isEmpty()) {
                redirectURL += "&genre=" + genre;
            }
            if (selectedBookID != null && !selectedBookID.isEmpty()) {
                redirectURL += "&selectedBookID=" + selectedBookID;
            }
            resp.sendRedirect(redirectURL);
        }
    }
}
