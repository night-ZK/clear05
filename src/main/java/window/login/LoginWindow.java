/*
 * Created by JFormDesigner on Fri May 08 13:12:06 CST 2020
 */

package window.login;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import cn.hutool.core.util.StrUtil;
import net.miginfocom.swing.*;
import service.LoginService;
import window.MainWindow;
import window.common.error.ErrorWindow;

/**
 * @author unknown
 */
public class LoginWindow extends JFrame {
    static LoginWindow loginWindow;

    public static synchronized LoginWindow getLoginWindow() {
        if (loginWindow == null) {
            loginWindow = new LoginWindow();
            loginWindow.getContentPane().setBackground(new Color(51, 51, 51));
            loginWindow.setVisible(true);
        }
        return loginWindow;
    }

    private LoginWindow() {
        initComponents();
    }

    private void loginButtonActionPerformed(ActionEvent e) {
        String pas = new String(passwordField.getPassword());
        if (StrUtil.isAllBlank(accountTextField.getText(), pas)) {
            new ErrorWindow("请输入账号名和密码").setVisible(true);
            return;
        }
        try {
            if (LoginService.login(accountTextField.getText(), pas)) {
                getLoginWindow().dispose();
                MainWindow.createInstance();
            }
        } catch (Exception ex) {
            new ErrorWindow(ex.getMessage()).setVisible(true);
            ex.printStackTrace();
        }
    }

    private void passwordFieldKeyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            loginButtonActionPerformed(null);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        accountTextField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("clear05 login");
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));
        contentPane.add(accountTextField, "cell 8 4 25 4");

        //---- passwordField ----
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                passwordFieldKeyPressed(e);
            }
        });
        contentPane.add(passwordField, "cell 8 8 25 4");

        //---- loginButton ----
        loginButton.setText("\u767b\u5165");
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> loginButtonActionPerformed(e));
        contentPane.add(loginButton, "cell 16 13 6 2");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTextField accountTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        getLoginWindow();
    }
}
