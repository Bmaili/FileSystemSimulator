package service;

import pojo.SuperBlock;
import pojo.User;
import pojo.UserGroup;

public class UserGroupService {
    public static boolean checkGroupname(String name) {
        if (name == null || name.equals("")) {
            return false;
        }
        for (UserGroup u : SuperBlock.superBlock.userGroupList) {
            if (u.name.equals(name)) {
                return false;
            }
        }
        return true;
    }

    public static boolean createGroup(String name) {
        if (!checkGroupname(name)) {
            System.out.println("创建失败！用户组名为空或重复！");
            return false;
        }
        UserGroup u = new UserGroup(name);
        SuperBlock.superBlock.userGroupList.add(u);
        return true;
    }

    public static void delGroup(UserGroup group) {
        if (group == null) {
            return;
        }
        if (!SuperBlock.superBlock.userGroupList.contains(group)) {
            return;
        }
        SuperBlock.superBlock.userGroupList.remove(group);
    }

    public static void addUserToGroup(UserGroup group, User user) {
        if (group == null || user == null) {//判空
            return;
        }
        if (group.members.contains(user)) {//是否已包含该用户
            return;
        }
        group.members.add(user);
    }

    public static void delUserFromGroup(UserGroup group, User user) {
        if (group == null || user == null) {//判空
            return;
        }
        if (!group.members.contains(user)) {//是否已包含该用户
            return;
        }
        group.members.remove(user);
    }
}
