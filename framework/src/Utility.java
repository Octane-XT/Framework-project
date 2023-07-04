package utility;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import java.lang.annotation.Annotation;

import annotation.Urls;
import etu1823.framework.Mapping;
import view.ModelView;

public class Utility {

    public String splitUrl(String url){
        String[] split = url.split("/",-1);
        return split[split.length-1] ;
    }

    public List<Class<?>> loadAllClasses(String projectDirectory, String pkg) throws Exception {
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
     
    public HashMap<String, Mapping> initHashmap(String path) throws Exception {
        HashMap<String, Mapping> hm = new HashMap<String, Mapping>();
        Mapping mapping = new Mapping();
        List<Class<?>> loadedClasses = loadAllClasses(path, "");
        for (Class<?> cl : loadedClasses) {
            for (Method method : cl.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Urls.class)) {
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

    public Mapping getMappingForUrl(HashMap<String, Mapping> hashMap, String url)throws Exception {
        for (Map.Entry<String, Mapping> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            Mapping value = entry.getValue();
            if (url.matches(key)) {
                return value;
            }
            else{
                throw new Exception("404 NOT FOUND.");
            }
        }
        return null;
    }

    public ModelView invokeMappedMethod(HashMap<String, Mapping> hashMap, String url) throws Exception {
        Mapping mapping = getMappingForUrl(hashMap, url);
        if (mapping != null) {
            String className = mapping.getClassname();
            String methodName = mapping.getMethod();
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName);
            Object instance = clazz.newInstance();
            ModelView r = (ModelView) method.invoke(instance);
            return r;
        }
        return null;
    }
    
}
