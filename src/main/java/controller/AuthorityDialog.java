package controller;

import pojo.OsFile;
import pojo.SuperBlock;
import pojo.User;
import pojo.UserGroup;
import service.SuperBlockSercive;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 文件权限管理模块的弹窗，描述其UI并绑定相关事件
 */
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
        setResizable(false);
        setTitle("文件权限管理");
        setSize(1000, 260);
        uiInit();
        dataInit();
        eventBind();
    }

    //事件绑定
    private void eventBind() {

        //用户选项发生变化时，展示该用户的权限数字
        userCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectUser = (User) e.getItem();
                userField.setText(selectFile.iNode.userACL.getOrDefault(selectUser, 0).toString());
            }
        });

        //用户组选项发生变化时，展示该用户组的权限数字
        groupCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                selectGroup = (UserGroup) e.getItem();
                groupField.setText(selectFile.iNode.groupACL.getOrDefault(selectGroup, 0).toString());
            }
        });

        //按下授权用户按钮时，对该用户授权
        userBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    int parseInt = Integer.parseInt(userField.getText());
                    //输入的数字必须是0-7
                    if (parseInt >= 0 && parseInt <= 7 && selectUser != null) {
                        selectFile.iNode.userACL.put(selectUser, parseInt);
                        FileWindow.fileProperties.updateData();//更新文件详情板
                        SuperBlockSercive.saveToDisk();//保存到磁盘
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
                        selectGroup.memberOfACL.add(selectFile);
                        for (User user : selectGroup.members) {//为用户组里的每个用户都更新权限
                            selectFile.iNode.userACL.put(user, parseInt);
                        }
                        FileWindow.fileProperties.updateData();
                        SuperBlockSercive.saveToDisk();
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
        userLabel.setPreferredSize(new Dimension(120, 60));
        userCombo.setPreferredSize(new Dimension(300, 60));
        Panel panel1 = new Panel();
        panel1.add(userLabel);
        panel1.add(userCombo);

        JLabel groupLabel = new JLabel("选择用户组:");
        groupLabel.setPreferredSize(new Dimension(130, 60));
        groupCombo.setPreferredSize(new Dimension(300, 60));
        panel1.add(groupLabel);
        panel1.add(groupCombo);

        JLabel userAuthLabel = new JLabel("该用户权限:");
        userAuthLabel.setPreferredSize(new Dimension(130, 60));
        userField.setPreferredSize(new Dimension(50, 60));
        userBtn.setPreferredSize(new Dimension(200, 60));
        Panel panel2 = new Panel();
        panel2.add(userAuthLabel);
        panel2.add(userField);
        panel2.add(userBtn);

        JLabel groupAuthLabel = new JLabel("该用户组权限:");
        groupAuthLabel.setPreferredSize(new Dimension(140, 60));
        groupField.setPreferredSize(new Dimension(50, 60));
        groupBtn.setPreferredSize(new Dimension(200, 50));
        panel2.add(groupAuthLabel);
        panel2.add(groupField);
        panel2.add(groupBtn);

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

        JLabel msg = new JLabel("已选择文件：" + selectFile.iNode.path + selectFile.filename);
        msg.setPreferredSize(new Dimension(600, 60));
        msg.setFont(new Font("Serif", 1, 20));

        JPanel panel = new JPanel();
        panel.add(msg);
        panel.add(panel2);
        panel.add(panel1);
        this.add(panel);
    }
}
