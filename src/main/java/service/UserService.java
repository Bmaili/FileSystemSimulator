package service;

import pojo.SuperBlock;
import pojo.User;

public class UserService {
    public static boolean checkUsername(String name) {
        if (name == null || name.equals("")) { //用户名不能为空
            return false;
        }
        for (User u : SuperBlock.superBlock.userList) { //用户名不能重复
            if (u.username.equals(name)) {
                return false;
            }
        }
        return true;
    }

    public static boolean createUser(String name, String password) {
        if (!checkUsername(name)) {
            System.out.println("创建失败！用户名为空或重复！");
            return false;
        }
        User user = new User(name, password);
        SuperBlock.superBlock.userList.add(user);
        return true;
    }
}
