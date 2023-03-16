package etu1823.framework.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.net.http.HttpClient.Redirect;
import java.util.HashMap;

import utility.*;
import etu1823.framework.Mapping;

public class FrontServlet extends HttpServlet {
  HashMap<String,Mapping> MappingUrls;
  
  public HashMap<String, Mapping> getMappingUrls() {
    return MappingUrls;
  }

  public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
    MappingUrls = mappingUrls;
  }
  
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