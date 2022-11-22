package jrails;

import org.junit.Before;
import org.junit.Test;

import books.BookController;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

public class JRouterTest {

    private JRouter jRouter;

    @Before
    public void setUp() throws Exception {
        jRouter = new JRouter();
    }

    @Test
    public void addRoute() {
        jRouter.addRoute("GET", "/", String.class, "index");
        assertThat(jRouter.getRoute("GET", "/"), is("java.lang.String#index"));
    }

    @Test
    public void rout(){
        jRouter.addRoute("GET", "/", BookController.class, "index");
        jRouter.route("GET", "/",new HashMap<String, String>());
    }
}