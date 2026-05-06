package com.ready2read.servlets;

import com.ready2read.dao.BookDAO;
import com.ready2read.dao.ReadingListDAO;
import com.ready2read.dao.ReviewDAO;
import com.ready2read.models.Book;
import com.ready2read.models.ReadingList;
import com.ready2read.models.Review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/reading-list")
public class ReadingListServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();
    private final ReadingListDAO readingListDAO = new ReadingListDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userID = (Integer) session.getAttribute("userID");

        List<ReadingList> entries = readingListDAO.getReadingListByUser(userID);

        int selectedBookID = 0;
        try {
            String s = req.getParameter("selectedBookID");
            if (s != null) selectedBookID = Integer.parseInt(s);
        } catch (NumberFormatException ignored) {}

        req.setAttribute("entries", entries);
        req.setAttribute("selectedBookID", selectedBookID);
        req.setAttribute("activePage", "reading-list");

        if (selectedBookID > 0) {
            Book selectedBook = bookDAO.getBookByID(selectedBookID);
            double avgRating = bookDAO.getAverageRating(selectedBookID);
            List<Review> bookReviews = reviewDAO.getReviewsByBook(selectedBookID);
            ReadingList readingListEntry = readingListDAO.getEntry(userID, selectedBookID);
            Review userReview = reviewDAO.getReviewByUserAndBook(userID, selectedBookID);

            req.setAttribute("selectedBook", selectedBook);
            req.setAttribute("avgRating", avgRating);
            req.setAttribute("bookReviews", bookReviews);
            req.setAttribute("readingListEntry", readingListEntry);
            req.setAttribute("userReview", userReview);
        }

        req.getRequestDispatcher("/WEB-INF/jsp/readingList.jsp").forward(req, resp);
    }
}
