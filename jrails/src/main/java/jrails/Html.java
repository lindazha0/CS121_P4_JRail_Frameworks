package jrails;

public class Html {
    // store the txt of the Html obj
    private String text;

    public void setText(String text) {
        this.text = text;
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
        return new Html(text +"."+ h.toString());
        // throw new UnsupportedOperationException();
    }

    public Html br() {
        return View.br();
        // throw new UnsupportedOperationException();
    }

    public static Html t(Object o) {
        // Use o.toString() to get the text for this
        return new Html(o.toString());
        // throw new UnsupportedOperationException();
    }

    public static Html p(Html child) {
        return View.p(child);
        // throw new UnsupportedOperationException();
    }

    public Html div(Html child) {
        return View.div(child);
        // throw new UnsupportedOperationException();
    }

    public Html strong(Html child) {
        return View.strong(child);
        // throw new UnsupportedOperationException();
    }

    public Html h1(Html child) {
        return View.h1(child);
        // throw new UnsupportedOperationException();
    }

    public Html tr(Html child) {
        return View.tr(child);
        // throw new UnsupportedOperationException();
    }

    public Html th(Html child) {
        return View.th(child);
        // throw new UnsupportedOperationException();
    }

    public Html td(Html child) {
        return View.td(child);
        // throw new UnsupportedOperationException();
    }

    public Html table(Html child) {
        return View.table(child);
        // throw new UnsupportedOperationException();
    }

    public Html thead(Html child) {
        return View.thead(child);
        // throw new UnsupportedOperationException();
    }

    public Html tbody(Html child) {
        return View.tbody(child);
        // throw new UnsupportedOperationException();
    }

    public Html textarea(String name, Html child) {
        return View.textarea(name, child);
        // throw new UnsupportedOperationException();
    }

    public Html link_to(String text, String url) {
        return View.link_to(text, url);
        // throw new UnsupportedOperationException();
    }

    public Html form(String action, Html child) {
        return View.form(action, child);
        // throw new UnsupportedOperationException();
    }

    public Html submit(String value) {
        return View.submit(value);
        // throw new UnsupportedOperationException();
    }
}