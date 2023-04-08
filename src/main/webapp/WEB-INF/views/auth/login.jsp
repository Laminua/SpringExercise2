<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Login</title>
</head>

<body>

<h2>Please enter your login and password</h2>

<br>
<br>

<form:form action="/process_login">

    Username: <input type="text" name="username" id="username"/>
    <br><br>
    Password: <input type="password" name="password" id="password">

    <input type="submit" value="OK">

</form:form>

</body>

</html>
