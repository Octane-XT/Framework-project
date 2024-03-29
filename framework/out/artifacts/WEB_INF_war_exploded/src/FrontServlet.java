package etu1823.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import etu1823.framework.FileUpload;
import etu1823.framework.Mapping;
import utility.Utility;
import view.ModelView;

@MultipartConfig
public class FrontServlet extends HttpServlet {
  HashMap<String,Mapping> MappingUrls ;
  HashMap<String, Object> singleton;
  
  public HashMap<String, Mapping> getMappingUrls() {
    return MappingUrls;
  }

  public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
    MappingUrls = mappingUrls;
  }

  public HashMap<String, Object> getSingleton() {
    return singleton;
  }

  public void setSingleton(HashMap<String, Object> annotedClass) {
    singleton = annotedClass;
  }

  public void init() throws ServletException {
    ServletContext context = getServletContext();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    try {
      String path = classLoader.getResource("").getPath();
      path = URLDecoder.decode(path, "UTF-8");
      setMappingUrls(Utility.initHashmap(path));
      setSingleton(Utility.getAnnotatedClasses(path));
    } catch (Exception e) {
      e.getMessage();
    } 
  }

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException { 
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

  private boolean isFilePart(Part part) {
    String fileName = part.getSubmittedFileName();
    return fileName != null && !fileName.isEmpty();
  }

  public void setDataFrom(HttpServletRequest request,Object obj)throws Exception{  
      for (Field field : obj.getClass().getDeclaredFields() ) {
        String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        if(field.getType()==FileUpload.class){
          FileUpload fileUpload = new FileUpload();
          Part filePart = request.getPart(field.getName());
          if (filePart != null && isFilePart(filePart)) {
            String fileName = filePart.getSubmittedFileName();
            byte[] fileData = filePart.getInputStream().readAllBytes();
            fileUpload.setNom(fileName);
            fileUpload.setData(fileData);
            Method setter = obj.getClass().getMethod(methodName, FileUpload.class);
            setter.invoke(obj, fileUpload);
          }
        }else {
            Method setter = obj.getClass().getMethod(methodName, field.getType());
            Object paramValue = Utility.convertParamValue(request.getParameter(field.getName()),field.getType());
            setter.invoke(obj, paramValue);
        }
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
      String paramValue = request.getParameter(paramName);
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
        Object instance = null;
        if(Utility.isSingleton(clazz)){
          if(getSingleton().containsKey(clazz.getSimpleName())){
            if(getSingleton().get(clazz.getSimpleName())==null){
              instance = clazz.newInstance();
              getSingleton().replace(clazz.getSimpleName(), null, instance);
            }else{
              instance = getSingleton().get(clazz.getSimpleName());
            }
          }else{
            instance = clazz.getConstructor().newInstance();
          }
        }
        Method methode = null;
        for (Method m : clazz.getDeclaredMethods()) {
            if (mapping.getMethod() == m.getName()) {
                methode = m;
            }
        }
        if (methode == null)
            throw new Exception("aucune methode ne correspond à " + mapping.getMethod());

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