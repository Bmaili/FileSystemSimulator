package controller;

import pojo.OsFile;
import service.OsFileService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;

/**
 * 文件列表控件，描述其UI并绑定相关事件
 */
public class FileListPanel extends JList {
    //列表的数据Model，用于列表数据的增删改
    private DefaultListModel<OsFile> pageModel = new DefaultListModel<>();

    FileListPanel() {
        setModel(pageModel);
        setCellRenderer(new ListCell());
        bindEvent();
    }

    public void updateData() {
        ArrayList<OsFile> list = FileWindow.folderNow.fileList;
        pageModel.clear();
        for (int i = 0; i < list.size(); i++) {
            pageModel.add(i, list.get(i));
        }
    }

    private void bindEvent() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int click_num = e.getClickCount();
                if (click_num == 2) {
                    // 1. 如果不是普通文件，那没事了
                    if (FileWindow.selectFile.iNode.fileType != 0) {
                        return;
                    }

                    //2. 需要读取权限
                    boolean b = OsFileService.authCheck(FileWindow.selectFile, FileWindow.userNow, 4);
                    if (b) {
                        OsFileService.openOsFile(FileWindow.selectFile);//打开虚拟文件
                    } else {
                        System.out.println("权限不足！无法打开文件！");
                    }
                }
            }
        });
    }


}

/**
 * 自定义列表单元格
 */
class ListCell extends DefaultListCellRenderer {
    private ImageIcon folder;
    private ImageIcon file;

    ListCell() {
        URL folder_url = this.getClass().getClassLoader().getResource("icon/folder.png");
        URL file_url = this.getClass().getClassLoader().getResource("icon/file.png");
        folder = new ImageIcon(folder_url);
        file = new ImageIcon(file_url);
        folder.setImage(folder.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        file.setImage(file.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
    }

    //重新定义选项事件
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list,
                                                  Object value, int index, boolean isSelected, boolean cellHasFocus) {

        ImageIcon icon = null;
        OsFile osFile = (OsFile) value;
        if (osFile.iNode.fileType == 0) {
            icon = file;
        } else if (osFile.iNode.fileType == 1) {
            icon = folder;
        }
        setIcon(icon);
        setText(osFile.filename);
        setBackground(Color.white);

        if (isSelected) {
            FileWindow.selectFile = osFile;
            FileWindow.fileProperties.updateData();
            setBackground(Color.cyan);
        }


        return this;
    }
}
