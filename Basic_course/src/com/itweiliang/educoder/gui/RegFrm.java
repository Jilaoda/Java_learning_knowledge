package com.itweiliang.educoder.gui;

import com.itweiliang.educoder.gui.utils.DbUtil;
import com.itweiliang.educoder.gui.utils.ToolUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author liweiliang
 * @create 2023-04-09 15:10:20
 * @description 用户注册界面
 */
public class RegFrm extends JFrame {
    private JFrame jFrame;

    private JTextField textField_1;
    private JTextField textField_2;

    private JLabel usernameMes;
    private JLabel passwordMes;

    private JButton button;

    public RegFrm() {
        jFrame = new JFrame("注册");
        jFrame.setSize(400, 250);
        jFrame.setLocation(800, 200);
        jFrame.setVisible(true);

        JPanel jPanel_prompt = new JPanel();
        JPanel jPanel_username = new JPanel();
        JPanel jPanel_password = new JPanel();

        //1.设置提示标签
        JLabel label_prompt = new JLabel("你好,欢迎注册!");
        label_prompt.setForeground(Color.BLACK);
        label_prompt.setFont(new Font("幼圆", Font.BOLD, 30));
        jPanel_prompt.add(label_prompt);

        //2.创建账号行
        //①创建并设置组件账号字样
        JLabel label_1 = new JLabel("账号：");
        label_1.setForeground(Color.BLACK);
        label_1.setFont(new Font("幼园", Font.BOLD, 16));
        label_1.setBounds(120, 108, 65, 40);

        //②创建并设置组件密码输入框
        textField_1 = new JTextField();
        textField_1.setFont(new Font("Dialog", Font.BOLD, 14));
        textField_1.setToolTipText("");
        textField_1.setColumns(16);
        textField_1.setBounds(198, 114, 164, 30);

        usernameMes = new JLabel();
        usernameMes.setText("             ");
        textField_1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String user = textField_1.getText();
                if (ToolUtil.isEmpty(user)) {
                    usernameMes.setText("用户名不能为空");
                    usernameMes.setForeground(Color.RED);
                } else {
                    if (user.length() >= 3 && user.length() <= 11) {
                        usernameMes.setText("√              ");
                        usernameMes.setForeground(Color.GREEN);
                    } else {
                        JOptionPane.showMessageDialog(null, "账号需要在3-11位之间");
                        usernameMes.setText("");
                        textField_1.setText("");
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        jPanel_username.add(label_1);
        jPanel_username.add(textField_1);
        jPanel_username.add(usernameMes);

        //3.创建密码行
        //②创建并设置组件密码字样
        JLabel label_2 = new JLabel("密码：");
        label_2.setForeground(Color.BLACK);
        label_2.setFont(new Font("幼园", Font.BOLD, 16));
        label_2.setBounds(120, 108, 65, 40);

        //②创建并设置组件密码输入框
        textField_2 = new JTextField();
        textField_2.setFont(new Font("Dialog", Font.BOLD, 14));
        textField_2.setToolTipText("");
        textField_2.setColumns(16);
        textField_2.setBounds(198, 114, 164, 30);

        passwordMes = new JLabel();
        passwordMes.setText("                ");
        textField_1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                String pwd = textField_2.getText();
                if (ToolUtil.isEmpty(pwd)) {
                    passwordMes.setText("密码不能为空");
                    passwordMes.setForeground(Color.RED);
                } else {
                    boolean flag = pwd.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");
                    if (flag) {
                        passwordMes.setText("√              ");
                        passwordMes.setForeground(Color.GREEN);
                    } else {
                        JOptionPane.showMessageDialog(null, "密码需为6-16位数字和字母的组合");
                        passwordMes.setText("");
                        textField_2.setText("");
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        jPanel_password.add(label_2);
        jPanel_password.add(textField_2);
        jPanel_password.add(passwordMes);

        //3.设置注册按钮
        JPanel jPanel_register = new JPanel();
        button = new JButton("注册");
        button.addActionListener(e -> {
            String user = textField_1.getText();
            String pwd = textField_2.getText();
            if (ToolUtil.isEmpty(user) || ToolUtil.isEmpty(pwd)) {
                JOptionPane.showMessageDialog(null, "账号或密码不能为空");
            } else {
                if (user.length() >= 3 && user.length() <= 11) {
                    if (pwd.length() >= 6 && pwd.length() <= 16) {
                        DbUtil dbUtil = new DbUtil();
                        Connection connection;
                        try {
                            connection = dbUtil.getConnection();
                            String sql = "select * from register";
                            Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery(sql);
                            while (resultSet.next()){
                                String username = resultSet.getString("username");
                                if(username.equals(user)){
                                    JOptionPane.showMessageDialog(null, "账号已存在,请重新输入");
                                    textField_1.setText("");
                                    textField_2.setText("");
                                    break;
                                }else{
                                    String userName = textField_1.getText();
                                    String passWord = textField_2.getText();
                                    Connection connection1 = dbUtil.getConnection();
                                    Statement statement1 = connection1.createStatement();
                                    String sql1 = "insert into register values('" + userName + "','" + passWord + "')";
                                    PreparedStatement pstmt = connection1.prepareStatement(sql1);
                                    pstmt.executeUpdate();
                                    JOptionPane.showMessageDialog(null, "注册成功");
                                }
                            }

                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
        jPanel_register.add(button,BorderLayout.CENTER);

        //4.将提示、账号、密码、提交按钮添加仅jFrame中并显示
        JPanel jPanel_username_password = new JPanel();
        jPanel_username_password.add(jPanel_prompt,BorderLayout.CENTER);
        jPanel_username_password.add(jPanel_username,BorderLayout.CENTER);
        jPanel_username_password.add(jPanel_password,BorderLayout.CENTER);
        jPanel_username_password.add(jPanel_register,BorderLayout.CENTER);

        jFrame.getContentPane().add(jPanel_username_password);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        RegFrm regFrm = new RegFrm();
    }

}