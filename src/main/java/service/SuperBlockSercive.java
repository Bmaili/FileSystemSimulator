package service;

import controller.FileWindow;
import pojo.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class SuperBlockSercive {
    public static String superPass;

    private static void initSuperBlock() {
        SuperBlock.superBlock.freeBlocks = new LinkedList<>();
        SuperBlock.superBlock.freeINodes = new LinkedList<>();
        SuperBlock.superBlock.userList = new ArrayList<>();
        SuperBlock.superBlock.userGroupList = new ArrayList<>();

        UserGroup userGroup1 = new UserGroup("三年二班");
        SuperBlock.superBlock.userGroupList.add(userGroup1);
        UserGroup userGroup2 = new UserGroup("你好吗");
        SuperBlock.superBlock.userGroupList.add(userGroup2);
        UserGroup userGroup3 = new UserGroup("等你下课");
        SuperBlock.superBlock.userGroupList.add(userGroup3);
        User user1 = new User("霍元甲", "666");
        SuperBlock.superBlock.userList.add(user1);
        SuperBlock.superBlock.userGroupList.get(0).members.add(user1);
        User user2 = new User("周杰伦", "666");
        SuperBlock.superBlock.userList.add(user2);
        SuperBlock.superBlock.userGroupList.get(0).members.add(user2);
        User user3 = new User("李荣浩", "666");
        SuperBlock.superBlock.userList.add(user3);
        SuperBlock.superBlock.userGroupList.get(0).members.add(user3);
        User user4 = new User("戳爷", "666");
        SuperBlock.superBlock.userList.add(user4);
        SuperBlock.superBlock.userGroupList.get(1).members.add(user4);
        User user5 = new User("许嵩", "666");
        SuperBlock.superBlock.userList.add(user5);
        SuperBlock.superBlock.userGroupList.get(1).members.add(user5);
        User user6 = new User("河图", "666");
        SuperBlock.superBlock.userList.add(user6);
        SuperBlock.superBlock.userGroupList.get(2).members.add(user6);
        for (int i = 0; i < 1024; i++) {
            BlockService.freeOneBlock(new Block());
        }
        for (int i = 0; i < 216; i++) {
            INodeService.freeOneINode(new INode());
        }
        User rootUser = new User("root", "root");
        SuperBlock.superBlock.rootFile = new Folder("", INodeService.getOneINode());
        INode node = SuperBlock.superBlock.rootFile.iNode;
        node.owner = rootUser;
        node.fileType = 1;
        node.path = "";
        node.father = null;
        OsFileService.createOsFile("示例文件", user2, 0, SuperBlock.superBlock.rootFile);
        OsFileService.createOsFile("示例文件夹", user2, 1, SuperBlock.superBlock.rootFile);
        FileWindow.folderNow = SuperBlock.superBlock.rootFile;
    }

    public static void saveToDisk() {
        try (//创建一个ObjectOutputStream输出流
             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("disk_data"))) {
            oos.writeObject(SuperBlock.superBlock);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFromDisk() {
        File file = new File("disk_data");
        try (//创建一个ObjectInputStream输入流
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            SuperBlock.superBlock = (SuperBlock) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void formatDisk() {
        File file = new File("disk_data");
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    public static void boot() {
        //1.加载管理员密码
        readSuperPass();

        //2.加载数据
        File file = new File("disk_data");
        if (file.isFile() && file.exists()) {
            readFromDisk();
        } else {//第一次使用，没有该文件，则创建
            initSuperBlock();
        }
    }

    private static void readSuperPass() {
        File file = new File("super_pass");
        if (file.isFile() && file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                superPass = reader.readLine();
            } catch (Exception e) {
                saveSuperPass("root");
            }
        } else {
            saveSuperPass("root");
        }
    }

    public static void saveSuperPass(String pass) {
        File file = new File("super_pass");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(pass);
            superPass = pass;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
