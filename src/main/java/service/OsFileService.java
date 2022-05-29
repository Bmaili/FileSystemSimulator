package service;

import controller.FileWindow;
import pojo.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class OsFileService {
    public static String allInfo(OsFile osFile) {
        INode iNode = osFile.iNode;
        Integer auth = iNode.userACL.getOrDefault(FileWindow.userNow, 0);
        String authStr = ((auth & 4) == 4 ? "r" : "-") + ((auth & 2) == 2 ? "w" : "-") + ((auth & 1) == 1 ? "x" : "-");
        return "文件名: " + osFile.filename +
                "\n路径: " + iNode.path +
                "\n所有者: " + iNode.owner +
                "\n文件类型: " + "普通文件" +
                "\n文件大小（字节）: " + iNode.fileSize +
                "\n第一块磁盘块: " + iNode.firstBlock +
                "\n最后一块磁盘块: " + iNode.lastBlock +
                "\n磁盘块数量: " + iNode.blockCount +
                "\n创建时间: " + iNode.createTime +
                "\n更改时间: " + iNode.modifyTime +
                "\n用户权限表: " + iNode.userACL +
                "\n用户组权限表: " + iNode.groupACL +
                "\n您的权限: " + authStr;
    }

    public static boolean checkFileName(Folder father, String name) {
        if (name == null || name.equals("")) {//名字不能为空
            return false;
        }

        //文件名不能重复
        ArrayList<OsFile> list = father.fileList;
        for (OsFile str : list) {
            if (str.filename.equals(name)) {
                return false;
            }
        }
        return true;
    }

    public static boolean changeName(OsFile osFile, String name) {
        if (!checkFileName(osFile.iNode.father, name)) {
            System.out.println("更改失败！名字为空或重复！");
            return false;
        }
        osFile.filename = name;
        return true;
    }

    //将本地文件转换成模拟文件
    public static void fileToOsFile(OsFile osFile, String pathStr) {
        if (osFile.iNode.fileType != 0) {
            System.out.println("该文件不是普通文件！");
            return;
        }
        //1.释放磁盘块block
        Block free = osFile.iNode.firstBlock;
        Block nextBlock;
        while (free != null) {
            nextBlock = free.next;
            BlockService.freeOneBlock(free);
            free = nextBlock;
        }

        //2.写数据
        File file = new File(pathStr);
        if ((int) file.length() == 0) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {

            INode node = osFile.iNode;
            node.fileSize = 0;
            node.blockCount = 0;
            node.modifyTime = new Date();

            int shengyu = (int) file.length();
            Block pre = null;
            while (shengyu > 0) {
                Block block = BlockService.getOneBlock();
                if (node.blockCount == 0) {
                    node.firstBlock = block;
                }
                node.blockCount++;
                int n = fis.read(block.data);
                bos.write(block.data, 0, n);
                if (pre != null) {
                    pre.next = block;
                }
                pre = block;
                shengyu -= n;
                node.fileSize += n;
            }
            node.lastBlock = pre;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void osFileToFile(OsFile osFile, String pathStr) {
        if (osFile.iNode.fileType != 0) {
            System.out.println("该文件不是普通文件！");
            return;
        }

        File file = new File(pathStr);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            Block block = osFile.iNode.firstBlock;
            while (block.next != null) {
                bos.write(block.data);
                block = block.next;
            }
            if (block != null) {
                int len = osFile.iNode.fileSize % block.blockSize;
                bos.write(block.data, 0, (len == 0 ? block.blockSize : len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static OsFile createOsFile(String filename, User owner, Integer fileType, Folder father) {
        if (!checkFileName(father, filename)) {
            System.out.println("创建失败！文件名为空或重复！");
            return null;
        }

        //从free节点链表中拿出一个
        INode node = INodeService.getOneINode();

        //初始化inode节点
        node.owner = owner;
        node.fileType = fileType;
        node.createTime = new Date();
        node.modifyTime = node.createTime;
        node.userACL.put(owner, 7);//创建者获得所有权限
        OsFile file = null;
        if (fileType == 0) {//普通文件
            file = new OsFile(filename, node);
        } else if (fileType == 1) {//目录文件
            file = new Folder(filename, node);
        }
        father.fileList.add(file);
        file.iNode.father = father;
        file.iNode.path = father.iNode.path + father.filename + "/";
        return file;
    }

    public static void deleteOsFile(OsFile osFile, Folder father) {
        if (osFile.iNode.fileType == 1) {//如果删除的文件是目录文件，则递归删除子文件
            Folder folder = (Folder) osFile;
            //模拟CopyOnWriteArrayList 遍历前复制，即写时复制机制
            ArrayList<OsFile> copy = new ArrayList<>(folder.fileList);
            for (OsFile fi : copy) {
                deleteOsFile(fi, folder);
            }
        }
        //1.释放磁盘块block
        Block block = osFile.iNode.firstBlock;
        Block nextBlock;
        while (block != null) {
            nextBlock = block.next;
            BlockService.freeOneBlock(block);
            block = nextBlock;
        }
        //2.释放Inode节点
        INodeService.freeOneINode(osFile.iNode);
        //3.从所属目录中移除该 文件/目录
        father.fileList.remove(osFile);
    }

    public static ArrayList<String> searchOsFile(Folder father, String name) {
        ArrayList<String> res = new ArrayList<>();
        dfsSearchOsFile(father, name, res);
        return res;
    }

    private static void dfsSearchOsFile(Folder father, String name, ArrayList<String> res) {
        for (OsFile fileNow : father.fileList) {
            if (fileNow.iNode.fileType == 0) { //普通文件，检查文件名是否与查询名一致
                if (fileNow.filename.equals(name)) {
                    res.add(fileNow.iNode.path + fileNow.filename);
                }
            } else if (fileNow.iNode.fileType == 1) {//目录文件，递归查找
                dfsSearchOsFile((Folder) fileNow, name, res);
            }
        }
    }

    //读、写、执行 权限检查
    public static boolean authCheck(OsFile osFile, User user, Integer auth) {
        if (osFile == null || user == null) {
            return false;
        }
        Integer userAuth = osFile.iNode.userACL.getOrDefault(user, 0);
        return (userAuth & auth) == auth;
    }
}
