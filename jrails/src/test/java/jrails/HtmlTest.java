package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

public class HtmlTest {

    private Html html;

    @Before
    public void setUp() throws Exception {
        html = new Html();
    }

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }

    @Test
    public void shengTest() {
        System.out.println(html.p(html.t("text0")));
        System.out.println(html.p(html.p(html.t("text2"))));
        System.out.println(html.p(html.t("text3")).p(html.t("test4")));
        System.out.println(html.p(html.p(html.t("text5"))).p(html.t("test6")));
    }
}
