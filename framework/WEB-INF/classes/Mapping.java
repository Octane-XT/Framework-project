package etu1823.framework;

import java.util.HashMap;

public class Mapping {
    String className ;
    String Method ;

    public Mapping() {
    }

    public Mapping(String className, String method) {
        this.className = className;
        this.Method = method;
    }

    public String getClassname() {
        return className;
    }
    public void setClassname(String classname) {
        this.className = classname;
    }
    public String getMethod() {
        return Method;
    }
    public void setMethod(String method) {
        Method = method;
    }
}
