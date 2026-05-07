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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/catalog")
public class CatalogServlet extends HttpServlet {

    private static final int PAGE_SIZE = 18;

    private final BookDAO bookDAO = new BookDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final ReadingListDAO readingListDAO = new ReadingListDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int page = 1;
        try {
            String p = req.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (NumberFormatException ignored) {}

        String genre = req.getParameter("genre");
        if (genre != null && genre.trim().isEmpty()) genre = null;

        String query = req.getParameter("query");
        if (query != null && query.trim().isEmpty()) query = null;

        int selectedBookID = 0;
        try {
            String s = req.getParameter("selectedBookID");
            if (s != null) selectedBookID = Integer.parseInt(s);
        } catch (NumberFormatException ignored) {}

        Integer userID = (Integer) session.getAttribute("userID");

        List<Book> books;
        int totalCount;
        //adding query check
        if (query != null) {
            books = bookDAO.searchBooks(query, genre, page, PAGE_SIZE);
            totalCount = bookDAO.getSearchBookCount(query, genre);
        } else if (genre == null) {
            books = bookDAO.getAllBooks(page, PAGE_SIZE);
            totalCount = bookDAO.getTotalBookCount();
        } else {
            books = bookDAO.getBooksByGenre(genre, page, PAGE_SIZE);
            totalCount = bookDAO.getTotalBookCountByGenre(genre);
        }

        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;

        Map<Integer, Double> bookRatings = new HashMap<>();
        for (Book book : books) {
            bookRatings.put(book.getBookID(), bookDAO.getAverageRating(book.getBookID()));
        }

        List<String> genres = bookDAO.getAllGenres();

        req.setAttribute("books", books);
        req.setAttribute("bookRatings", bookRatings);
        req.setAttribute("genres", genres);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("selectedGenre", genre);
        req.setAttribute("query", query);
        req.setAttribute("selectedBookID", selectedBookID);
        req.setAttribute("activePage", "catalog");

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

        req.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(req, resp);
    }
}
