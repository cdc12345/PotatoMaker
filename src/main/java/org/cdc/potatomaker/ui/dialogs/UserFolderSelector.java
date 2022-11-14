package org.cdc.potatomaker.ui.dialogs;

import org.cdc.potatomaker.exception.DefinedException;

import javax.swing.*;

/**
 * e-mail: 3154934427@qq.com
 * 用户目录选择对话框
 *
 * @author cdc123
 * @classname UserFolderSelector
 * @date 2022/11/13 22:05
 */
public class UserFolderSelector {

    public static String userFolder;

    public static void showUserFolderSelector() throws DefinedException {
        if (userFolder != null) throw new DefinedException("用户目录已经定义");
        JPanel folderSelect = new JPanel();

        JComboBox<String> userFolderComboBox = new JComboBox<>();


    }
}
