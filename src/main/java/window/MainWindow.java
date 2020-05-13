/*
 * Created by JFormDesigner on Wed May 06 13:44:46 CST 2020
 */

package window;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import net.miginfocom.swing.*;
import service.ClearService;
import service.io.FileService;
import window.common.components.ChooseFileComponent;
import window.common.error.ErrorWindow;
import window.common.info.InfoWindow;
import window.vanish.VanishWindow;

/**
 * @author unknown
 */
public class MainWindow extends JFrame {

    static String _outDayFileKeyName = "僵尸粉清除数据";

    static String _multipleFileKeyName = "一机多号清除数据";

    static String _partnerFileKeyName = "合伙人清除数据";

    static volatile MainWindow instance;

    public static MainWindow createInstance(){
        if (instance == null) {
            synchronized (MainWindow.class) {
                if (instance == null) {
                    instance = new MainWindow();
                    instance.getContentPane().setBackground(new Color(51, 51, 51));
                    instance.setVisible(true);
                }
            }
        }
        return instance;
    }

    private MainWindow() {
        initComponents();
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        System.exit(0);
    }

    private void clearChooseButton1ActionPerformed(ActionEvent e) {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Map writeDataMap = exportFile();
        List closeDataList = CollUtil.newArrayList();
        if (writeDataMap.containsKey(_outDayFileKeyName)) {
            closeDataList.addAll((List) writeDataMap.get(_outDayFileKeyName));
        }
        if (writeDataMap.containsKey(_multipleFileKeyName)) {
            closeDataList.addAll((List) writeDataMap.get(_multipleFileKeyName));
        }
        try {
            if (closeDataList.size() > 0) {
                List sqlData = (List) closeDataList.stream().distinct().collect(Collectors.toList());
                ClearService.clearData(sqlData);
            }
            if (writeDataMap.containsKey(_partnerFileKeyName)) {
                ClearService.cancelPartner((List) writeDataMap.get(_partnerFileKeyName));
            }
        } catch (Exception ex) {
            new ErrorWindow(ex.getMessage()).setVisible(true);
            ex.printStackTrace();
        } finally {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void exportButtonActionPerformed(ActionEvent e) {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        exportFile();
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private Map exportFile() {
        Map writeDateMap = new HashMap(6);
        Long time = System.currentTimeMillis();
        try {
            checkChooseAndInput();
            String filePath = ChooseFileComponent.openJFileChooserForDir();
            if (dayClearCheckBox.isSelected()) {
                List<List> writeData = ClearService.selectOutDayData(Integer.valueOf(dayTextField.getText()));
                FileService.writeExcel(filePath + "/" + time + "/" + _outDayFileKeyName + ".xlsx", writeData, _outDayFileKeyName);
                new InfoWindow("僵尸粉清除数据-保存成功").setVisible(true);
                writeData.remove(0);
                writeDateMap.put(_outDayFileKeyName, writeData.stream().map(data -> data.get(0)).collect(Collectors.toList()));
            }
            if (multipleCheckBox.isSelected()) {
                List<List> writeData = ClearService.selectMultipleData(Integer.valueOf(multipleTextField.getText()));
                FileService.writeExcel(filePath + "/" + time + "/" + _multipleFileKeyName + ".xlsx", writeData, _multipleFileKeyName);
                new InfoWindow("一机多号清除数据-保存成功").setVisible(true);
                writeData.remove(0);
                writeDateMap.put(_multipleFileKeyName, writeData.stream().map(date -> date.get(0)).collect(Collectors.toList()));
            }
            if (partnerCheckBox.isSelected()) {
                List<List> writeData = ClearService.selectPartnerData(Double.valueOf(partnerTextField.getText()));
                FileService.writeExcel(filePath + "/" + time + "/" + _partnerFileKeyName + ".xlsx", writeData, _partnerFileKeyName);
                new InfoWindow("合伙人清除数据-保存成功").setVisible(true);
                writeData.remove(0);
                writeDateMap.put(_partnerFileKeyName, writeData.stream().map(date -> date.get(0)).collect(Collectors.toList()));
            }
        } catch (Exception ex) {
            new ErrorWindow(ex.getMessage()).setVisible(true);
            ex.printStackTrace();
        }
        return writeDateMap;
    }

    private void recoverButtonActionPerformed(ActionEvent e) {
        try {
            String readFilePath = ChooseFileComponent.openJFileChooserForExcelFile();
            String fileName = readFilePath.substring(readFilePath.lastIndexOf("\\") + 1, readFilePath.lastIndexOf("."));
            if (fileName.equals(_outDayFileKeyName) || fileName.equals(_multipleFileKeyName)) {
                List readData = FileService.readExcel(readFilePath);
                ClearService.recoverData(readData);
            }
            if (fileName.equals(_partnerFileKeyName)) {
                List readData = FileService.readPartnerExcel(readFilePath);
                ClearService.recoverPartner(readData);
            }
            new InfoWindow("恢复完成").setVisible(true);
        } catch (Exception ex) {
            new ErrorWindow(ex.getMessage()).setVisible(true);
            ex.printStackTrace();
        }
    }

    private void clearAllButtonActionPerformed(ActionEvent e) {
        dayClearCheckBox.setSelected(true);
        multipleCheckBox.setSelected(true);
        partnerCheckBox.setSelected(true);
        clearChooseButton1ActionPerformed(e);
    }

    private void vanishButtonActionPerformed(ActionEvent e) {
        VanishWindow.getVanishWindow().setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        closeButton = new JButton();
        clearAllButton = new JButton();
        dayClearCheckBox = new JCheckBox();
        dayTextField = new JTextField();
        multipleCheckBox = new JCheckBox();
        multipleTextField = new JTextField();
        partnerCheckBox = new JCheckBox();
        partnerTextField = new JTextField();
        exportButton = new JButton();
        clearChooseButton = new JButton();
        recoverButton = new JButton();
        vanishButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(new Color(51, 51, 51));
        setTitle("clear05 --by zk");
        setUndecorated(true);
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

        //---- closeButton ----
        closeButton.setText("\u5173\u95ed");
        closeButton.setBackground(new Color(102, 204, 255));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> closeButtonActionPerformed(e));
        contentPane.add(closeButton, "cell 38 0 1 3");

        //---- clearAllButton ----
        clearAllButton.setText("\u4e00\u952e\u6e05\u9664");
        clearAllButton.setFocusPainted(false);
        clearAllButton.addActionListener(e -> clearAllButtonActionPerformed(e));
        contentPane.add(clearAllButton, "cell 10 7 18 1");

        //---- dayClearCheckBox ----
        dayClearCheckBox.setText("\u50f5\u5c38\u7c89\u6e05\u9664");
        dayClearCheckBox.setOpaque(false);
        dayClearCheckBox.setForeground(Color.white);
        contentPane.add(dayClearCheckBox, "cell 10 11 4 3");

        //---- dayTextField ----
        dayTextField.setText("15");
        contentPane.add(dayTextField, "cell 17 10 11 4");

        //---- multipleCheckBox ----
        multipleCheckBox.setText("\u4e00\u673a\u591a\u53f7\u6e05\u9664");
        multipleCheckBox.setForeground(Color.white);
        multipleCheckBox.setOpaque(false);
        contentPane.add(multipleCheckBox, "cell 10 16 7 3");

        //---- multipleTextField ----
        multipleTextField.setText("3");
        contentPane.add(multipleTextField, "cell 17 15 11 4");

        //---- partnerCheckBox ----
        partnerCheckBox.setText("\u5408\u4f19\u4eba\u6e05\u9664");
        partnerCheckBox.setOpaque(false);
        partnerCheckBox.setForeground(Color.white);
        contentPane.add(partnerCheckBox, "cell 10 21 7 4");

        //---- partnerTextField ----
        partnerTextField.setText("348");
        contentPane.add(partnerTextField, "cell 17 21 11 3");

        //---- exportButton ----
        exportButton.setText("\u5bfc\u51fa\u52fe\u9009\u6570\u636e");
        exportButton.setPreferredSize(new Dimension(82, 30));
        exportButton.setFocusPainted(false);
        exportButton.setMaximumSize(null);
        exportButton.setMinimumSize(null);
        exportButton.addActionListener(e -> exportButtonActionPerformed(e));
        contentPane.add(exportButton, "cell 10 30 18 1");

        //---- clearChooseButton ----
        clearChooseButton.setText("\u52fe\u9009\u6e05\u9664");
        clearChooseButton.setForeground(new Color(51, 51, 51));
        clearChooseButton.setFocusPainted(false);
        clearChooseButton.addActionListener(e -> clearChooseButton1ActionPerformed(e));
        contentPane.add(clearChooseButton, "cell 10 35 18 1");

        //---- recoverButton ----
        recoverButton.setText("\u6062\u590d\u6570\u636e");
        recoverButton.setForeground(new Color(51, 51, 51));
        recoverButton.setFocusPainted(false);
        recoverButton.addActionListener(e -> recoverButtonActionPerformed(e));
        contentPane.add(recoverButton, "cell 10 40 18 1");

        //---- vanishButton ----
        vanishButton.setText("\u5220\u9664\u7528\u6237\u6570\u636e");
        vanishButton.setFocusPainted(false);
        vanishButton.addActionListener(e -> vanishButtonActionPerformed(e));
        contentPane.add(vanishButton, "cell 10 44 18 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JButton closeButton;
    private JButton clearAllButton;
    private JCheckBox dayClearCheckBox;
    private JTextField dayTextField;
    private JCheckBox multipleCheckBox;
    private JTextField multipleTextField;
    private JCheckBox partnerCheckBox;
    private JTextField partnerTextField;
    private JButton exportButton;
    private JButton clearChooseButton;
    private JButton recoverButton;
    private JButton vanishButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

//    public static void main(String[] args) {
//        MainWindow mainFrame = new MainWindow();
//        mainFrame.getContentPane().setBackground(new Color(51, 51, 51));
//        mainFrame.setVisible(true);
//    }

    public boolean checkChooseAndInput() {
        if (dayClearCheckBox.isSelected()) {
            if (StrUtil.isBlank(dayTextField.getText()) || !NumberUtil.isInteger(dayTextField.getText())) {
                throw new RuntimeException("tip: 僵尸粉检测天数填写错误， 请填写数字");
            }
        }
        if (multipleCheckBox.isSelected()) {
            if (StrUtil.isBlank(multipleTextField.getText()) || !NumberUtil.isInteger(multipleTextField.getText())) {
                throw new RuntimeException("tip: 一机多号检测数量填写错误， 请填写数字");
            }
        }
        if (partnerCheckBox.isSelected()) {
            if (StrUtil.isBlank(partnerTextField.getText()) || !NumberUtil.isNumber(partnerTextField.getText())) {
                throw new RuntimeException("tip: 合伙人检测基础贡献值填写错误， 请填写数字");
            }
        }
        return true;
    }
}
