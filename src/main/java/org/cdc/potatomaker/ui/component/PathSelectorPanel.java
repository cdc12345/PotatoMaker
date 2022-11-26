package org.cdc.potatomaker.ui.component;

import org.cdc.potatomaker.preference.PreferenceManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * e-mail: 3154934427@qq.com
 * 文件选择面板
 *
 * @author cdc123
 * @classname PathSelectorPanel
 * @date 2022/11/26 12:06
 */
public class PathSelectorPanel extends JPanel {
    public static File showSelector(Container parent,int fileSelectionMode,boolean enableMulti,File current){
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(fileSelectionMode);
        jFileChooser.setMultiSelectionEnabled(enableMulti);
        jFileChooser.setCurrentDirectory(current);
        if (jFileChooser.showDialog(parent,null) == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getCurrentDirectory();
        }
        return current;
    }

    private static ConcurrentHashMap<String, Set<String>> histories;

    JComboBox<String> filePath;
    JButton chooseFile;
    private final String historyName;
     public PathSelectorPanel(String historyName, int fileSelectionMode, boolean enableMulti, File current){
         histories = (ConcurrentHashMap<String, Set<String>>) PreferenceManager.getPreferences().get("hidden.historyPaths");
         if (histories == null) histories = new ConcurrentHashMap<>();
         this.historyName = historyName;

         setLayout(new BorderLayout());

         filePath = new JComboBox<>();
         filePath.setEditable(true);
         filePath.setSelectedItem(current.getPath());
         if (historyName != null) {
             filePath.setModel(new DefaultComboBoxModel<>() {
                 String[] sets;

                 @Override
                 public int getSize() {
                     sets = histories.get(historyName).toArray(new String[0]);
                     return sets.length;
                 }

                 @Override
                 public String getElementAt(int index) {
                     return sets[index];
                 }
             });
         }
         chooseFile = new JButton("...");
         chooseFile.addActionListener(a->{
             var currentFile = showSelector(PathSelectorPanel.this.getParent(),fileSelectionMode,enableMulti,current);
             if (historyName != null) {
                 if (!histories.containsKey(historyName)) histories.put(historyName, new HashSet<>());
                 histories.get(historyName).add(currentFile.getPath());
                 PreferenceManager.getPreferences().put("hidden.historyPaths",histories);
             }
             filePath.setSelectedItem(currentFile.getPath());
         });

         add(filePath,"Center");
         add(chooseFile,"East");

     }

     public File getCurrentFile(){
         return new File(Objects.requireNonNull(filePath.getSelectedItem()).toString());
     }

     public void setEnabled(boolean enabled){
         super.setEnabled(enabled);
         filePath.setEnabled(enabled);
         chooseFile.setEnabled(enabled);
     }
}
