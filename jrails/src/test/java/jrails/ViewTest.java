package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import books.Book;
import books.BookView;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.*;

public class ViewTest {
    @Test
    public void test_t_p(){
        assertEquals(View.t("This is a t test for view").toString(),"This is a t test for view");
        assertEquals(View.p(View.t("This is a p test for view")).toString(),"<p>This is a p test for view</p>");
    }

    @Test
    public void bookTest(){
        Book b = new Book();
        b.title = "Programming Languages: Build, Prove, and Compare";
        b.author = "Norman Ramsey";
        b.num_copies = 999;
        String s = BookView.show(b).toString();
        System.out.println(s);
    }

    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }
}
