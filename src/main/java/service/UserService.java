package service;

import pojo.SuperBlock;
import pojo.User;

/**
 * 用户服务类，定义了一些用户的操作
 */
public class UserService {
    /**
     * 检查用户名是否符合规范（不为空不重复）
     *
     * @param
     * @return
     * @date 18:31 2022/5/29
     */
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

    /**
     * 创建用户
     *
     * @param
     * @return
     * @date 18:32 2022/5/29
     */
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
