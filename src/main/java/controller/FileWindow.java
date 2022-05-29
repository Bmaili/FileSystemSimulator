package controller;

import pojo.*;

import javax.swing.*;
import java.awt.*;

public class FileWindow extends JFrame {
    public static User userNow;
    public static Folder folderNow;
    public static OsFile selectFile;
    public static FilePropertiesPanel fileProperties;
    public static FileListPanel fileListPanel;

    public static FileWindow fileWindow = new FileWindow();

    public static void init() {
        fileProperties = new FilePropertiesPanel();
        fileListPanel = new FileListPanel();

        JPanel contentPane = new JPanel();    //创建内容面板

        //标题 图标
        ImageIcon imageIcon = new ImageIcon(fileWindow.getClass().getClassLoader().getResource("icon/title.png"));
        fileWindow.setIconImage(imageIcon.getImage());
        fileWindow.setTitle("文件模拟系统");


        fileWindow.setSize(1200, 900);                       // 设置窗口大小
        fileWindow.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        fileWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）
        fileWindow.setResizable(false);
        fileListPanel.updateData();
        JScrollPane scrollPane = new JScrollPane();    //创建滚动面板
        scrollPane.setPreferredSize(new Dimension(580, 450));
        contentPane.add(scrollPane, BorderLayout.WEST);    //将面板增加到边界布局中央
        scrollPane.setViewportView(fileListPanel);    //在滚动面板中显示列表

        fileProperties.setPreferredSize(new Dimension(580, 450));
        contentPane.add(fileProperties, BorderLayout.EAST);

        ButtonMenuPanel menu = new ButtonMenuPanel();
        contentPane.add(menu);
        fileWindow.add(contentPane);
        fileWindow.setVisible(true);
    }
}
