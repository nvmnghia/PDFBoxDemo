package main.Metadata;

/**
 * Created by nghia on 6/21/2017.
 */
public class Input {
    private String file;
    private String url;

    // Constructors
    public Input() {
    }

    public Input(String file, String url) {
        this.file = file;
        this.url = url;
    }

    // Getters and Setters
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
