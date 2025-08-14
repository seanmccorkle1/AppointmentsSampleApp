package Entities;


/**
 * The logged-in user.
 * <br/>
 * The DB comes with 2 users, for me 'script' and 'admin'
 * <br/>
 * The passwords are both 'script' and 'admin', as well as the usernames
 */
public class User {

    private int id;
    private String username;

    public User() {
        this.id = -1;
        this.username=null;
    }

    public User(int id, String username) {
        this.id = id;
        this.username =username;
    }

    public void set_user_id(int id) {
        this.id = id;
    }

    public void set_user(String username) {
        this.username = username;
    }

    public int get_user_id() {
        return id;
    }
    public String get_user() {
        return username;
    }

}