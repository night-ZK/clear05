package window.common.components;

import javax.swing.*;
import java.io.File;

/**
 * @author zk
 */
public class ChooseFileComponent {
    public static String openJFileChooserForDir() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showDialog(new JLabel(), "选择数据备份位置");
        File writeFile = jfc.getSelectedFile();
        if (writeFile == null) {
            throw new RuntimeException("未选择文件夹");
        }
        if (!writeFile.isDirectory()) {
            throw new RuntimeException("请选择一个文件夹");
        }
        return writeFile.getPath();
    }

    public static String openJFileChooserForExcelFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showDialog(new JLabel(), "选择数据文件");
        File readFile = jfc.getSelectedFile();
        if (readFile == null) {
            throw new RuntimeException("未选择文件");
        }
        if (!readFile.isFile()) {
            throw new RuntimeException("请选择一个excel文件");
        }
        String readFilePath =  readFile.getPath();
        String type = readFilePath.substring(readFilePath.lastIndexOf("."));
        if (!type.equals(".xlsx")) {
            throw new RuntimeException("请选择一个.xlsx文件");
        }
        return readFilePath;
    }
}
