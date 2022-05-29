package pojo;

import service.BlockService;
import service.INodeService;
import service.OsFileService;
import controller.FileWindow;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class SuperBlock implements Serializable {
    public LinkedList<Block> freeBlocks;
    public LinkedList<INode> freeINodes;
    public ArrayList<User> userList;
    public ArrayList<UserGroup> userGroupList;

    public Folder rootFile;

    private SuperBlock() {}

    public static SuperBlock superBlock = new SuperBlock();
}
