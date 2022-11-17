package jrails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JRouter {

    /**
     * mapping router path to controller method name
     */
    static Map<String, Map<String, String>> routes = new HashMap<>();
    Class clazz;

    /**
     * @param verb: "GET/POST"
     * @param path: "/path-to-page"
     * @param clazz: which controller class to use
     * @param method: controller methods
     */
    public void addRoute(String verb, String path, Class clazz, String method) {
        this.clazz = clazz;
        // Implement me!
        switch(verb){
            case "GET":
                if(!routes.containsKey("GET")){
                    Map<String, String> getRoutes = new HashMap<>();
                    getRoutes.put(path, method);
                    routes.put("GET", getRoutes);
                }
                routes.get("GET").put(path, method);
                break;
            case "POST":
                break;
            default:
                // error!
        }
    }

    /**
     * @param verb
     * @param path
     * @return "clazz#method" corresponding to verb+URN,
     * Null if no such route
     */
    public String getRoute(String verb, String path) {
        if(!routes.containsKey(verb)){
            throw new UnsupportedOperationException();
        }
        if(!routes.get(verb).containsKey(path)){
            throw new UnsupportedOperationException();
        }

        return clazz.getName() + "#" + routes.get(verb).get(path);

    }

    /**
     * Call the appropriate controller method and
     * @param verb
     * @param path
     * @param params
     * @return return the result
     */
    public Html route(String verb, String path, Map<String, String> params) {
        Method m = clazz.getMethod(getRoute(verb, path));
        Html result=null;

        try {
            result =  (Html) m.invoke(clazz, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
        // throw new UnsupportedOperationException();
    }
}
