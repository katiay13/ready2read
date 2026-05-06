package com.ready2read.servlets;

import com.ready2read.dao.UserDAO;
import com.ready2read.models.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
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
        User user = userDAO.getUserByID(userID);

        req.setAttribute("user", user);
        req.setAttribute("formattedJoinDate",
                user != null && user.getJoinDate() != null
                        ? user.getJoinDate().format(DATE_FMT)
                        : "");
        req.setAttribute("activePage", "profile");
        req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userID = (Integer) session.getAttribute("userID");
        String action = req.getParameter("action");

        if ("resetPassword".equals(action)) {
            String newPassword = req.getParameter("newPassword");
            String confirmPassword = req.getParameter("confirmPassword");

            User user = userDAO.getUserByID(userID);
            req.setAttribute("user", user);
            req.setAttribute("formattedJoinDate",
                    user != null && user.getJoinDate() != null
                            ? user.getJoinDate().format(DATE_FMT)
                            : "");
            req.setAttribute("activePage", "profile");

            if (newPassword == null || newPassword.trim().isEmpty()
                    || confirmPassword == null || confirmPassword.trim().isEmpty()) {
                req.setAttribute("error", "All fields are required.");
                req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                req.setAttribute("error", "Passwords do not match.");
                req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);
                return;
            }

            userDAO.updatePassword(userID, newPassword);
            req.setAttribute("success", "Password updated successfully.");
            req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(req, resp);

        } else if ("deleteAccount".equals(action)) {
            userDAO.deleteUser(userID);
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/?deleted=true");
        }
    }
}
