package com.ready2read.servlets.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/catalog")
public class AdminCatalogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!"admin".equals(req.getSession(false) != null
                ? req.getSession(false).getAttribute("role") : null)) {
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/admin/catalog.jsp").forward(req, resp);
    }
}
