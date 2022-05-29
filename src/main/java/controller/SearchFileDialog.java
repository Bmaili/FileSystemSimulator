package controller;

import service.OsFileService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SearchFileDialog extends JDialog {

    SearchFileDialog() {
        super(FileWindow.fileWindow, true);
        // 对话框不可缩放
        setResizable(false);
        setTitle("文件搜索");
        setSize(700, 550);
        uiInit();
    }

    private void uiInit() {
        JButton searchBtn = new JButton("查找");
        JTextField textField = new JTextField();
        JTextArea resault = new JTextArea();
        textField.setPreferredSize(new Dimension(600, 40));
        searchBtn.setPreferredSize(new Dimension(600, 40));
        resault.setPreferredSize(new Dimension(600, 400));

        textField.setFont(new Font("Serif", 1, 20));
        searchBtn.setFont(new Font("Serif", 1, 20));
        resault.setFont(new Font("Serif", 1, 20));
        textField.setToolTipText("请输入文件名");
        JPanel panel = new JPanel();
        this.add(panel);
        panel.add(textField);
        panel.add(searchBtn);
        panel.add(resault);
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String text = textField.getText();
                ArrayList<String> res = OsFileService.searchOsFile(FileWindow.folderNow, text);
                resault.setText(searchResToString(res));
            }
        });
    }

    private String searchResToString(ArrayList<String> res) {
        if (res == null || res.size() == 0) {
            return "该目录下没有此文件哦！";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : res) {
            sb.append(str + "\n");
        }
        return sb.toString();
    }
}
