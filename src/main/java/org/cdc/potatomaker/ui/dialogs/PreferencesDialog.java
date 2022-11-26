package org.cdc.potatomaker.ui.dialogs;

import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.preference.sections.PreferenceSectionModel;
import org.cdc.potatomaker.preference.sections.PreferenceSectionsManager;
import org.cdc.potatomaker.util.locale.GlobalUtil;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * e-mail: 3154934427@qq.com
 * 设置窗口
 *
 * @author cdc123
 * @classname PreferencesDialog
 * @date 2022/11/26 15:14
 */
public class PreferencesDialog extends JDialog {

    private String[] models;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);
    public PreferencesDialog(Window parent){
        super(parent);
        setTitle(GlobalUtil.getInstance().t("dialog.preference.title"));
        JScrollPane sectionsScroll = new JScrollPane();
        JList<String> sections = new JList<>();
        sections.setPreferredSize(new Dimension(100,600));
        sections.setModel(new ListModel<>() {
            @Override
            public int getSize() {
                models = PreferenceManager.getSections();
                return models.length;
            }

            @Override
            public String getElementAt(int index) {
                return GlobalUtil.getInstance().t("preferences.sections."+models[index]);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });

        for (String section:PreferenceManager.getSections()){
            content.add(section, PreferenceSectionsManager.
                    getSection(section).getRender().getSectionComponent(section));
        }


        JPanel controlPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton apply = new JButton(GlobalUtil.getInstance().t("dialog.preferences.apply"));


        sections.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3){
                    cardLayout.show(content,models[sections.getSelectedIndex()]);
                }
            }
        });
        sectionsScroll.setViewportView(sections);

        add(sectionsScroll,"West");
        add(content,"Center");

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);

    }
}
