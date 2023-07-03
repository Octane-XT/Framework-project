package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import annotation.Param;
import annotation.Urls;
import view.ModelView;

public class Emp {
    String Nom;
    int Age;

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        this.Age = age;
    }

    public Emp() {
    }
    
    public Emp(String nom,int age) {
        this.Nom = nom;
        this.Age = age;
    }

    public List<Emp> getlistEmp(){
        List<Emp> le = new ArrayList<Emp>();
        le.add(new Emp("Rakoto",18));
        le.add(new Emp("Rabe",19));
        le.add(new Emp("Rasoa",20));
        return le;
    }

    @Urls(values = "Emp-all.go")
    public ModelView find_All(){
        ModelView mv = new ModelView();
        mv.setView("emplist.jsp");
        List<Emp> le = getlistEmp();
        mv.addItem("listemp",le);
        return mv;
    }

    @Urls(values = "Emp-add.go")
    public ModelView add_emp(){
        ModelView mv = new ModelView();
        mv.setView("empadd.jsp");
        return mv;
    }

    @Urls(values = "Emp-save.go")
    public ModelView save() {
        ModelView mv = new ModelView();
        mv.setView("empadd.jsp");
        System.out.println("nom "+getNom());
        System.out.println("age "+getAge());
        return mv;
    }

    @Urls(values = "Emp-id.go")
    public ModelView findById(@Param(ParamName = "id") int id,@Param(ParamName = "date") Date date) {
        ModelView mv = new ModelView();
        mv.setView("empinfo.jsp");
        List<Emp> l = getlistEmp();
        mv.addItem("employe", l.get(id));
        mv.addItem("id", id);
        mv.addItem("date", date);
        return mv;
    }
}