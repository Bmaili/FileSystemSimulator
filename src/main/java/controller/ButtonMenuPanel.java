package controller;

import pojo.SuperBlock;
import service.FolderService;
import service.OsFileService;
import service.SuperBlockSercive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/*
 *文件操作页面下方的按钮组控件，描述其UI并绑定相关事件
 */
public class ButtonMenuPanel extends JPanel {

    ButtonMenuPanel() {
        uiInit();
        eventBind();
    }

    JButton writeFileBtn = new JButton("写入文件");
    JButton createFileBtn = new JButton("创建文件");
    JButton readFileBtn = new JButton("读取文件");
    JButton reNameFileBtn = new JButton("重命名目录/文件");
    JButton delteFileBtn = new JButton("删除目录/文件");
    JButton enterCaBtn = new JButton("进入目录");
    JButton createCaBtn = new JButton("创建目录");
    JButton backCaBtn = new JButton("返回上级目录");
    JButton backRootCaBtn = new JButton("返回根目录");
    JButton searchFileBtn = new JButton("当前目录查找文件");
    JButton logoutBtn = new JButton("用户登出");
    JButton authorityBtn = new JButton("该文件权限管理");
    JLabel userlabel = new JLabel();

    public void uiInit() {
        updataLabel();
        userlabel.setPreferredSize(new Dimension(300, 50));
        userlabel.setFont(new Font("Serif", 1, 30));
        Box info = Box.createHorizontalBox();
        info.add(userlabel);

        writeFileBtn.setToolTipText("需要w权限");
        readFileBtn.setToolTipText("需要r权限");
        reNameFileBtn.setToolTipText("需要w权限");
        delteFileBtn.setToolTipText("需要w权限");
        enterCaBtn.setToolTipText("需要x权限");

        writeFileBtn.setPreferredSize(new Dimension(160, 50));
        createFileBtn.setPreferredSize(new Dimension(160, 50));
        readFileBtn.setPreferredSize(new Dimension(160, 50));
        reNameFileBtn.setPreferredSize(new Dimension(160, 50));
        delteFileBtn.setPreferredSize(new Dimension(160, 50));
        enterCaBtn.setPreferredSize(new Dimension(160, 50));
        createCaBtn.setPreferredSize(new Dimension(160, 50));
        backCaBtn.setPreferredSize(new Dimension(160, 50));
        backRootCaBtn.setPreferredSize(new Dimension(160, 50));
        searchFileBtn.setPreferredSize(new Dimension(150, 50));
        logoutBtn.setPreferredSize(new Dimension(160, 50));
        authorityBtn.setPreferredSize(new Dimension(160, 50));
        writeFileBtn.setFont(new Font("Serif", 1, 19));
        createFileBtn.setFont(new Font("Serif", 1, 19));
        readFileBtn.setFont(new Font("Serif", 1, 19));
        reNameFileBtn.setFont(new Font("Serif", 1, 15));
        delteFileBtn.setFont(new Font("Serif", 1, 19));
        enterCaBtn.setFont(new Font("Serif", 1, 19));
        createCaBtn.setFont(new Font("Serif", 1, 19));
        backCaBtn.setFont(new Font("Serif", 1, 19));
        backRootCaBtn.setFont(new Font("Serif", 1, 19));
        searchFileBtn.setFont(new Font("Serif", 1, 12));
        logoutBtn.setFont(new Font("Serif", 1, 19));
        authorityBtn.setFont(new Font("Serif", 1, 17));

        JPanel box1 = new JPanel();
        box1.setPreferredSize(new Dimension(1100, 100));
        box1.add(writeFileBtn);
        box1.add(createFileBtn);
        box1.add(readFileBtn);
        box1.add(reNameFileBtn);
        box1.add(delteFileBtn);
        box1.add(authorityBtn);

        JPanel box2 = new JPanel();
        box2.add(enterCaBtn);
        box2.add(createCaBtn);
        box2.add(backCaBtn);
        box2.add(backRootCaBtn);
        box2.add(searchFileBtn);
        box2.add(logoutBtn);

        Box all = Box.createVerticalBox();
        all.add(info);
        all.add(Box.createVerticalStrut(10));
        all.add(box1);
        all.add(Box.createVerticalStrut(10));
        all.add(box2);
        this.add(all);
    }

    public void eventBind() {
        writeFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (FileWindow.selectFile.iNode.fileType != 0) {
                    JOptionPane.showMessageDialog(null, "该文件不是普通文件！");
                    return;
                }
                if (!OsFileService.authCheck(FileWindow.selectFile, FileWindow.userNow, 2)) {
                    JOptionPane.showMessageDialog(null, "您没有该文件的写入权限！");
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(null);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                File file = fileChooser.getSelectedFile();
                boolean w = OsFileService.fileToOsFile(FileWindow.selectFile, file.getAbsolutePath());
                if (w) {
                    FileWindow.fileProperties.updateData();
                    SuperBlockSercive.saveToDisk();
                    JOptionPane.showMessageDialog(null, "写入成功！");
                } else {
                    JOptionPane.showMessageDialog(null, "写入失败！");
                }
            }
        });
        createFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String inputValue = JOptionPane.showInputDialog("请输入新文件名称");
                OsFileService.createOsFile(inputValue,
                        FileWindow.userNow, 0, FileWindow.folderNow);
                FileWindow.fileListPanel.updateData();
                SuperBlockSercive.saveToDisk();
            }
        });
        delteFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!OsFileService.authCheck(FileWindow.selectFile, FileWindow.userNow, 2)) {
                    JOptionPane.showMessageDialog(null, "您没有该文件的删除权限！");
                    return;
                }
                OsFileService.deleteOsFile(FileWindow.selectFile, FileWindow.folderNow);
                FileWindow.selectFile = null;
                FileWindow.fileListPanel.updateData();
                SuperBlockSercive.saveToDisk();
            }
        });

        readFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (FileWindow.selectFile.iNode.fileType != 0) {
                    JOptionPane.showMessageDialog(null, "该文件不是普通文件！");
                    return;
                }
                if (!OsFileService.authCheck(FileWindow.selectFile, FileWindow.userNow, 4)) {
                    JOptionPane.showMessageDialog(null, "您没有该文件的读取权限！");
                    return;
                }
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(null);
                File file = fileChooser.getSelectedFile();
                OsFileService.osFileToFile(FileWindow.selectFile, file.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "读取成功！");
            }
        });

        reNameFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (!OsFileService.authCheck(FileWindow.selectFile, FileWindow.userNow, 2)) {
                    JOptionPane.showMessageDialog(null, "您没有该文件的重命名权限！");
                    return;
                }

                String inputValue = JOptionPane.showInputDialog("请输入新名称");
                OsFileService.changeName(FileWindow.selectFile, inputValue);
                FileWindow.fileListPanel.updateData();
                FileWindow.fileProperties.updateData();
                SuperBlockSercive.saveToDisk();
            }
        });

        enterCaBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (FileWindow.selectFile.iNode.fileType != 1) {
                    JOptionPane.showMessageDialog(null, "该文件不是目录文件！");
                    return;
                }
                if (!OsFileService.authCheck(FileWindow.selectFile, FileWindow.userNow, 1)) {
                    JOptionPane.showMessageDialog(null, "您没有该文件的执行权限！");
                    return;
                }
                FolderService.enterDirectory(FileWindow.selectFile);
                FileWindow.fileListPanel.updateData();
                FileWindow.fileProperties.updateData();
                updataLabel();
            }
        });

        createCaBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String inputValue = JOptionPane.showInputDialog("请输入新目录名称");
                OsFileService.createOsFile(inputValue,
                        FileWindow.userNow, 1, FileWindow.folderNow);
                FileWindow.fileListPanel.updateData();
                SuperBlockSercive.saveToDisk();
            }
        });

        backCaBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FolderService.backDirectory();
                FileWindow.fileListPanel.updateData();
                FileWindow.fileProperties.updateData();
                updataLabel();
            }
        });

        searchFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new SearchFileDialog().setVisible(true);
            }
        });
        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FileWindow.fileWindow.dispose();
                MainWindow.mainWIndow.setVisible(true);
            }
        });

        authorityBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (FileWindow.selectFile.iNode.owner != FileWindow.userNow) {
                    JOptionPane.showMessageDialog(null, "您不是该文件的所有者！无权操作！");
                    return;
                }
                new AuthorityDialog(FileWindow.selectFile).setVisible(true);
            }
        });
        backRootCaBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                FileWindow.folderNow = SuperBlock.superBlock.rootFile;
                FileWindow.fileListPanel.updateData();
                FileWindow.fileProperties.updateData();
                updataLabel();
            }
        });
    }

    private void updataLabel() {
        userlabel.setText("当前用户：" + FileWindow.userNow +
                "   当前目录：" +
                FileWindow.folderNow.iNode.path + FileWindow.folderNow.filename + "/");
    }
}
