package controller;

import pojo.SuperBlock;
import pojo.User;
import service.SuperBlockSercive;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserLoginPanel extends JPanel {

    JButton loginBtn = new JButton("登录系统");
    JButton changePWBtn = new JButton("更改密码");
    JButton createUserBtn = new JButton("注册用户");
    JTextField field = new JTextField();
    JComboBox<User> comboBox = new JComboBox<>(SuperBlock.superBlock.userList.toArray(new User[]{}));

    UserLoginPanel() {
        uiInit();
        eventBind();
    }

    private void uiInit() {
        JLabel label = new JLabel("选择用户：");
        JLabel pwLabel = new JLabel("用户密码：");
        label.setPreferredSize(new Dimension(200, 80));
        comboBox.setPreferredSize(new Dimension(300, 60));
        pwLabel.setPreferredSize(new Dimension(200, 80));
        field.setPreferredSize(new Dimension(300, 60));
        loginBtn.setPreferredSize(new Dimension(400, 80));
        changePWBtn.setPreferredSize(new Dimension(400, 80));
        createUserBtn.setPreferredSize(new Dimension(400, 80));
        label.setFont(new Font("Serif", 1, 30));
        comboBox.setFont(new Font("Serif", 1, 30));
        pwLabel.setFont(new Font("Serif", 1, 30));
        field.setFont(new Font("Serif", 1, 30));
        loginBtn.setFont(new Font("Serif", 1, 30));
        changePWBtn.setFont(new Font("Serif", 1, 30));
        createUserBtn.setFont(new Font("Serif", 1, 30));
        Panel panel1 = new Panel();
        panel1.add(label);
        panel1.add(comboBox);
        Panel panel2 = new Panel();
        panel2.add(pwLabel);
        panel2.add(field);
        Panel panel3 = new Panel();
        panel3.add(loginBtn);
        Panel panel4 = new Panel();
        panel4.add(changePWBtn);
        Panel panel5 = new Panel();
        panel5.add(createUserBtn);
        Box box = Box.createVerticalBox();
        box.add(panel1);
        box.add(panel2);
        box.add(panel3);
        box.add(panel4);
        box.add(panel5);
        this.add(box);
        // 设置默认选中的条目
        comboBox.setSelectedIndex(0);
    }

    private void eventBind() {
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                User user = comboBox.getItemAt(comboBox.getSelectedIndex());
                if (user.password.equals(field.getText())) {
                    FileWindow.userNow = user;
                    FileWindow.folderNow = SuperBlock.superBlock.rootFile;
                    FileWindow.init();
                } else {
                    JOptionPane.showMessageDialog(null, "密码错误！");
                }
            }
        });
        changePWBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    String newPW = JOptionPane.showInputDialog("请输入该用户新密码");
                    User user = comboBox.getItemAt(comboBox.getSelectedIndex());
                    if (newPW == null || newPW.equals("")) {
                        JOptionPane.showMessageDialog(null, "更改失败！密码不能为空！");
                    } else {
                        user.password = newPW;
                        SuperBlockSercive.saveToDisk();
                        JOptionPane.showMessageDialog(null, "更改完成！");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
        createUserBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    String name = JOptionPane.showInputDialog("请输入新用户名，初始密码等于666");
                    if (UserService.createUser(name, "666")) {//初始密码等于666
                        SuperBlockSercive.saveToDisk();
                        JOptionPane.showMessageDialog(null, "创建完成！");
                    } else {
                        JOptionPane.showMessageDialog(null, "创建失败！用户名为空或重复！");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
    }


}
