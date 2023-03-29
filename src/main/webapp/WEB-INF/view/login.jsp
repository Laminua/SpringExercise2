<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Login</title>
</head>

<body>

<h2>Please enter your login</h2>

<br>
<br>

<form:form action="/login">

    Login <input type="text" name="userLogin"/>
    <br><br>

    <input type="submit" value="OK">

</form:form>

</body>

</html>
