/*
 * Created by JFormDesigner on Fri May 08 15:06:33 CST 2020
 */

package window.vanish;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import net.miginfocom.swing.*;
import service.ClearService;
import service.io.FileService;
import util.ThreadExecute;
import window.common.components.ChooseFileComponent;
import window.common.error.ErrorWindow;
import window.common.info.InfoWindow;

/**
 * @author unknown
 */
public class VanishWindow extends JFrame {

    static VanishWindow vanishWindow;

    protected InfoWindow infoWindowForCloseTip;

    public InfoWindow getInfoWindowForCloseTip() {
        return infoWindowForCloseTip;
    }

    public synchronized static VanishWindow getVanishWindow() {
        if (vanishWindow == null) {
            vanishWindow = new VanishWindow();
            vanishWindow.setVisible(true);
            vanishWindow.getContentPane().setBackground(new Color(51, 51, 51));
        }
        return vanishWindow;
    }

    private VanishWindow() {
        initComponents();
    }

    private void delByIdButtonActionPerformed(ActionEvent e) {
        if (StrUtil.isBlank(userIdTextField.getText()) || !ReUtil.isMatch("^[\\d+,|\\d+]{1,}$", userIdTextField.getText())) {
            new ErrorWindow("userId输入错误， 请输入数字和‘,’").setVisible(true);
            return;
        }
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        List userIds = CollUtil.newArrayList(userIdTextField.getText().split(","));
        try {
            if (ClearService.delUseData(userIds)) {
                new InfoWindow("执行完毕");
            }
        } catch (Exception ex) {
            new ErrorWindow(ex.getMessage()).setVisible(true);
            ex.printStackTrace();
        } finally {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void delByFileButtonActionPerformed(ActionEvent e) {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        String excelFilePath = ChooseFileComponent.openJFileChooserForExcelFile();
        File file = new File(excelFilePath);
        if (!file.getName().contains("一机多号清除数据") && !file.getName().contains("僵尸粉清除数据")) {
            new ErrorWindow("文件选择错误").setVisible(true);
            return;
        }
        fileNamelabel.setText(excelFilePath);
        List userIds = FileService.readExcel(excelFilePath);
//            if (ClearService.delUseData(userIds)) {
//                new InfoWindow("执行完毕");
//            }
        try {
            ThreadExecute.createThreadForSqlByUserIds(userIds, 200);
            FileService.setSqlFileName();
            infoWindowForCloseTip = new InfoWindow("请勿关闭本程序，正在执行Sql...");
            infoWindowForCloseTip.setVisible(true);
        } catch (Exception ex) {
            new ErrorWindow(ex.getMessage()).setVisible(true);
            ex.printStackTrace();
        } finally {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        userIdTextField = new JTextField();
        delByIdButton = new JButton();
        fileNamelabel = new JLabel();
        delByFileButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("\u5220\u9664\u7528\u6237\u6570\u636e");
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
            "[]" +
            "[]" +
            "[]" +
            "[]"));
        contentPane.add(userIdTextField, "cell 4 5 19 3");

        //---- delByIdButton ----
        delByIdButton.setText("\u901a\u8fc7\u7528\u6237id-\u5220\u9664");
        delByIdButton.setFocusPainted(false);
        delByIdButton.addActionListener(e -> delByIdButtonActionPerformed(e));
        contentPane.add(delByIdButton, "cell 23 5 10 3");

        //---- fileNamelabel ----
        fileNamelabel.setText("\u6587\u4ef6\u8def\u5f84");
        fileNamelabel.setForeground(Color.white);
        contentPane.add(fileNamelabel, "cell 4 9 19 3");

        //---- delByFileButton ----
        delByFileButton.setText("\u901a\u8fc7\u5bfc\u5165\u6570\u636e-\u5220\u9664");
        delByFileButton.setFocusPainted(false);
        delByFileButton.addActionListener(e -> delByFileButtonActionPerformed(e));
        contentPane.add(delByFileButton, "cell 23 9 10 3");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTextField userIdTextField;
    private JButton delByIdButton;
    private JLabel fileNamelabel;
    private JButton delByFileButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

//    public static void main(String[] args) {
//        getVanishWindow();
//    }
}
