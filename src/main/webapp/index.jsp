<%@page import="java.util.Date"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ApplicationOne</title>
    </head>
    <body>
        <h1>Application One</h1>
        
        <%
            out.println("Welcome, "+request.getUserPrincipal());
            out.println("<p>"+new Date()+"</p>");
        %>
        <p><a href="logout.jsp">Click Here to Logout</a></p>
    </body>
</html>
