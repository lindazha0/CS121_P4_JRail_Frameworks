package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static jrails.Html.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

public class ViewTest {
    @Test
    public void test_t_p(){
        assertEquals(Html.t("This is a t test for view").toString(),"This is a t test for view");
        assertEquals(Html.p(Html.t("This is a p test for view")).toString(),"<p>This is a p test for view</p>");
    }

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }
}