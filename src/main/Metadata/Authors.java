package main.Metadata;

/**
 * Created by nghia on 6/21/2017.
 */
public class Authors {
    private Author[] authors;

    // Constructor
    public Authors() {
    }

    public Authors(Author[] authors) {
        this.authors = authors;
    }

    // Getter and Setter
    public Author[] getAuthors() {
        return authors;
    }

    public void setAuthors(Author[] authors) {
        this.authors = authors;
    }
}
