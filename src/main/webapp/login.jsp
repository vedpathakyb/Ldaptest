<%-- 
    Document   : login
    Created on : Apr 29, 2017, 11:42:36 AM
    Author     : sidde
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>Login Page</title>
</head>

<form action="j_security_check" method="post">
    <p><strong>User Name: </strong><input type="text" name="j_username" size="25" value="jboss">
    <p><p><strong>Password: </strong><input type="password" size="15" name="j_password" value="RedHat1!">
    <p><p><input type="submit" value="Submit"><input type="reset" value="Reset">
</form>
</html>
