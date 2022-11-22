package jrails;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import books.Book;
import java.util.List;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ModelTest {

    private Model model;

    @Before
    public void setUp() throws Exception {
        model = new Model(){};
    }

    @Test
    public void id() {
        assertThat(model.id(), notNullValue());
    }

    @Test
    public void bookTest(){
        Book b = new Book();
        b.title = "Programming Languages: Build, Prove, and Compare";
        b.author = "Norman Ramsey";
        b.num_copies = 999;
// The book b exists in memory but isn't saved to the db
        b.save(); // now the book is in the db
        b.num_copies = 42; // the book in the db still has 999 copies
        b.save(); // now the book in the db has 42 copies
        Book b2 = new Book();
        b2.title = "Programming Languages: Build, Prove, and Compare";
        b2.author = "Norman Ramsey";
        b2.num_copies = 888; // hm, same as other book
        b2.save(); // a second record is added to the database
        assert(b.id() != b2.id()); // every row has a globally unique id (int) column, so we can tell them apart
        Book b3 = Model.find(Book.class, 3); // finds the book with id 3 in the db, if any
        List<Book> bs = Model.all(Book.class); // returns all books in the db
        b.destroy(); // remove book b from db
    }

    @After
    public void tearDown() throws Exception {
    }
}