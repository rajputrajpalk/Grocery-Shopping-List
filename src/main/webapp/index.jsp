<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Add Grocery</title>

    <link rel="stylesheet"
          href="<%= request.getContextPath() %>/style.css">

    <script>
        function validate() {

            const item =
                document.getElementById("item").value.trim();

            const qty =
                Number(document.getElementById("qty").value);

            const price =
                document.getElementById("price").value.trim();

            if (item === "") {
                alert("Please enter a grocery item.");
                return false;
            }

            if (!Number.isInteger(qty) || qty <= 0) {
                alert("Quantity must be a positive integer.");
                return false;
            }

            if (price === "" ||
                !/^\d+(\.\d{1,2})?$/.test(price)) {

                alert("Enter a valid price with up to 2 decimals.");
                return false;
            }

            return true;
        }
    </script>

</head>

<body>

    <div class="container">

        <h1>Grocery Management System</h1>

        <h2>Add Grocery Item</h2>

        <form action="<%= request.getContextPath() %>/grocery"
              method="post"
              onsubmit="return validate();">

            <input type="hidden"
                   name="action"
                   value="add">

            <label for="item">Grocery Name</label>

            <input type="text"
                   id="item"
                   name="item"
                   maxlength="100"
                   required>

            <label for="qty">Quantity</label>

            <input type="number"
                   id="qty"
                   name="qty"
                   min="1"
                   step="1"
                   required>

            <label for="price">Price per Item (₹)</label>

            <input type="number"
                   id="price"
                   name="price"
                   min="0"
                   step="0.01"
                   required>

            <button type="submit">Add Grocery</button>

        </form>

        <a class="link-button"
           href="<%= request.getContextPath() %>/grocery">
            View Grocery List
        </a>

    </div>

</body>

</html>
