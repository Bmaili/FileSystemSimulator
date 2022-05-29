package controller;

import pojo.Folder;
import pojo.OsFile;
import service.FolderService;
import service.OsFileService;

import javax.swing.*;
import java.awt.*;

public class FilePropertiesPanel extends JTextArea {
    FilePropertiesPanel() {
        this.setEditable(false);
        Font font = new Font("Serif", 1, 20);
        this.setFont(font);
    }

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
