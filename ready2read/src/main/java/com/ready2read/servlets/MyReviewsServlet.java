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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet("/my-reviews")
public class MyReviewsServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userID = (Integer) session.getAttribute("userID");
        List<Review> reviews = reviewDAO.getReviewsByUser(userID);

        Map<Integer, String> formattedDates = new HashMap<>();
        for (Review r : reviews) {
            formattedDates.put(r.getReviewID(), r.getDateCreated().format(DATE_FMT));
        }

        req.setAttribute("reviews", reviews);
        req.setAttribute("formattedDates", formattedDates);
        req.setAttribute("activePage", "my-reviews");
        req.getRequestDispatcher("/WEB-INF/jsp/myReviews.jsp").forward(req, resp);
    }
}
