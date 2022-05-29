package service;

import pojo.*;
import controller.FileWindow;

public class FolderService {
    public static void enterDirectory(OsFile osFile) {
        if (osFile.iNode.fileType != 1) {
            System.out.println("不是目录文件");
            return;
        }
        FileWindow.folderNow = (Folder) osFile;
        FileWindow.selectFile = null;
    }

    public static void backDirectory() {
        Folder father = FileWindow.folderNow.iNode.father;
        if (father == null) {
            return;
        }
        FileWindow.folderNow = father;
        FileWindow.selectFile = null;
    }


    public static String allInfo(Folder folder) {
        INode iNode = folder.iNode;
        Integer auth = iNode.userACL.getOrDefault(FileWindow.userNow, 0);
        String authStr = ((auth & 4) == 4 ? "r" : "-") + ((auth & 2) == 2 ? "w" : "-") + ((auth & 1) == 1 ? "x" : "-");
        return "文件名: " + folder.filename +
                "\n路径: " + iNode.path +
                "\n所有者: " + iNode.owner +
                "\n文件类型: " + "目录文件" +
                "\n包含文件数量: " + folder.fileList.size() +
                "\n创建时间: " + iNode.createTime +
                "\n更改时间: " + iNode.modifyTime +
                "\n用户权限表: " + iNode.userACL +
                "\n用户组权限表: " + iNode.groupACL +
                "\n您的权限: " + authStr;
    }

}
