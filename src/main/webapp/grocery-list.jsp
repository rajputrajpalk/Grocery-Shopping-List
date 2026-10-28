<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="java.util.List,com.grocery.Grocery,java.math.BigDecimal" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Grocery List</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/style.css">
</head>

<body>

<div class="container wide">

    <h1>Grocery List</h1>

    <a class="link-button"
       href="<%= request.getContextPath() %>/index.jsp">
        Add New Grocery
    </a>

    <%
        List<Grocery> groceries =
            (List<Grocery>) request.getAttribute("groceryList");

        BigDecimal totalCost =
            (BigDecimal) request.getAttribute("totalCost");
    %>

    <div class="total-box">
        Total Price of Unbought Items:
        ₹<%= totalCost %>
    </div>

    <div class="table-container">

        <table>

            <thead>
                <tr>
                    <th>ID</th>
                    <th>Item</th>
                    <th>Quantity</th>
                    <th>Price per Item</th>
                    <th>Total Price</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>

            <tbody>

            <%
                if (groceries != null && !groceries.isEmpty()) {

                    for (Grocery g : groceries) {
            %>

                <tr>
                    <td><%= g.getId() %></td>

                    <td><%= g.getItem() %></td>

                    <td><%= g.getQty() %></td>

                    <td>₹<%= String.format("%.2f", g.getPrice()) %></td>

                    <td>₹<%= String.format("%.2f", g.getTotal()) %></td>

                    <td>
                        <%= g.isBought() ? "Bought" : "Not Bought" %>
                    </td>

                    <td>
                        <form action="<%= request.getContextPath() %>/grocery"
                              method="post">

                            <input type="hidden"
                                   name="action"
                                   value="toggle">

                            <input type="hidden"
                                   name="id"
                                   value="<%= g.getId() %>">

                            <button type="submit">
                                <%= g.isBought()
                                    ? "Mark Unbought"
                                    : "Mark Bought" %>
                            </button>

                        </form>
                    </td>
                </tr>

            <%
                    }

                } else {
            %>

                <tr>
                    <td colspan="7">No grocery records found.</td>
                </tr>

            <%
                }
            %>

            </tbody>

        </table>

    </div>

</div>

</body>

</html>
