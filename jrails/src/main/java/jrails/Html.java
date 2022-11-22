package jrails;

public class Html {
    // store the txt of the Html obj
    private String text;
    public void setText(String text){
        this.text = text;
    }

    /**
     * Constructor taking the text context
     * @param text
     */
    public Html(String text){
        this.text = text;
    }

    public String toString() {
        return text;
        // throw new UnsupportedOperationException();
    }

    public Html seq(Html h) {
        throw new UnsupportedOperationException();
    }

    public Html br() {
        throw new UnsupportedOperationException();
    }

    public static Html t(Object o) {
        // Use o.toString() to get the text for this
        return new Html(o.toString());
        // throw new UnsupportedOperationException();
    }

    public static Html p(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html div(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html strong(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html h1(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html tr(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html th(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html td(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html table(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html thead(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html tbody(Html child) {
        throw new UnsupportedOperationException();
    }

    public Html textarea(String name, Html child) {
        throw new UnsupportedOperationException();
    }

    public Html link_to(String text, String url) {
        throw new UnsupportedOperationException();
    }

    public Html form(String action, Html child) {
        throw new UnsupportedOperationException();
    }

    public Html submit(String value) {
        throw new UnsupportedOperationException();
    }
}