package pojo;

import java.io.Serializable;

/**
 * 用户类
 */
public class User implements Serializable {
    public String username;//用户名，唯一
    public String password;//用户密码

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return username;
    }
}
