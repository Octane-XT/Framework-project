package etu1823.framework.servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.http.HttpClient.Redirect;
import java.util.Enumeration;
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
      this.setMappingUrls(util.initHashmap(context.getRealPath("/WEB-INF/classes")));
    } catch (Exception e) {
      e.getMessage();
    } 
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    Utility u = new Utility();
    String url = u.splitUrl(request.getRequestURL().toString()); 
    if (MappingUrls.containsKey(url)==true) {
      try {
        ModelView mv = invokeMappedMethod(request,this.getMappingUrls(), url); 
        if(mv.getData() instanceof HashMap){
          sendDataTo(request,mv);
          RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
          dispatcher.forward(request, response);
        }
        
      } catch (Exception e) {
        out.println(e.getMessage());
      }
    }else{
      out.println("Error 404 not found!");
    }
  }

  public void setDataFrom(HttpServletRequest request,Object obj)throws Exception{  
    Enumeration<String> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
        String paramName = parameterNames.nextElement();
        String paramValue = request.getParameter(paramName);
        String methodName = "set" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
        Method setter = obj.getClass().getMethod(methodName, String.class);
        setter.invoke(obj, paramValue);
    }
  }

  public void sendDataTo(HttpServletRequest request,ModelView mv)throws Exception{  
    for (Map.Entry<String, Object> entry : mv.getData().entrySet()){
      String key = entry.getKey();
      Object value = entry.getValue();
      request.setAttribute(key,value);
    }
  }

  public ModelView invokeMappedMethod(HttpServletRequest request,HashMap<String, Mapping> hashMap, String url) throws Exception {
    Utility u = new Utility();
    Mapping mapping = getMappingForUrl(url);
    if (mapping != null) {
        Class<?> clazz = Class.forName(mapping.getClassname());
        Method method = clazz.getMethod(mapping.getMethod());
        Object instance = clazz.newInstance();
        setDataFrom(request,instance);
        ModelView r = (ModelView) method.invoke(instance);
        return r;
    }
    return null;
  }

  public Mapping getMappingForUrl(String url)throws Exception {
    for (Map.Entry<String, Mapping> entry : MappingUrls.entrySet()) {
        String key = entry.getKey();
        Mapping value = entry.getValue();
        if (url.matches(key)) {
            return value;
        }  
    }
    return null;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    processRequest(request,response);
  }
  protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    processRequest(request,response);
  }
}