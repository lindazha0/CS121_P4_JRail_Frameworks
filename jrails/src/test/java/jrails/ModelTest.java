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
        assertThat((new Model()).id(), notNullValue());
        assertThat( (new Book()).id(), notNullValue());
    }

    @Test
    public void bookTest()  throws IllegalAccessException{
        Model.reset();

        Book b = new Book();
        b.title = "Programming Languages: Build, Prove, and Compare";
        b.author = "Norman Ramsey";
        b.num_copies = 999;
        // The book b exists in memory but isn't saved to the db
        System.out.println(b.id());
        b.save(); // now the book is in the db
        b.num_copies = 42; // the book in the db still has 999 copies
        b.save(); // now the book in the db has 42 copies'
        System.out.println(b.id());

        Book b1 = new Book();
        b1.title = "Book 1";
        b1.author = "Chuyi Zhao";
        b1.num_copies = 666;
        // The book b exists in memory but isn't saved to the db
        b1.save(); // now the book is in the db
        b1.num_copies = 48; // the book in the db still has 999 copies
        b1.save(); // now the book in the db has 42 copies

        Book b2 = new Book();
        b2.title = "Programming Languages: Build, Prove, and Compare";
        b2.author = "Norman Ramsey";
        b2.num_copies = 888; // hm, same as other book
        b2.save(); // a second record is added to the database
        b2.save();
        b2.save();
        b2.save();

        assert(b.id() != b1.id()); // every row has a globally unique id (int) column, so we can tell them apart
        Book b3 = Model.find(Book.class, b2.id()); // finds the book with id 3 in the db, if any
        assert (b3.id() == Model.find(Book.class, b3.id()).id());

        var list2 = Model.all(Model1.class);
        for(var item: list2){
            System.out.println(item.getFieldString(item.id(), item));
        }

        b3.title="update Title with the fresh instance";
        b3.save();
        List<Book> bs = Model.all(Book.class); // returns all books in the db

//        b.destroy(); // remove book b from db
    }


    @Test
    public void find () throws IllegalAccessException {
        Model.reset();
        Model1 model1 = new Model1();
        model1.s = "string";
        model1.i = -10;
        model1.b = false;
        model1.save();

        Book b = new Book();
        b.author = "Wei Sheng";
        b.title = "SWE 04";
        b.num_copies = 1;
        b.save();

        b = Model.find(b.getClass(),b.id());
        b.author = "Sheng Wei";
        b.save();
    }
    @Test
    public void save(){
        Model.reset();
        Model1 model1 = new Model1();
        model1.s = "string";
        model1.i = -10;
        model1.b = false;

        Model1 model2 = new Model1();
        model2.s = null;
        model2.i = -10;
        model2.b = true;

        Model1 model3 = new Model1();
        model3.s = "";
        model3.i = 0;
        model3.b = false;

        model1.save();
        model2.save();
        model3.save();

        Book b = new Book();
        b.author = " ";
        b.title = "SWE 04";
        b.num_copies = 1;
        b.save();
    }


    @Test
    public void all() throws IllegalAccessException {
        var list = Model.all(Book.class);
        for(var item: list){
            System.out.println(item.getFieldString(item.id(), item));
        }

        var list2 = Model.all(Model1.class);
        for(var item: list2){
            System.out.println(item.getFieldString(item.id(), item));
        }
    }

    @After
    public void tearDown() throws Exception {
    }
}