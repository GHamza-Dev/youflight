<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><
<%@page  import="com.marocair.marocair.model.Client" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>
<form action="add-client" method="post">
    <input type="text" name="email" value="" placeholder="email">
    <input type="password" name="password" value="" placeholder="password">
    <button type="submit">SUBMIT</button>

</form>

    <h3> EMAIL : {client.getEmail()}</h3>
    <h3> PASSW : {client.getPassw()}</h3>
    <h3> PASSW : {client.getId()}</h3>
    <a href="auth/login.jsp">Login</a>

</body>
</html>