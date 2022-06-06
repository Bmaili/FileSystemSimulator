package pojo;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 超级块，保存一些系统启动时的必要数据
 */
public class SuperBlock implements Serializable {
    public LinkedList<Block> freeBlocks;//空闲block块，使用空闲链表法来给文件分配Block块
    public LinkedList<INode> freeINodes;//空闲Inode结点

    public ArrayList<User> userList;//用户表
    public ArrayList<UserGroup> userGroupList;//用户组表

    public Folder rootFile;//文件系统根目录

    private SuperBlock() {}

    public static SuperBlock superBlock = new SuperBlock();

}
