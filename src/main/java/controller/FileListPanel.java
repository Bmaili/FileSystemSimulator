package controller;

import pojo.OsFile;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;


public class FileListPanel extends JList {
    //列表的数据Model，用于列表数据的增删改
    private DefaultListModel<OsFile> pageModel = new DefaultListModel<>();

    FileListPanel() {
        setModel(pageModel);
        setCellRenderer(new ListCell());
    }

    public void updateData() {
        ArrayList<OsFile> list = FileWindow.folderNow.fileList;
        pageModel.clear();
        for (int i = 0; i < list.size(); i++) {
            pageModel.add(i, list.get(i));
        }
        // this.setSelectedIndex(0);
        // setModel(pageModel);    //为列表填充数据
    }
}

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
