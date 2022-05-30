package controller;

import pojo.SuperBlock;
import pojo.User;
import pojo.UserGroup;
import service.SuperBlockSercive;
import service.UserGroupService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 用户组界面，描述其UI并绑定相关事件
 */
public class UserGroupPanel extends JPanel {
    JButton createGroupBtn = new JButton("新建用户组");
    JButton delGroupBtn = new JButton("删除该用户组");
    JButton addUserBtn = new JButton("添加用户到该组");
    JButton delUserBtn = new JButton("从该组删除该用户");
    JComboBox<UserGroup> groupCombo = new JComboBox<>();
    JComboBox<User> userCombo = new JComboBox<>();

    UserGroupPanel() {
        updataComboBox();
        uiInit();
        eventBind();
    }

    private void updataComboBox() {
        groupCombo.setModel(new DefaultComboBoxModel<>((SuperBlock.superBlock.userGroupList.toArray(new UserGroup[]{}))));
        UserGroup item = (UserGroup) groupCombo.getSelectedItem();
        User[] array = item.members.toArray(new User[]{});
        userCombo.setModel(new DefaultComboBoxModel<>(array));
    }

    private void uiInit() {
        JLabel groupLabel = new JLabel("用户组：");
        JLabel userLabel = new JLabel("该组用户：");

        groupLabel.setPreferredSize(new Dimension(200, 80));
        groupCombo.setPreferredSize(new Dimension(300, 60));
        userLabel.setPreferredSize(new Dimension(200, 80));
        userCombo.setPreferredSize(new Dimension(300, 60));
        createGroupBtn.setPreferredSize(new Dimension(400, 80));
        delGroupBtn.setPreferredSize(new Dimension(400, 80));
        addUserBtn.setPreferredSize(new Dimension(400, 80));
        delUserBtn.setPreferredSize(new Dimension(400, 80));

        groupLabel.setFont(new Font("Serif", 1, 30));
        groupCombo.setFont(new Font("Serif", 1, 30));
        userLabel.setFont(new Font("Serif", 1, 30));
        userCombo.setFont(new Font("Serif", 1, 30));
        createGroupBtn.setFont(new Font("Serif", 1, 30));
        delGroupBtn.setFont(new Font("Serif", 1, 30));
        addUserBtn.setFont(new Font("Serif", 1, 30));
        delUserBtn.setFont(new Font("Serif", 1, 30));

        Panel panel1 = new Panel();
        panel1.add(groupLabel);
        panel1.add(groupCombo);
        Panel panel2 = new Panel();
        panel2.add(userLabel);
        panel2.add(userCombo);
        Panel panel3 = new Panel();
        panel3.add(createGroupBtn);
        Panel panel4 = new Panel();
        panel4.add(delGroupBtn);
        Panel panel5 = new Panel();
        panel5.add(addUserBtn);
        Panel panel6 = new Panel();
        panel6.add(delUserBtn);
        Box box = Box.createVerticalBox();
        box.add(panel1);
        box.add(panel2);
        box.add(panel3);
        box.add(panel4);
        box.add(panel5);
        box.add(panel6);
        this.add(box);

    }

    private void eventBind() {
        groupCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                UserGroup item = (UserGroup) e.getItem();
                User[] array = item.members.toArray(new User[]{});
                userCombo.setModel(new DefaultComboBoxModel<>(array));
            }

        });
        createGroupBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    String name = JOptionPane.showInputDialog("请输入新用户组名");
                    if (UserGroupService.createGroup(name)) {
                        updataComboBox();
                        SuperBlockSercive.saveToDisk();
                        JOptionPane.showMessageDialog(null, "创建成功！");
                    } else {
                        JOptionPane.showMessageDialog(null, "创建失败！用户组名为空或重复！");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
        delGroupBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    UserGroup item = (UserGroup) groupCombo.getSelectedItem();
                    UserGroupService.delGroup(item);
                    updataComboBox();
                    SuperBlockSercive.saveToDisk();
                    JOptionPane.showMessageDialog(null, "删除完成！");
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
        addUserBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    UserGroup userGroup = (UserGroup) groupCombo.getSelectedItem();
                    User[] selectionValues = SuperBlock.superBlock.userList.toArray(new User[]{});
                    User inputContent = (User) JOptionPane.showInputDialog(
                            null,
                            "选择一项: ",
                            "添加用户到用户组",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            selectionValues,
                            selectionValues[0]
                    );
                    UserGroupService.addUserToGroup(userGroup, inputContent);
                    updataComboBox();
                    SuperBlockSercive.saveToDisk();
                    JOptionPane.showMessageDialog(null, "添加完成！");
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
        delUserBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String superPW = JOptionPane.showInputDialog("请先输入管理员密码");
                if (SuperBlockSercive.superPass.equals(superPW)) {
                    UserGroup userGroup = (UserGroup) groupCombo.getSelectedItem();
                    User user = (User) userCombo.getSelectedItem();
                    UserGroupService.delUserFromGroup(userGroup, user);
                    updataComboBox();
                    SuperBlockSercive.saveToDisk();
                    JOptionPane.showMessageDialog(null, "删除完成！");
                } else {
                    JOptionPane.showMessageDialog(null, "管理员密码错误！");
                }
            }
        });
    }
}
