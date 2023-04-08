<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>

<head>
    <title>Adding user</title>
</head>

<body>

<h2>Please fill all fields</h2>

<br>
<br>

<form:form action="/admin/addUser" modelAttribute="userProfile">

    <form:hidden path="id"/>
    <br><br>
    Username <form:input path="username"/>
    <br><br>
    Password <form:input path="password"/>
    <br><br>
    Role <form:select path="role">
    <form:option value="ROLE_ADMIN" label="ADMIN"/>
    <form:option value="ROLE_USER" label="USER"/>
</form:select>
    <br><br>
    Name <form:input path="name"/>
    <br><br>

    Email <form:input path="email"/>
    <br><br>

    <input type="submit" value="OK">

</form:form>
<form action="/admin/showUsers" target="_blank">
    <button>Back</button>
</form>

</body>

</html>