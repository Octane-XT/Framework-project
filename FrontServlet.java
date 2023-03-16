package etu1823.framework.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.net.http.HttpClient.Redirect;

import utility.*;

public class FrontServlet extends HttpServlet {
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    Utility util = new Utility();
    // Récupère l'URL demandée
    String url = request.getRequestURL().toString();
    out.println(util.splitUrl(url));
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    processRequest(request,response);
  }  
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    processRequest(request,response);
  }
}