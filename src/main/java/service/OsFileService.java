package service;

import controller.ButtonMenuPanel;
import controller.FileWindow;
import pojo.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * 通用文件服务类，定义了对文件的一些基本操作
 */
public class OsFileService {
    /**
     * 定义了普通文件展示的具体详细信息
     *
     * @param
     * @return
     * @date 17:45 2022/5/29
     */
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

    /**
     * 检查该文件名是否可用（不为空不重复）
     *
     * @param father 这个文件所属的父目录
     * @param name   待检查的文件名
     * @return
     * @date 17:46 2022/5/29
     */
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

    /**
     * 更改文件名
     *
     * @param osFile 选择的文件
     * @param name   名字
     * @return
     * @date 17:47 2022/5/29
     */
    public static boolean changeName(OsFile osFile, String name) {
        if (!checkFileName(osFile.iNode.father, name)) {
            System.out.println("更改失败！名字为空或重复！");
            return false;
        }
        osFile.filename = name;
        return true;
    }

    /**
     * ”写“操作，将本地文件写入到本系统的模拟文件中
     *
     * @param osFile  模拟系统中的文件
     * @param pathStr 本地文件路径
     * @return
     * @date 17:49 2022/5/29
     */
    public static boolean fileToOsFile(OsFile osFile, String pathStr) {
        if (osFile.iNode.fileType != 0) {
            System.out.println("该文件不是普通文件！");
            return false;
        }

        //1.判断：若文件字节数为0或者大于剩余block总容量，则不写入
        File file = new File(pathStr);
        int length = (int) file.length();
        LinkedList<Block> freeBlocks = SuperBlock.superBlock.freeBlocks;
        if (length == 0 || length > freeBlocks.size() * freeBlocks.get(0).data.length) {
            System.out.println("文件字节数为0或者大于剩余block总容量");
            return false;
        }

        //2.释放磁盘块block
        Block free = osFile.iNode.firstBlock;
        Block nextBlock;
        while (free != null) {
            nextBlock = free.next;
            BlockService.freeOneBlock(free);
            free = nextBlock;
        }

        //3.写数据
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {

            INode node = osFile.iNode;
            node.fileSize = 0;
            node.blockCount = 0;
            node.modifyTime = new Date();

            int shengyu = (int) file.length();//本地文件剩余待写的字节数
            Block pre = null;
            while (shengyu > 0) {//当待写入的字节大于0则继续写
                Block block = BlockService.getOneBlock();
                if (node.blockCount == 0) {//如果是第一块
                    node.firstBlock = block;
                }
                node.blockCount++;
                int n = fis.read(block.data);//n为从本地文件一次性读取的字节数
                bos.write(block.data, 0, n);
                if (pre != null) {//将前一块Block与下一块Block连起来
                    pre.next = block;
                }
                pre = block;
                shengyu -= n;
                node.fileSize += n;
            }
            node.lastBlock = pre;//写完后，pre会指向最后一个Block
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * "读"操作，将本系统的模拟文件读到本地文件中
     *
     * @param osFile  选择的模拟文件
     * @param pathStr 本地文件的路径
     * @return
     * @date 17:54 2022/5/29
     */
    public static void osFileToFile(OsFile osFile, String pathStr) {
        if (osFile.iNode.fileType != 0) {
            System.out.println("该文件不是普通文件！");
            return;
        }

        File file = new File(pathStr);
        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            Block block = osFile.iNode.firstBlock;
            while (block.next != null) {// 从选择的模拟文件的第一个block开始读，每读完一个block，就循环读下一个block
                bos.write(block.data);
                block = block.next;
            }
            if (block != null) {
                int len = osFile.iNode.fileSize % block.blockSize;
                //如果len为0，意味着最后一个block刚好填满数据，可以读完，否则对最后一个block只读len长度
                bos.write(block.data, 0, (len == 0 ? block.blockSize : len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建模拟文件
     *
     * @param
     * @return
     * @date 18:04 2022/5/29
     */
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

    /**
     * 删除文件
     *
     * @param
     * @return
     * @date 18:05 2022/5/29
     */
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

    /**
     * 搜索文件
     *
     * @param father 当前目录
     * @param name   文件名
     * @return
     * @date 18:05 2022/5/29
     */
    public static ArrayList<String> searchOsFile(Folder father, String name) {
        ArrayList<String> res = new ArrayList<>();
        dfsSearchOsFile(father, name, res);//实际的搜索函数
        return res;
    }

    // 深度优先递归搜索
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

    /**
     * 读、写、执行 权限检查
     *
     * @param osFile 选择的文件
     * @param user   当前的用户
     * @param auth   期望具备的权限，执行1，写2，读4
     * @return
     * @date 18:07 2022/5/29
     */
    public static boolean authCheck(OsFile osFile, User user, Integer auth) {
        if (osFile == null || user == null) {
            return false;
        }
        Integer userAuth = osFile.iNode.userACL.getOrDefault(user, 0);
        return (userAuth & auth) == auth;//将用户具备的权限与需要的权限 按位与
    }

    /**
     * 调用Windows操作系统接口，模拟双击操作，打开文件
     *
     * @param
     * @return
     * @date 18:57 2022/6/3
     */
    public static void openOsFile(OsFile osFile) {
        //1. 获得模拟文件的后缀名（如txt、png、mp3），如果没有后缀名，默认txt文本打开
        String[] split = osFile.filename.split("\\.");//以小数点分割
        String suffix = "txt";//获得后缀名
        if (split.length > 1) {
            suffix = split[1];
        }

        //2. 在操作系统中建立临时文件
        String tempFilePath = "temp_file." + suffix;
        File tempFile = new File(tempFilePath);
        osFileToFile(osFile, tempFilePath);

        //3. 调用系统API打开文件
        Runtime runtime = Runtime.getRuntime();
        try {
            //调用AOPI打开临时文件
            runtime.exec("rundll32 url.dll FileProtocolHandler " + tempFile.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
