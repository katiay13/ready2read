package com.ready2read.servlets.admin;

import com.ready2read.dao.BookDAO;
import com.ready2read.dao.ReviewDAO;
import com.ready2read.models.Book;
import com.ready2read.models.Review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/catalog")
public class AdminCatalogServlet extends HttpServlet {

    private static final int PAGE_SIZE = 12;

    private final BookDAO bookDAO = new BookDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        int page = 1;
        try {
            String p = req.getParameter("page");
            if (p != null) page = Integer.parseInt(p);
        } catch (NumberFormatException ignored) {}

        int selectedBookID = 0;
        try {
            String s = req.getParameter("selectedBookID");
            if (s != null) selectedBookID = Integer.parseInt(s);
        } catch (NumberFormatException ignored) {}

        List<Book> books = bookDAO.getAllBooks(page, PAGE_SIZE);
        int totalCount = bookDAO.getTotalBookCount();
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);
        if (totalPages < 1) totalPages = 1;

        Map<Integer, Double> bookRatings = new HashMap<>();
        for (Book book : books) {
            bookRatings.put(book.getBookID(), bookDAO.getAverageRating(book.getBookID()));
        }

        String action = req.getParameter("action");
        if (action == null) action = "";

        List<String> genres = bookDAO.getAllGenres();

        req.setAttribute("books", books);
        req.setAttribute("bookRatings", bookRatings);
        req.setAttribute("genres", genres);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("selectedBookID", selectedBookID);
        req.setAttribute("activePage", "adminCatalog");
        req.setAttribute("action", action);
        req.setAttribute("currentYear", Year.now().getValue());

        if (!"add".equals(action) && selectedBookID > 0) {
            Book selectedBook = bookDAO.getBookByID(selectedBookID);
            double avgRating = bookDAO.getAverageRating(selectedBookID);
            List<Review> bookReviews = reviewDAO.getReviewsByBook(selectedBookID);

            req.setAttribute("selectedBook", selectedBook);
            req.setAttribute("avgRating", avgRating);
            req.setAttribute("bookReviews", bookReviews);
        }

        req.getRequestDispatcher("/WEB-INF/jsp/admin/catalog.jsp").forward(req, resp);
    }
}
