package utility;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotation.Param;
import annotation.Urls;
import etu1823.framework.Mapping;

public class Utility {

    public static String splitUrl(String url){
        String[] split = url.split("/",-1);
        return split[split.length-1] ;
    }

    public static List<Class<?>> loadAllClasses(String projectDirectory, String pkg) throws Exception {
        List<Class<?>> loadedClasses = new ArrayList<>();
        File directoryPath = new File(projectDirectory);
        File[] listFiles = directoryPath.listFiles();
        if (listFiles == null) {
            throw new Exception("Please check your Project path.");
        }else{
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    loadedClasses.addAll(loadAllClasses(file.getAbsolutePath(), pkg + file.getName() + "."));
                } else if (file.getName().endsWith(".class")) {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(pkg + className);
                    loadedClasses.add(clazz);
                }
            }
        }
        return loadedClasses;
    }
     
    public static HashMap<String, Mapping> initHashmap(String path) throws Exception {
        HashMap<String, Mapping> hm = new HashMap<String, Mapping>();
        List<Class<?>> loadedClasses = loadAllClasses(path, "");
        for (Class<?> cl : loadedClasses) {
            for (Method method : cl.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Urls.class)) {
                    Mapping mapping = new Mapping();
                    Urls annotation = method.getAnnotation(Urls.class);
                    mapping.setClassname(cl.getName());
                    mapping.setMethod(method.getName());
                    String values = annotation.getClass().getMethod("values").invoke(annotation).toString();
                    hm.put(values,mapping);
                } 
            }
        }
        return hm;
    }  

    public static Mapping getMappingForUrl(String url, HashMap<String, Mapping> MappingUrls) throws Exception {
        for (Map.Entry<String, Mapping> entry : MappingUrls.entrySet()) {
            String key = entry.getKey();
            Mapping value = entry.getValue();
            if (url.matches(key)) {
                return value;
            }
        }
        return null;
    }

    public static String getParamName(Parameter parameter) {
        Param paramAnnotation = parameter.getAnnotation(Param.class);
        if (paramAnnotation != null) {
            return paramAnnotation.ParamName();
        }
        return parameter.getName();
    }

    public static Object convertParamValue(String paramValue, Class<?> paramType) {
        if (paramValue == null) {
            return null;
        }
        if (paramType == int.class || paramType == Integer.class) {
            return Integer.parseInt(paramValue);
        } else if (paramType == Double.class) {
            return Double.parseDouble(paramValue);
        } else if (paramType == Float.class) {
            return Float.parseFloat(paramValue);
        } else if (paramType == String.class) {
            return paramValue;
        } else if (paramType == Date.class) {
            return new Date();
        } 
        return null;
    }
}
