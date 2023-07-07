package view;

import java.util.HashMap;

public class ModelView {
    String View;
    HashMap<String,Object> data;

    public ModelView() {
        this.data = new HashMap<>();
    }

    public String getView(){
        return View;
    }

    public void setView(String view) {
        View = view;
    }

    public HashMap<String,Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem(String key, Object value){
        this.data.put(key,value);
    }

}
