<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>

<body>

<h2>Please fill your name and email</h2>

<br>
<br>

<form:form action ="/addUser" modelAttribute="userProfile">

    <form:hidden path="id"/>
    <br><br>
    Name <form:input path="name"/>
    <br><br>
    Email <form:input path="email"/>
    <br><br>

    <input type="submit" value="OK">

</form:form>
<form action="/" target="_blank">
    <button>Back</button>
</form>

</body>

</html>