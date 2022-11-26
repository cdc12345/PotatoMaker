package org.cdc.potatomaker.ui.component;

import org.cdc.potatomaker.exception.PluginEnabledException;
import org.cdc.potatomaker.plugin.loader.PMPluginLoader;
import org.cdc.potatomaker.plugin.loader.PluginLoader;
import org.cdc.potatomaker.resourcepack.themes.Theme;
import org.cdc.potatomaker.util.ComponentUtils;
import org.cdc.potatomaker.util.locale.GlobalUtil;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;

/**
 * e-mail: 3154934427@qq.com
 * 插件面板
 *
 * @author cdc123
 * @classname PluginPanel
 * @date 2022/11/26 14:24
 */
public class PluginsPanel extends JPanel{
    public PluginsPanel(){
        setLayout(new BorderLayout());
        JLabel label = GlobalUtil.getInstance().label("tips.pluginTip");
        ComponentUtils.deriveFont(label, Theme.getInstance().getFontSize());

        JScrollPane pluginsScroll = new JScrollPane();

        JTable plugins = new JTable();
        plugins.setRowSelectionAllowed(true);
        plugins.setShowHorizontalLines(false);
        plugins.setModel(getTabModel());
        plugins.setRowHeight(30);
        pluginsScroll.setViewportView(plugins);

        add(label,"North");
        add(pluginsScroll,"Center");
    }

    private TableModel getTabModel(){
        return new TableModel() {
            PluginLoader[] pluginLoaders;
            @Override
            public int getRowCount() {
                pluginLoaders = PMPluginLoader.getInstance().getPlugins().values().toArray(new PluginLoader[0]);
                return pluginLoaders.length;
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public String getColumnName(int columnIndex) {
                columnIndex+=1;
                //不小心写错了,忘了是从0开始,emmm
                switch (columnIndex){
                    case 1 ->{
                        return GlobalUtil.getInstance().t("panel.pluginsColumns.pluginName.text");
                    }
                    case 2 ->{
                        return GlobalUtil.getInstance().t("panel.pluginsColumns.pluginAuthor.text");
                    }
                    case 3 ->{
                        return GlobalUtil.getInstance().t("panel.pluginsColumns.pluginVersion.text");
                    }
                    case 4 ->{
                        return GlobalUtil.getInstance().t("panel.pluginsColumns.pluginEnable.text");
                    }
                    default -> {
                        return GlobalUtil.getInstance().t("panel.columns.none.text");
                    }
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3){
                    return Boolean.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 3;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                var loader = pluginLoaders[rowIndex];
                var info = loader.getPluginInfo();
                columnIndex +=1;
                switch (columnIndex){
                    case 1 ->{
                        return info.getName();
                    }
                    case 2 ->{
                        return Arrays.toString(info.getAuthors());
                    }
                    case 3 ->{
                        return info.getVersion();
                    }
                    case 4 ->{
                        return loader.isEnable();
                    }
                    default -> {
                        return "";
                    }
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex == 3){
                    pluginLoaders[rowIndex].setEnable(Boolean.parseBoolean(aValue.toString()));
                    try {
                        PMPluginLoader.getInstance().enableAllPlugins();
                    } catch (PluginEnabledException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void addTableModelListener(TableModelListener l) {

            }

            @Override
            public void removeTableModelListener(TableModelListener l) {

            }
        };
    }
}
