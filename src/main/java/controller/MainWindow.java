package controller;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public static void boot() {
        MainWindow mainWIndow = new MainWindow();
        // 创建选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();
        ClassLoader loader = mainWIndow.getClass().getClassLoader();
        Image image = new ImageIcon(loader.getResource("icon/user.png"))
                .getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        Image image2 = new ImageIcon(loader.getResource("icon/group.png"))
                .getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        Image image3 = new ImageIcon(loader.getResource("icon/set.png"))
                .getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT);
        tabbedPane.addTab("登录", new ImageIcon(image), new UserLoginPanel());
        tabbedPane.addTab("用户组", new ImageIcon(image2), new UserGroupPanel());
        tabbedPane.addTab("设置", new ImageIcon(image3), new SetupPanel());

        // 设置默认选中的选项卡
        tabbedPane.setSelectedIndex(0);

        //标题 图标
        ImageIcon imageIcon = new ImageIcon(mainWIndow.getClass().getClassLoader().getResource("icon/title.png"));
        mainWIndow.setIconImage(imageIcon.getImage());
        mainWIndow.setTitle("文件模拟系统");

        mainWIndow.setSize(800, 700);
        mainWIndow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWIndow.setLocationRelativeTo(null);
        mainWIndow.setContentPane(tabbedPane);
        mainWIndow.setVisible(true);
        mainWIndow.setResizable(false);
    }
}
