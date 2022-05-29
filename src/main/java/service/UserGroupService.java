package service;

import pojo.OsFile;
import pojo.SuperBlock;
import pojo.User;
import pojo.UserGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 用户组服务类，定义了一些用户组的操作
 */
public class UserGroupService {
    /**
     * 检查用户组名是否符合规范（不为空不重复）
     *
     * @param
     * @return
     * @date 18:29 2022/5/29
     */
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

    /**
     * 创建用户组
     *
     * @param
     * @return
     * @date 18:29 2022/5/29
     */
    public static boolean createGroup(String name) {
        if (!checkGroupname(name)) {
            System.out.println("创建失败！用户组名为空或重复！");
            return false;
        }
        UserGroup u = new UserGroup(name);
        SuperBlock.superBlock.userGroupList.add(u);
        return true;
    }

    /**
     * 删除用户组
     *
     * @param
     * @return
     * @date 18:30 2022/5/29
     */
    public static void delGroup(UserGroup group) {
        if (group == null) {
            return;
        }
        if (!SuperBlock.superBlock.userGroupList.contains(group)) {
            return;
        }
        SuperBlock.superBlock.userGroupList.remove(group);
        delGroupFromFileACL(group);
    }

    /**
     * 向用户组中增加用户
     *
     * @param
     * @return
     * @date 18:30 2022/5/29
     */
    public static void addUserToGroup(UserGroup group, User user) {
        if (group == null || user == null) {//判空
            return;
        }
        if (group.members.contains(user)) {//是否已包含该用户
            return;
        }
        group.members.add(user);
    }

    /**
     * 从用户组中删除用户
     *
     * @param
     * @return
     * @date 18:30 2022/5/29
     */
    public static void delUserFromGroup(UserGroup group, User user) {
        if (group == null || user == null) {//判空
            return;
        }
        if (!group.members.contains(user)) {//是否已包含该用户
            return;
        }
        group.members.remove(user);
    }

    /**
     * 从文件的权限控制表中将该用户组删除
     *
     * @param
     * @return
     * @date 19:30 2022/5/29
     */
    private static void delGroupFromFileACL(UserGroup group) {
        HashSet<OsFile> memberOfACL = group.memberOfACL;
        for (OsFile f : memberOfACL) {
            if (f != null) {
                f.iNode.groupACL.remove(group);
            }
        }
    }
}
