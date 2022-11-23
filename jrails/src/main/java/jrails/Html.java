package jrails;

public class Html {
    // store the txt of the Html obj
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public Html() {
        this.text = "";
    }

    /**
     * Constructor taking the text context
     * 
     * @param text
     */
    public Html(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
        // throw new UnsupportedOperationException();
    }

    public Html seq(Html h) {
        return new Html(text + h.toString());
        // throw new UnsupportedOperationException();
    }

    public Html br() {
        return this.seq(View.br());
        // throw new UnsupportedOperationException();
    }

    public Html t(Object o) {
        // Use o.toString() to get the text for this
        return this.seq(new Html(o.toString()));
        // throw new UnsupportedOperationException();
    }

    public Html p(Html child) {
        return this.seq(View.p(child));
        // throw new UnsupportedOperationException();
    }

    public Html div(Html child) {
        return this.seq(View.div(child));
        // throw new UnsupportedOperationException();
    }

    public Html strong(Html child) {
        return this.seq(View.strong(child));
        // throw new UnsupportedOperationException();
    }

    public Html h1(Html child) {
        return this.seq(View.h1(child));
        // throw new UnsupportedOperationException();
    }

    public Html tr(Html child) {
        return this.seq(View.tr(child));
        // throw new UnsupportedOperationException();
    }

    public Html th(Html child) {
        return this.seq(View.th(child));
        // throw new UnsupportedOperationException();
    }

    public Html td(Html child) {
        return this.seq(View.td(child));
        // throw new UnsupportedOperationException();
    }

    public Html table(Html child) {
        return this.seq(View.table(child));
        // throw new UnsupportedOperationException();
    }

    public Html thead(Html child) {
        return this.seq(View.thead(child));
        // throw new UnsupportedOperationException();
    }

    public Html tbody(Html child) {
        return this.seq(View.tbody(child));
        // throw new UnsupportedOperationException();
    }

    public Html textarea(String name, Html child) {
        return this.seq(View.textarea(name, child));
        // throw new UnsupportedOperationException();
    }

    public Html link_to(String text, String url) {
        return this.seq(View.link_to(text, url));
        // throw new UnsupportedOperationException();
    }

    public Html form(String action, Html child) {
        return this.seq(View.form(action, child));
        // throw new UnsupportedOperationException();
    }

    public Html submit(String value) {
        return this.seq(View.submit(value));
        // throw new UnsupportedOperationException();
    }
}