package com.ready2read.servlets.admin;

import com.ready2read.dao.BookDAO;
import com.ready2read.models.Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.Year;

@WebServlet("/admin/books/edit")
public class AdminBookEditServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }

        int bookID = 0;
        try {
            String b = req.getParameter("bookID");
            if (b != null) bookID = Integer.parseInt(b.trim());
        } catch (NumberFormatException ignored) {}

        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String genre = req.getParameter("genre");
        int publishedYear = 0;
        try {
            String y = req.getParameter("publishedYear");
            if (y != null && !y.trim().isEmpty()) publishedYear = Integer.parseInt(y.trim());
        } catch (NumberFormatException ignored) {}
        String isbn = req.getParameter("isbn");
        String description = req.getParameter("description");
        String currentPage = req.getParameter("currentPage");
        if (currentPage == null || currentPage.trim().isEmpty()) currentPage = "1";

        int currentYear = Year.now().getValue();
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() +
                    "/admin/catalog?page=" + currentPage +
                    "&selectedBookID=" + bookID +
                    "&action=edit&error=Title+and+author+are+required");
            return;
        }
        if (genre == null || genre.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() +
                    "/admin/catalog?page=" + currentPage +
                    "&selectedBookID=" + bookID +
                    "&action=edit&error=Genre+is+required");
            return;
        }
        if (publishedYear < 1 || publishedYear > currentYear) {
            resp.sendRedirect(req.getContextPath() +
                    "/admin/catalog?page=" + currentPage +
                    "&selectedBookID=" + bookID +
                    "&action=edit&error=Published+year+must+be+between+1+and+" + currentYear);
            return;
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() +
                    "/admin/catalog?page=" + currentPage +
                    "&selectedBookID=" + bookID +
                    "&action=edit&error=ISBN+is+required");
            return;
        }
        if (bookDAO.isbnExists(isbn.trim(), bookID)) {
            resp.sendRedirect(req.getContextPath() +
                    "/admin/catalog?page=" + currentPage +
                    "&selectedBookID=" + bookID +
                    "&action=edit&error=A+book+with+that+ISBN+already+exists");
            return;
        }

        Book book = new Book(bookID, title.trim(), author.trim(),
                genre != null ? genre.trim() : null,
                publishedYear,
                isbn != null ? isbn.trim() : null,
                description != null ? description.trim() : null);
        bookDAO.updateBook(book);

        resp.sendRedirect(req.getContextPath() +
                "/admin/catalog?page=" + currentPage +
                "&selectedBookID=" + bookID +
                "&success=Book+updated+successfully");
    }
}
