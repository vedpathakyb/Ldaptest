<%-- 
    Document   : logout
    Created on : Apr 29, 2017, 11:44:05 AM
    Author     : sidde
--%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logout</title>
    </head>
    <body>
        <form action="/APPONE">
            <input type="submit" value="login"/>
        </form>
        <% 
            out.println("<h3>"+session+"</h3>");
            if(session != null){
                request.logout();
                session.invalidate();
                out.println("<h2>You are logged out</h2>");
            }else{
                out.println("<h2>You are not logged in...</h2>");
            }
        %>
        <!--%
            HashMap<String, List<HttpSession>> loggedUserMap = (HashMap<String, List<HttpSession>>) getServletContext().getAttribute("loggedSessions");
            if (loggedUserMap.containsKey(request.getUserPrincipal().getName())) {
                List<HttpSession> sessionList = loggedUserMap.get(request.getUserPrincipal().getName());
                sessionList.remove(request.getSession());
            }
            session.invalidate();
            
            out.println("Logged Out ");
        %-->
    </body>
</html>
