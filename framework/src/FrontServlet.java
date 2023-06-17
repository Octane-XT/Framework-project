package etu1823.framework.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.net.http.HttpClient.Redirect;
import java.util.HashMap;
import java.util.Map;

import utility.*;
import view.ModelView;
import etu1823.framework.Mapping;

public class FrontServlet extends HttpServlet {
  HashMap<String,Mapping> MappingUrls ;
  
  public HashMap<String, Mapping> getMappingUrls() {
    return MappingUrls;
  }

  public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
    MappingUrls = mappingUrls;
  }

  public void init() throws ServletException {
    Utility util = new Utility();
    ServletContext context = getServletContext();
    try {
      setMappingUrls(util.initHashmap(context.getRealPath("/WEB-INF/classes")));
    } catch (Exception e) {
      e.getMessage();
    } 
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    Utility u = new Utility();
    String url = u.splitUrl(request.getRequestURL().toString()); 
    ModelView mv = new ModelView();
    try {
      mv = u.invokeMappedMethod(getMappingUrls(), url);  
      if(mv instanceof ModelView){
        for (Map.Entry<String, Object> entry : mv.getData().entrySet()){
          String key = entry.getKey();
          Object value = entry.getValue();
          System.out.println(value);
          request.setAttribute(key,value);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
        dispatcher.forward(request, response);
      }
    } catch (Exception e) {
      e.getMessage();
    }
    
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    processRequest(request,response);
  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    processRequest(request,response);
  }
}