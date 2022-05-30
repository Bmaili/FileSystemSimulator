package controller;

import pojo.*;

import javax.swing.*;
import java.awt.*;

/**
 * 文件系统操作的窗口,描述其UI
 */
public class FileWindow extends JFrame {
    public static User userNow; //当前登陆的用户
    public static Folder folderNow;//当前所处在的目录
    public static OsFile selectFile;//选择的文件（高亮显示的那个）
    public static FilePropertiesPanel fileProperties;//左边文件列表区域
    public static FileListPanel fileListPanel;//右边文件详情区域

    public static FileWindow fileWindow = null;

    public static void init() {
        fileWindow = new FileWindow();
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

        JLabel userlabel = new JLabel();
        userlabel.setText("当前用户：" + FileWindow.userNow +
                "   当前目录：" +
                FileWindow.folderNow.iNode.path + FileWindow.folderNow.filename + "/");
        JPanel panel = new JPanel();
        ButtonMenuPanel menu = new ButtonMenuPanel();
        panel.add(userlabel);
        panel.add(menu);
        contentPane.add(menu);
        fileWindow.add(contentPane);
        fileWindow.setVisible(true);
    }
}
