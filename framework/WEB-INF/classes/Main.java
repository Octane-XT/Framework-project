import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import utility.Utility;
import view.ModelView;
import etu1823.framework.Mapping;
import model.Emp;

public class Main {
    public static void main(String[] args) throws Exception{
        Utility u = new Utility();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = classLoader.getResource("").getPath();
        path = URLDecoder.decode(path, "UTF-8");
        System.out.println(path);

        List<Class<?>> loadedClasses = u.loadAllClasses(path,"");
        /*for (int i = 0; i < loadedClasses.size(); i++) {
            System.out.println(loadedClasses.get(i));
        }*/
        HashMap<String, Mapping> h = u.initHashmap(path);
        for (Mapping value : h.values()) {
            System.out.print(value.getClass()+"     ");
            System.out.println(value.getMethod());
        }
        //ModelView mv = u.invokeMappedMethod(h, "Dept-all");
        //System.out.println(u.getClassbyMapping(h, "Dept-all"));
        //System.out.println(mv.ge);
        //System.out.println(mv.getView());

        
    }
}
