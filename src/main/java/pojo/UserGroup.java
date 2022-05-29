package pojo;

import java.io.Serializable;
import java.util.LinkedList;

public class UserGroup implements Serializable {
    public String name;
    public LinkedList<User> members = new LinkedList<>();

    public UserGroup(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
