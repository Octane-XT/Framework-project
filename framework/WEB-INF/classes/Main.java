import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.Utility;
import view.ModelView;
import etu1823.framework.Mapping;

public class Main {
    public static void main(String[] args) throws Exception{
        Utility u = new Utility();
        List<Class<?>> loadedClasses = u.loadAllClasses("C://Program Files//Apache Software Foundation//Tomcat 8.5//webapps//framework//WEB-INF//classes","");
        /*for (int i = 0; i < loadedClasses.size(); i++) {
            System.out.println(loadedClasses.get(i));
        }*/
        HashMap<String, Mapping> h = u.initHashmap("C://Program Files//Apache Software Foundation//Tomcat 8.5//webapps//framework//WEB-INF//classes");
        for (Mapping value : h.values()) {
            System.out.println(value.getMethod());
        }
        //ModelView mv = u.invokeMappedMethod(h, "Emp-add");
        /*public static List<String> getInfoURL(HttpServletRequest req) {
            return Arrays.asList(req.getServletPath().split("/"));
        }
        public static String getInfo(String url){
            return  url.split("\\?")[0];
        }*/
        
    }
}
