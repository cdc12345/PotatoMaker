package org.cdc.potatomaker.preference;

import org.cdc.potatomaker.events.PreferenceRenderingEvent;
import org.cdc.potatomaker.preference.render.PreferenceRender;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * e-mail: 3154934427@qq.com
 * 设置界面渲染器,配置系最高一级的渲染器
 * 只有最高一级的才能被Override
 * @author cdc123
 * @classname PreferenceRender
 * @date 2022/11/13 22:33
 */
public class DefaultPreferenceRender implements PreferenceRender {

    private final Preferences preferences;

    private final ArrayList<PreferenceSection> sections;

    private CardLayout cardLayout;
    /**
     * 给PreferenceSection渲染时与其他的section沟通用的缓存
     */
    private final HashMap<String,Object> cache;
    private final ArrayList<Component> componentCache;


    public DefaultPreferenceRender(){
        this.componentCache = new ArrayList<>();
        this.sections = new ArrayList<>();
        this.preferences = (Preferences) PreferenceManager.getPreferences().clone();
        this.cache = new HashMap<>();
    }

    public void addSection(PreferenceSection section){
        sections.add(section);
        sections.sort(Comparator.comparingInt(PreferenceSection::getPriority));
        reloadSections();
    }

    public void reloadSections(){
        cardLayout = new CardLayout();
        componentCache.clear();
        for (PreferenceSection section : sections) {
            var com = section.getSectionRender()
                    .getComponent(new PreferenceRenderingEvent(() -> preferences, null, cache));
            com.setName(section.getName());
            cardLayout.addLayoutComponent(com,section.getName());
            componentCache.add(com);
        }
    }

    @Override
    public Component getComponent(PreferenceRenderingEvent event) {
        JPanel panel = new JPanel(new BorderLayout());

        JList<Component> sections = new JList<>();
        sections.setCellRenderer(new DefaultSectionsCellRender());
        sections.setModel(new DefaultSectionModel());
        panel.add(sections,"Center");

        JPanel content = new JPanel(cardLayout);
        sections.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    cardLayout.show(content,sections.getSelectedValue().getName());
                }
            }
        });
        return null;
    }

    @Override
    public Preferences storePreference() {
        return preferences;
    }

    private class DefaultSectionModel implements ListModel<Component> {

        @Override
        public int getSize() {
            return sections.size();
        }

        @Override
        public Component getElementAt(int index) {
            return componentCache.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {

        }

        @Override
        public void removeListDataListener(ListDataListener l) {

        }
    }

    private static class DefaultSectionsCellRender extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setText(((Component) value).getName());
            return label;
        }
    }

}
