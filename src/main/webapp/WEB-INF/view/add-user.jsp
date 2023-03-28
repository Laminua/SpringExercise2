<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>

<head>
    <title>Adding user</title>
</head>

<body>

<h2>Please fill your name and email</h2>

<br>
<br>

<form:form action ="/addUser" modelAttribute="userProfile">

    <form:hidden path="id"/>
    <br><br>
    Role <form:select path="role">
    <form:option value="ADMIN" label="ADMIN"/>
    <form:option value="USER" label="USER"/>
</form:select>
    <br><br>
    Name <form:input path="name"/>
    <br><br>
    Email <form:input path="email"/>
    <br><br>

    <input type="submit" value="OK">

</form:form>
<form action="/showUsers" target="_blank">
    <button>Back</button>
</form>

</body>

</html>