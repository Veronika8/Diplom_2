package api.model;

public class User {

    public String getEmail() {
        return email;
    }

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email=email;
        this.password=password;
        this.name=name;
    }

    public User(String email,String password) {
        this.email=email;
        this.password=password;
    }
}
