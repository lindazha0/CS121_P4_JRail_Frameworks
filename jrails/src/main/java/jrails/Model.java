package jrails;

import java.util.List;

public class Model {

    public void save() {
        /* this is an instance of the current model */
        throw new UnsupportedOperationException();
    }

    public int id() {
        throw new UnsupportedOperationException();
    }

    public static <T> T find(Class<T> c, int id) {
        throw new UnsupportedOperationException();
    }

    public static <T> List<T> all(Class<T> c) {
        // Returns a List<element type>
        throw new UnsupportedOperationException();
    }

    public void destroy() {
        throw new UnsupportedOperationException();
    }

    public static void reset() {
        throw new UnsupportedOperationException();
    }
}
