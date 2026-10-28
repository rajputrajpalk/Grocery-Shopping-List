package com.grocery;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/grocery")
public class GroceryServlet extends HttpServlet {


@Override
protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    List<Grocery> groceries = new ArrayList<>();
    BigDecimal totalCost = BigDecimal.ZERO;

    String selectSQL =
            "SELECT Id, item, qty, price, brgt " +
            "FROM grossary ORDER BY Id";

    String totalSQL =
            "SELECT COALESCE(SUM(qty * price), 0) AS total " +
            "FROM grossary WHERE brgt = 0";

    try (Connection con = DBConnection.getConnection()) {

        // Retrieve all grocery records
        try (PreparedStatement ps =
                     con.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Grocery grocery = new Grocery(
                        rs.getInt("Id"),
                        rs.getString("item"),
                        rs.getInt("qty"),
                        rs.getDouble("price"),
                        rs.getBoolean("brgt")
                );

                groceries.add(grocery);
            }
        }

        // Calculate the expense of unbought items
        try (PreparedStatement ps =
                     con.prepareStatement(totalSQL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                totalCost = rs.getBigDecimal("total");
            }
        }

    } catch (SQLException e) {
        throw new ServletException(
                "Unable to retrieve grocery records.", e);
    }

    request.setAttribute("groceryList", groceries);
    request.setAttribute("totalCost", totalCost);

    request.getRequestDispatcher("/grocery-list.jsp")
            .forward(request, response);
}

@Override
protected void doPost(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    String action = request.getParameter("action");

    try (Connection con = DBConnection.getConnection()) {

        if ("add".equals(action)) {

            String item = request.getParameter("item");
            String qtyValue = request.getParameter("qty");
            String priceValue = request.getParameter("price");

            if (item == null || item.trim().isEmpty()
                    || item.trim().length() > 100
                    || qtyValue == null || priceValue == null
                    || qtyValue.trim().isEmpty()
                    || priceValue.trim().isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Please enter valid grocery details.");
                return;
            }

            int qty = Integer.parseInt(qtyValue.trim());
            BigDecimal price =
                    new BigDecimal(priceValue.trim());

            if (qty <= 0
                    || price.compareTo(BigDecimal.ZERO) < 0
                    || price.scale() > 2) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Please enter valid grocery details.");
                return;
            }

            String sql =
                    "INSERT INTO grossary " +
                    "(item, qty, price, brgt) " +
                    "VALUES (?, ?, ?, 0)";

            try (PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setString(1, item.trim());
                ps.setInt(2, qty);
                ps.setBigDecimal(3, price);

                ps.executeUpdate();
            }

        } else if ("toggle".equals(action)) {

            String idValue = request.getParameter("id");

            if (idValue == null || idValue.trim().isEmpty()) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid grocery ID.");
                return;
            }

            int id = Integer.parseInt(idValue.trim());

            if (id <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid grocery ID.");
                return;
            }

            String sql =
                    "UPDATE grossary " +
                    "SET brgt = 1 - brgt WHERE Id = ?";

            try (PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.executeUpdate();
            }

        } else {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid action.");
            return;
        }

    } catch (NumberFormatException e) {

        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid quantity, price, or ID.");
        return;

    } catch (SQLException e) {

        throw new ServletException(
                "Unable to update the grocery database.", e);
    }

    // Post/Redirect/Get prevents duplicate submissions on refresh
    response.sendRedirect(
            request.getContextPath() + "/grocery");
}


}
