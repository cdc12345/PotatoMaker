package org.cdc.potatomaker.ui.dialogs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.util.locale.GlobalUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;

/**
 * e-mail: 3154934427@qq.com
 * 用户目录选择对话框
 *
 * @author cdc123
 * @classname UserFolderSelector
 * @date 2022/11/13 22:05
 */
public class UserFolderSelector extends JDialog{

    private static final Logger LOG = LogManager.getLogger("UserFolderSelector");

    private static final Object lock = new Object();

    public String userFolder;

    public static final String notAskKey = "userFolderSelectorDialog.notAsk";

    private static UserFolderSelector instance;

    public static UserFolderSelector getInstance() {
        if (instance == null) instance = new UserFolderSelector();
        return instance;
    }

    private UserFolderSelector() {
        var folder = PreferenceManager.getPreferences().get("userFolder");
        if (folder != null)
            userFolder = folder.toString();
    }

    public static void showUserFolderSelector() {
        var di = getInstance();
        di.initUserFolderSelector();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PreferenceManager.getPreferences().put("userFolder",di.userFolder);
    }

    private void initUserFolderSelector(){
        setTitle("请选择用户目录");
        setLayout(new BorderLayout());
        JPanel folderSelect = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JComboBox<String> userFolderComboBox = new JComboBox<>();
        userFolderComboBox.setEditable(true);
        if (userFolder != null) userFolderComboBox.setSelectedItem(userFolderComboBox);

        String defaultUserFolder = System.getProperty("user.home")+"\\"+".pm";
        String historyKey = "userFolder.history";
        if (!PreferenceManager.getPreferences().containsKey(historyKey)||!
                (PreferenceManager.getPreferences().get(historyKey) instanceof UserFolderHistory)){
            UserFolderHistory his = new UserFolderHistory();
            his.add(defaultUserFolder);
            PreferenceManager.getPreferences().put(historyKey,his);
        }
        UserFolderHistory history = (UserFolderHistory) PreferenceManager.getPreferences().get(historyKey);
        userFolderComboBox.setModel(new DefaultComboBoxModel<>(){
            @Override
            public int getSize(){
                return history.toArray().length;
            }
            @Override
            public String getElementAt(int index) {
                return history.toArray()[index].toString();
            }
        });
        if (userFolderComboBox.getSelectedItem() == null) userFolderComboBox.setSelectedIndex(0);

        JButton selectFolder = new JButton("...");
        selectFolder.addActionListener(a->{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setCurrentDirectory(new File(
                    Objects.requireNonNullElse(userFolderComboBox.getSelectedItem(),defaultUserFolder).toString()));
            int state = fileChooser.showDialog(null,"ok");
            if (state == JFileChooser.APPROVE_OPTION){
                userFolderComboBox.setSelectedItem(fileChooser.getSelectedFile().toString());
            }
        });


        folderSelect.add(userFolderComboBox);
        folderSelect.add(selectFolder);

        JPanel controlPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton ok = new JButton("ok");
        ok.addActionListener(a->{
            userFolder = Objects.requireNonNullElse(userFolderComboBox.getSelectedItem(),defaultUserFolder).toString();
            setVisible(false);
            notifyThread();
        });
        JButton cancel = new JButton("cancel");
        cancel.addActionListener(a->{
            userFolder = defaultUserFolder;
            setVisible(false);
            notifyThread();
        });
        JCheckBox notAsk = new JCheckBox("do not ask again");
        notAsk.addActionListener(a->{
            PreferenceManager.getPreferences().put(notAskKey,notAsk.isSelected());
        });

        controlPane.add(notAsk);
        controlPane.add(ok);
        controlPane.add(cancel);

        add(controlPane,"South");
        add(folderSelect,"Center");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void notifyThread(){
        new Thread(()->{
            synchronized (lock){
                lock.notifyAll();
            }
        }).start();
    }

    private static class UserFolderHistory extends HashSet<String>{};
}
