 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sid.redhat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author sidde
 */
@WebFilter("/CustomFilter")
public class CustomFilter implements Filter{
    private ServletContext context;

    @Override
    public void init(FilterConfig fc) throws ServletException {
        this.context = fc.getServletContext();
        this.context.setAttribute("maxCount", 3);
        HashMap<String,List<HttpSession>> loggedUserMap = new HashMap<String, List<HttpSession>>();
        this.context.setAttribute("loggedSessions", loggedUserMap);
        System.out.println("RequestLoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        
        System.out.println("Filtering");
        HttpServletRequest req = (HttpServletRequest) sr;
        req.getSession(false);
        HashMap<String,List<HttpSession>> loggedUserMap = (HashMap<String,List<HttpSession>>)this.context.getAttribute("loggedSessions");
        
        if (loggedUserMap.containsKey(req.getUserPrincipal().getName()) && loggedUserMap.get(req.getUserPrincipal().getName()).size()<3) {
           
            List<HttpSession> userSession = loggedUserMap.get(req.getUserPrincipal().getName());
            userSession.add(req.getSession());
            loggedUserMap.put(req.getUserPrincipal().getName(), userSession);
        }
        
        else if (loggedUserMap.containsKey(req.getUserPrincipal().getName()) && loggedUserMap.get(req.getUserPrincipal().getName()).size()>=3){
            throw new RuntimeException("Session is exceeds the max count....");
        }
        else if (!loggedUserMap.containsKey(req.getUserPrincipal().getName())) {
            List<HttpSession> userSession = new ArrayList<HttpSession>();
            userSession.add(req.getSession());
            loggedUserMap.put(req.getUserPrincipal().getName(), userSession);
            
        }
        fc.doFilter(sr, sr1);
    }

    @Override
    public void destroy() {
        //we can close resources here
    }
    
}
