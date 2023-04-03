package model;

import annotation.Urls;

public class Emp {
    String id;
    String nom;

    @Urls(values="Emp-all")
    public String find_All(){
        return "test";
    }
}