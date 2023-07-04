package model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import annotation.Urls;
import view.ModelView;

public class Dept {
    String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Dept() {
    }

    public Dept(String nom) {
        this.nom = nom;
    }

    @Urls(values = "Dept-add.go")
    public void add_Dept() {
        ModelView mv = new ModelView();
        mv.setView("empadd.jsp");
    }

    @Urls(values = "test")
    public static void test() {

    }
}