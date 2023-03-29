<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <title>List of users</title>
</head>

<body>

<table border="1" width="20%" align="center">
    <caption><h2>List of users</h2></caption>
    <tr>
        <th>ID</th>
        <th>Login</th>
        <th>Role</th>
        <th>Name</th>
        <th>Email</th>
        <th>Action</th>

        <c:forEach var="user" items="${usersList}">

    <tr>
        <td>${user.id}</td>
        <td>${user.login}</td>
        <td>${user.role}</td>
        <td>${user.name}</td>
        <td>${user.email}</td>
        <td>
            <a href="/deleteUser?userIdToDelete=${user.id}">delete</a>
            <a href="/updateUserForm?userIdToUpdate=${user.id}">update</a>
        </td>

        </c:forEach>

    </tr>
</table>

<a href="/addUserForm"><p style="text-align: center">Add user</p></a>
<a href="/"><p style="text-align: center">Back to login form</p></a>
</body>

</html>