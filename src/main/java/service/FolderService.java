package service;

import pojo.*;
import controller.FileWindow;

/**
 * 目录服务类，相对于通用文件类的操作，这里定义了目录文件特有的相关操作
 */
public class FolderService {

    /**
     * 进入目录
     *
     * @param osFile 选择的目录文件
     * @return
     * @date 17:35 2022/5/29
     */
    public static void enterDirectory(OsFile osFile) {
        if (osFile.iNode.fileType != 1) {
            System.out.println("不是目录文件");
            return;
        }
        FileWindow.folderNow = (Folder) osFile;
        FileWindow.selectFile = null;
    }

    /**
     * 回到上层目录
     *
     * @param
     * @return
     * @date 17:35 2022/5/29
     */
    public static void backDirectory() {
        Folder father = FileWindow.folderNow.iNode.father;
        if (father == null) {
            return;
        }
        FileWindow.folderNow = father;
        FileWindow.selectFile = null;
    }


    /**
     * 定义了目录展示的具体详细信息
     *
     * @param folder 选择的目录
     * @return
     * @date 17:36 2022/5/29
     */
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
