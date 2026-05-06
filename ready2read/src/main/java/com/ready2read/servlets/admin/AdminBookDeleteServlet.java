package com.ready2read.servlets.admin;

import com.ready2read.dao.BookDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/books/delete")
public class AdminBookDeleteServlet extends HttpServlet {

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
            bookID = Integer.parseInt(req.getParameter("bookID"));
        } catch (NumberFormatException ignored) {}

        String currentPage = req.getParameter("currentPage");
        if (currentPage == null || currentPage.trim().isEmpty()) currentPage = "1";

        if (bookID > 0) {
            bookDAO.deleteBook(bookID);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/catalog?page=" + currentPage);
    }
}
