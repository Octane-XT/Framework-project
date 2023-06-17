package model;

import annotation.Urls;
import view.ModelView;

public class Emp {
    String id;
    String nom;

    @Urls(values="Emp-all")
    public ModelView find_All(){
        ModelView mv = new ModelView();
        mv.setView("emplist.jsp");
        return mv;
    }

    @Urls(values="Emp-add")
    public ModelView add_emp(){
        ModelView mv = new ModelView();
        mv.setView("empadd.jsp");
        return mv;
    }
}