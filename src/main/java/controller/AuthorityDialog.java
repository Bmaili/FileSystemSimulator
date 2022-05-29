package controller;

import pojo.OsFile;
import pojo.SuperBlock;
import pojo.User;
import pojo.UserGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthorityDialog extends JDialog {
    JButton userBtn = new JButton("设置用户权限");
    JTextField userField = new JTextField();
    JComboBox<User> userCombo = new JComboBox<>();
    JButton groupBtn = new JButton("设置用户组权限");
    JTextField groupField = new JTextField();
    JComboBox<UserGroup> groupCombo = new JComboBox<>();
    OsFile selectFile = null;

    User selectUser = null;
    UserGroup selectGroup = null;


    AuthorityDialog(OsFile osFile) {
        super(FileWindow.fileWindow, true);
        selectFile = osFile;
        // 对话框不可缩放
        // setResizable(false);
        setTitle("文件权限管理");
        setSize(700, 550);
        uiInit();
        dataInit();
        eventBind();
    }

    private void eventBind() {
        userCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectUser = (User) e.getItem();
                userField.setText(selectFile.iNode.userACL.getOrDefault(selectUser, 0).toString());
            }
        });
        groupCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectGroup = (UserGroup) e.getItem();
                groupField.setText(selectFile.iNode.groupACL.getOrDefault(selectGroup, 0).toString());
            }
        });
        userBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    int parseInt = Integer.parseInt(userField.getText());
                    if (parseInt >= 0 && parseInt <= 7 && selectUser != null) {
                        selectFile.iNode.userACL.put(selectUser, parseInt);
                        JOptionPane.showMessageDialog(null, "设置成功！");
                    } else {
                        JOptionPane.showMessageDialog(null, "权限数字必须在0~7！");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "权限数字必须在0~7！");
                }
            }
        });
        groupBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    int parseInt = Integer.parseInt(groupField.getText());
                    if (parseInt >= 0 && parseInt <= 7 && selectGroup != null) {
                        selectFile.iNode.groupACL.put(selectGroup, parseInt);
                        for (User user : selectGroup.members) {//为用户组里的每个用户都更新权限
                            Integer auth = selectFile.iNode.userACL.getOrDefault(user, 0);
                            auth |= parseInt;//或运算，能在用户原有基础上增加权限
                            selectFile.iNode.userACL.put(selectUser, auth);
                        }
                        JOptionPane.showMessageDialog(null, "设置成功！");
                    } else {
                        JOptionPane.showMessageDialog(null, "权限数字必须在0~7！");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "权限数字必须在0~7！");
                }
            }
        });
    }

    private void dataInit() {
        groupCombo.setModel(new DefaultComboBoxModel<>((SuperBlock.superBlock.userGroupList.toArray(new UserGroup[]{}))));
        userCombo.setModel(new DefaultComboBoxModel<>((SuperBlock.superBlock.userList.toArray(new User[]{}))));
        if (groupCombo.getItemCount() > 0) {
            selectGroup = groupCombo.getItemAt(0);
            groupCombo.setSelectedIndex(0);
            groupField.setText(selectFile.iNode.groupACL.getOrDefault(selectGroup, 0).toString());
        }
        if (userCombo.getItemCount() > 0) {
            selectUser = userCombo.getItemAt(0);
            userCombo.setSelectedIndex(0);
            userField.setText(selectFile.iNode.userACL.getOrDefault(selectUser, 0).toString());
        }
    }

    private void uiInit() {
        JLabel userLabel = new JLabel("选择用户:");
        userLabel.setPreferredSize(new Dimension(200, 60));
        userCombo.setPreferredSize(new Dimension(300, 60));
        Panel panel1 = new Panel();
        panel1.add(userLabel);
        panel1.add(userCombo);

        JLabel userAuthLabel = new JLabel("该用户文件权限:");
        userAuthLabel.setPreferredSize(new Dimension(200, 60));
        userField.setPreferredSize(new Dimension(300, 60));
        Panel panel2 = new Panel();
        panel2.add(userAuthLabel);
        panel2.add(userField);

        userBtn.setPreferredSize(new Dimension(400, 50));
        Panel panel3 = new Panel();
        panel3.add(userBtn);

        JLabel groupLabel = new JLabel("选择用户组:");
        groupLabel.setPreferredSize(new Dimension(200, 60));
        groupCombo.setPreferredSize(new Dimension(300, 60));
        Panel panel4 = new Panel();
        panel4.add(groupLabel);
        panel4.add(groupCombo);

        JLabel groupAuthLabel = new JLabel("该用户组文件权限:");
        groupAuthLabel.setPreferredSize(new Dimension(200, 60));
        groupField.setPreferredSize(new Dimension(300, 60));
        Panel panel5 = new Panel();
        panel5.add(groupAuthLabel);
        panel5.add(groupField);

        groupBtn.setPreferredSize(new Dimension(400, 50));
        Panel panel6 = new Panel();
        panel6.add(groupBtn);

        userBtn.setFont(new Font("Serif", 1, 20));
        userField.setFont(new Font("Serif", 1, 20));
        userCombo.setFont(new Font("Serif", 1, 20));
        groupBtn.setFont(new Font("Serif", 1, 20));
        groupField.setFont(new Font("Serif", 1, 20));
        groupCombo.setFont(new Font("Serif", 1, 20));
        userLabel.setFont(new Font("Serif", 1, 20));
        userAuthLabel.setFont(new Font("Serif", 1, 20));
        groupLabel.setFont(new Font("Serif", 1, 20));
        groupAuthLabel.setFont(new Font("Serif", 1, 20));

        JLabel msg = new JLabel(selectFile.iNode.path + selectFile.filename);
        msg.setPreferredSize(new Dimension(600, 60));
        msg.setFont(new Font("Serif", 1, 20));


        JPanel panel = new JPanel();
        panel.add(msg);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel3);
        panel.add(panel4);
        panel.add(panel5);
        panel.add(panel6);
        this.add(panel);
    }


}
