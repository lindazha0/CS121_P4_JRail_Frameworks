package jrails;

import java.lang.reflect.*;
import java.util.*;

public class JRouter {
    static private String[] validVerbs = { "GET", "HEAD", "POST", "PUT", "DELETE", "CONNECT", "OPTIONS", "TRACE",
            "PATCH" };

    /**
     * mapping router path to controller method name
     * key: verb
     * value: map<path, method_name>
     */
    static Map<String, Map<String, String>> routes = new HashMap<>();
    static String className;

    /**
     * @param verb:   "GET/POST"
     * @param path:   "/path-to-page"
     * @param clazz:  which controller class to use
     * @param method: controller methods
     */
    public void addRoute(String verb, String path, Class clazz, String method) {
        // check if the verb is valid
        Set<String> validVerbSet = new HashSet<>();
        validVerbSet.addAll(Arrays.asList(validVerbs));
        assert validVerbSet.contains(verb) : "Error: Invalid Route Verb!";

        // implement the router
        className = clazz.getName();

        // if no verb key
        if (!routes.containsKey(verb)) {
            Map<String, String> getRoutes = new HashMap<>();
            getRoutes.put(path, method);
            routes.put(verb, getRoutes);
            return;
        }

        // if verb key
        routes.get(verb).put(path, method);
    }

    /**
     * Helper function
     * 
     * @param verb
     * @param path
     * @return controller#method name based on verb & path
     */
    private String getControllerMethod(String verb, String path) {
        if (!routes.containsKey(verb)) {
            throw new UnsupportedOperationException();
        }
        if (!routes.get(verb).containsKey(path)) {
            throw new UnsupportedOperationException();
        }

        return routes.get(verb).get(path);
    }

    /**
     * @param verb
     * @param path
     * @return "clazz#method" corresponding to verb+URN,
     *         Null if no such route
     */
    public String getRoute(String verb, String path) {
        try {
            return className + "#" + getControllerMethod(verb, path);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Call the appropriate controller method and
     * 
     * @param verb
     * @param path
     * @param params
     * @return return the result
     */
    public Html route(String verb, String path, Map<String, String> params) {
        try {
            Class<?> clazz = Class.forName(className);
            Method m = clazz.getMethod(getControllerMethod(verb, path),Map.class);
            Html result = (Html) m.invoke(clazz, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new UnsupportedOperationException();
    }
}
