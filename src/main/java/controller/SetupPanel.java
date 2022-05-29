package controller;

import service.SuperBlockSercive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SetupPanel extends JPanel {
    JButton changePWBtn = new JButton("更改管理员密码");
    JButton helpBtn = new JButton("使用说明");
    JButton formatBtn = new JButton("格式化模拟磁盘");
    JButton aboutBtn = new JButton("关于系统");
    JButton exitBtn = new JButton("退出系统");

    SetupPanel() {
        uiInit();
        eventBind();
    }

    private void uiInit() {
        changePWBtn.setPreferredSize(new Dimension(400, 80));
        formatBtn.setPreferredSize(new Dimension(400, 80));
        aboutBtn.setPreferredSize(new Dimension(400, 80));
        exitBtn.setPreferredSize(new Dimension(400, 80));
        helpBtn.setPreferredSize(new Dimension(400, 80));
        changePWBtn.setFont(new Font("Serif", 1, 30));
        formatBtn.setFont(new Font("Serif", 1, 30));
        aboutBtn.setFont(new Font("Serif", 1, 30));
        exitBtn.setFont(new Font("Serif", 1, 30));
        helpBtn.setFont(new Font("Serif", 1, 30));

        Panel panel1 = new Panel();
        panel1.add(changePWBtn);
        Panel panel2 = new Panel();
        panel2.add(formatBtn);
        Panel panel3 = new Panel();
        panel3.add(aboutBtn);
        Panel panel4 = new Panel();
        panel4.add(exitBtn);
        Panel panel5 = new Panel();
        panel5.add(helpBtn);
        Box box = Box.createVerticalBox();
        box.add(panel1);
        box.add(panel5);
        box.add(panel2);
        box.add(panel3);
        box.add(panel4);

        Panel panel0 = new Panel();
        JLabel label = new JLabel("文件系统模拟器");
        ImageIcon image = new ImageIcon(this.getClass().getClassLoader().getResource("icon/title.png"));
        label.setIcon(image);
        label.setPreferredSize(new Dimension(500, 100));
        label.setFont(new Font("Monospaced", 1, 45));
        panel0.add(label);
        this.add(panel0);
        this.add(box);
    }

    private void eventBind() {
        changePWBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    String pass = JOptionPane.showInputDialog("请输入新管理员密码");
                    if (pass != null && !pass.equals("")) {
                        SuperBlockSercive.saveSuperPass(pass);
                        JOptionPane.showMessageDialog(null, "修改成功！");
                    } else {
                        JOptionPane.showMessageDialog(null, "修改失败！");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
        formatBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    int opt = JOptionPane.showConfirmDialog(null,
                            "请确定您要格式化磁盘?", "确认信息",
                            JOptionPane.YES_NO_OPTION);
                    if (opt == JOptionPane.YES_OPTION) {
                        SuperBlockSercive.formatDisk();
                        JOptionPane.showMessageDialog(null, "格式化完成！重启生效！");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
        exitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });

        aboutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String s = "***文件系统模拟器***\n\n" +
                        "磁盘块大小：512byte\n\n" +
                        "磁盘块数量：1024\n\n" +
                        "模拟磁盘总大小：512kb\n\n" +
                        "作者：苏慧 刘凯 贲国庆\n\n" +
                        "项目地址：https://github.com/Bmaili/FileSystemSimulator";
                JOptionPane.showMessageDialog(null, s, "关于系统", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String s = "1. 管理员默认密码：root ，管理员密码保存于文件super_pass中。\n" +
                        "2. 用户默认密码：666。\n" +
                        "3. 功能：\n" +
                        "   - 多用户的相关操作\n" +
                        "   - 用户组管理\n" +
                        "   - 文件/目录增删改查等功能\n" +
                        "   - 针对于单一文件/目录的授权、鉴权";
                JOptionPane.showMessageDialog(null, s, "使用说明", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
