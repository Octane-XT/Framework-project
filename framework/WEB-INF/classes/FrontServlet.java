package etu1823.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;

import javax.servlet.*;
import javax.servlet.http.*;

import etu1823.framework.Mapping;
import utility.Utility;
import view.ModelView;

public class FrontServlet extends HttpServlet {
  HashMap<String,Mapping> MappingUrls ;
  
  public HashMap<String, Mapping> getMappingUrls() {
    return MappingUrls;
  }

  public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
    MappingUrls = mappingUrls;
  }

  public void init() throws ServletException {
    ServletContext context = getServletContext();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    try {
      String path = classLoader.getResource("").getPath();
      path = URLDecoder.decode(path, "UTF-8");
      this.setMappingUrls(Utility.initHashmap(path));
    } catch (Exception e) {
      e.getMessage();
    } 
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
    System.out.println("teset");
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String url = Utility.splitUrl(request.getRequestURL().toString()); 
    if (MappingUrls.containsKey(url)==true) {
      try {
        
        ModelView mv = invokeMappedMethod(request,this.getMappingUrls(), url); 
        if(mv.getData() instanceof HashMap){
          sendDataTo(request,mv);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
        dispatcher.forward(request, response);
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
        String methodName = "set" + paramName.substring(0, 1).toUpperCase() + paramName.substring(1);
        System.out.println(obj.getClass().getDeclaredField(paramName).getType());
        Method setter = obj.getClass().getMethod(methodName, obj.getClass().getDeclaredField(paramName).getType());
        Object paramValue = Utility.convertParamValue(request.getParameter(paramName), obj.getClass().getDeclaredField(paramName).getType());
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

  public static Object[] extractParameters(HttpServletRequest request, Method method) {
    Parameter[] parameters = method.getParameters();
    Object[] extractedParams = new Object[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      String paramName = Utility.getParamName(parameter);
      // Extract the parameter value from the request
      String paramValue = request.getParameter(paramName);
      // Convert the parameter value to the appropriate type
      Object convertedValue = Utility.convertParamValue(paramValue, parameter.getType());
      extractedParams[i] = convertedValue;
    }
    return extractedParams;
  }


  public ModelView invokeMappedMethod(HttpServletRequest request,HashMap<String, Mapping> hashMap, String url)throws Exception {
    Mapping mapping = Utility.getMappingForUrl(url,getMappingUrls());
    ModelView r = null;
    if (mapping != null) {
        Class<?> clazz = Class.forName(mapping.getClassname());
        Method methode = null;
        for (Method m : clazz.getDeclaredMethods()) {
            if (mapping.getMethod() == m.getName()) {
                methode = m;
            }
        }
        if (methode == null)
            throw new Exception("aucune methode ne correspond à " + mapping.getMethod());

        Object instance = clazz.getConstructor().newInstance();
        if (methode.getParameters().length > 0) {
          r = (ModelView) methode.invoke(instance, extractParameters(request,methode));
        }else{
          setDataFrom(request,instance);
          r = (ModelView) methode.invoke(instance);
        }
        return r;
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