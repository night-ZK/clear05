package service.check;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import javax.swing.*;

/**
 * @author zk
 */
public class MainWindowCheckService {
    public static boolean checkChooseAndInput(JCheckBox textField){
        if (textField.isSelected()) {
            if (StrUtil.isBlank(textField.getText()) || !NumberUtil.isInteger(textField.getText())) {
                throw new RuntimeException("tip: 填写错误， 请填写数字");
            }
        }
        return true;
    }
}
