package pojo;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable {
    public String username;
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return username;
    }
}
