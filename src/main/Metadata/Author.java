package main.Metadata;

/**
 * Created by nghia on 6/21/2017.
 */
public class Author {
    private String name;
    private String affiliation;
    private String tel;
    private String email;

    // Constructors
    public Author() {
    }

    public Author(String name, String affiliation, String tel, String email) {
        this.name = name;
        this.affiliation = affiliation;
        this.tel = tel;
        this.email = email;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
