package model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import annotation.Urls;
import view.ModelView;

public class Emp {
    String Nom;
    String Age;

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        this.Age = age;
    }

    public Emp() {
    }
    
    public Emp(String nom,String age) {
        this.Nom = nom;
        this.Age = age;
    }

    public List<Emp> getlistEmp(){
        List<Emp> le = new ArrayList<Emp>();
        le.add(new Emp("Rakoto","18"));
        le.add(new Emp("Rabe","19"));
        le.add(new Emp("Rasoa","20"));
        return le;
    }

    @Urls(values="Emp-all.go")
    public ModelView find_All(){
        ModelView mv = new ModelView();
        mv.setView("emplist.jsp");
        List<Emp> le = getlistEmp();
        mv.addItem("listemp",le);
        return mv;
    }

    @Urls(values="Emp-add.go")
    public ModelView add_emp(){
        ModelView mv = new ModelView();
        mv.setView("empadd.jsp");
        return mv;
    }

    @Urls(values="save.go")
    public ModelView save() {
        ModelView mv = new ModelView();
        mv.setView("empadd.jsp");
        System.out.println("nom "+getNom());
        System.out.println("age "+getAge());
        return mv;
    }
}