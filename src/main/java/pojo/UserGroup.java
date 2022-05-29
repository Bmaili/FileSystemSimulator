package pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * 用户组
 */
public class UserGroup implements Serializable {
    public String name;//用户组名，唯一
    public LinkedList<User> members = new LinkedList<>();//用户表

    public HashSet<OsFile> memberOfACL = new HashSet<>();//被包含在哪些文件的ACL表中

    public UserGroup(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
