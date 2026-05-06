package com.ready2read.servlets;

import com.ready2read.dao.ReadingListDAO;
import com.ready2read.models.ReadingList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/reading-list/add")
public class ReadingListAddServlet extends HttpServlet {

    private final ReadingListDAO readingListDAO = new ReadingListDAO();

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
        String status = req.getParameter("status");
        String page = req.getParameter("currentPage");
        String genre = req.getParameter("selectedGenre");
        String source = req.getParameter("source");

        ReadingList existing = readingListDAO.getEntry(userID, bookID);
        if (existing == null) {
            readingListDAO.addEntry(userID, bookID, status);
        }

        if ("readingList".equals(source)) {
            resp.sendRedirect(req.getContextPath() + "/reading-list?selectedBookID=" + bookID);
        } else {
            String redirectURL = req.getContextPath() + "/catalog?page=" + (page != null ? page : "1");
            if (genre != null && !genre.isEmpty()) {
                redirectURL += "&genre=" + genre;
            }
            redirectURL += "&selectedBookID=" + bookID;
            resp.sendRedirect(redirectURL);
        }
    }
}
