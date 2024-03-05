<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <title>Admin Panel</title>
</head>
<body>
<div th:if="${products}">
  <table>
    <thead>
    <tr>
      <th>ID</th>
      <th>Photo</th>
      <th>Name</th>
      <th>Category</th>
      <th>Price</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <tr th:each="product : ${products}">
      <td th:text="${product.id}"></td>
      <td><img th:src="@{'/images/' + ${product.photo}}" width="50" height="50"/></td>
      <td th:text="${product.name}"></td>
      <td th:text="${product.category}"></td>
      <td th:text="${product.price}"></td>
      <td>

      </td>
    </tr>
    </tbody>
  </table>
</div>
<div th:if="${products.isEmpty()}">
  <p>No products available</p>
</div>

</body>
</html>
