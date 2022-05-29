package controller;

import pojo.Folder;
import pojo.OsFile;
import service.FolderService;
import service.OsFileService;

import javax.swing.*;
import java.awt.*;

/**
 * 文件相信信息的展示控件，描述其UI并绑定相关事件
 */
public class FilePropertiesPanel extends JTextArea {
    FilePropertiesPanel() {
        this.setEditable(false);
        Font font = new Font("Serif", 1, 20);
        this.setFont(font);
    }

    //更新数据
    public void updateData() {
        OsFile selectFile = FileWindow.selectFile;
        if (selectFile == null) {
            this.setText("请选择文件~");
        } else if (selectFile.iNode.fileType == 0) {
            this.setText(OsFileService.allInfo(selectFile));
        } else if (selectFile.iNode.fileType == 1) {
            this.setText(FolderService.allInfo((Folder) selectFile));
        }
    }
}
