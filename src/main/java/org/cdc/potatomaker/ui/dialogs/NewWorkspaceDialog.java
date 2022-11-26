package org.cdc.potatomaker.ui.dialogs;

import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.resourcepack.themes.ThemeManager;
import org.cdc.potatomaker.ui.component.PathSelectorPanel;
import org.cdc.potatomaker.ui.workspace.selector.WorkspaceSelector;
import org.cdc.potatomaker.util.ComponentUtils;
import org.cdc.potatomaker.util.PotatoMakerApplication;
import org.cdc.potatomaker.util.fold.UserFolderManager;
import org.cdc.potatomaker.util.locale.GlobalUtil;
import org.cdc.potatomaker.util.resource.UIRE;
import org.cdc.potatomaker.workspace.type.WorkspaceModelManager;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Objects;

/**
 * e-mail: 3154934427@qq.com
 * 新建工作区的对话框
 *
 * @author cdc123
 * @classname NewWorkspaceDialog
 * @date 2022/11/26 10:57
 */
public class NewWorkspaceDialog extends JDialog {
    private static final Logger LOGGER = LogManager.getLogger("NewWorkspaceDialog");

    public NewWorkspaceDialog(WorkspaceSelector parent){
        super(parent);
        this.setTitle(GlobalUtil.getInstance().t("dialog.newWorkspace.title"));
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setIconImage((Image) UIManager.get(ThemeManager.icon));


        JPanel center = new JPanel(new BorderLayout());
        Box ver = Box.createVerticalBox();

        Box namePanel = Box.createHorizontalBox();
        JLabel nameLabel = GlobalUtil.getInstance().label("dialog.newWorkspace.nameLabel");
        ComponentUtils.deriveFont(nameLabel,12);
        JTextField nameFiled = new JTextField();
        nameFiled.setText("Hello world");
        nameFiled.setPreferredSize(new Dimension(200,40));
        namePanel.add(nameLabel);
        namePanel.add(Box.createHorizontalGlue());
        namePanel.add(nameFiled);
        ver.add(namePanel);

        Box pathPanel = Box.createHorizontalBox();
        JLabel pathSelectLabel = GlobalUtil.getInstance().label("dialog.newWorkspace.pathSelectLabel");
        ComponentUtils.deriveFont(pathSelectLabel,12);
        PathSelectorPanel pathSelectorPanel = new PathSelectorPanel(null,
                JFileChooser.DIRECTORIES_ONLY,false, UserFolderManager.getInstance().getWorkspaceFolder());
        ComponentUtils.deriveFont(pathSelectorPanel,20);
        pathPanel.add(pathSelectLabel);
        pathPanel.add(Box.createHorizontalGlue());
        pathPanel.add(pathSelectorPanel);
        ver.add(pathPanel);

        Box genPanel = Box.createHorizontalBox();
        JLabel genLabel = GlobalUtil.getInstance().label("dialog.newWorkspace.genLabel");
        ComponentUtils.deriveFont(genLabel,12);
        JComboBox<WorkspaceModelManager.WorkspaceModelPack> generators = new JComboBox<>();
        ComponentUtils.deriveFont(generators,20);
        genPanel.add(genLabel);
        genPanel.add(Box.createHorizontalGlue());
        genPanel.add(generators);
        ver.add(genPanel);
        JPanel newCom = new JPanel(new GridLayout(1,1));
        generators.addItemListener(a -> {
            Component component = null;
            try {
                component = ((WorkspaceModelManager.WorkspaceModelPack) Objects.requireNonNull(generators.getSelectedItem()))
                        .model().getNewWorkspaceComponent();
            } catch (Exception e){
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(outputStream));
                new JLabel(GlobalUtil.getInstance().t("dialog.newWorkspace.errNewCom")+"\n"+outputStream);
            }
            if (component == null) component = new JLabel(GlobalUtil.getInstance().t("dialog.newWorkspace.errNewCom")+"返回为null");
            newCom.removeAll();
            newCom.add(component);

            generators.setEnabled(true);
            pathSelectorPanel.setEnabled(true);
        });
        if (WorkspaceModelManager.getTypes().size() == 0){
            newCom.add(new Panel());
        }
        if (generators.getModel().getSize()>0) {
            generators.setSelectedIndex(0);
        }

        JPanel west = new JPanel(new BorderLayout());
        JLabel label = new JLabel(UIRE.getInstance().get("addwrk"));
        label.setText(GlobalUtil.getInstance().t("dialog.newWorkspace.title"));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JScrollPane typesScroll = new JScrollPane();
        JList<String> types = new JList<>();
        types.setPreferredSize(new Dimension(100,700));
        types.setModel(new DefaultListModel<>(){
            private String[] types;
            @Override
            public int getSize() {
                types = WorkspaceModelManager.getTypes().toArray(new String[0]);
                return types.length;
            }

            @Override
            public String get(int index) {
                return types[index];
            }

            @Override
            public String getElementAt(int index) {
                generators.updateUI();
                return super.getElementAt(index);
            }
        });
        types.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setIcon(UIRE.getInstance().get("workspace.types"+value));
                return label;
            }
        });
        types.setVisibleRowCount(10);
        if (types.getModel().getSize() > 0) {
            types.setSelectedIndex(0);
        }
        typesScroll.setViewportView(types);

        west.add(label,"North");
        west.add(typesScroll,"Center");


        generators.setModel(new DefaultComboBoxModel<>(){
            private WorkspaceModelManager.WorkspaceModelPack[] packs;
            @Override
            public int getSize() {
                packs = WorkspaceModelManager.getModelsByType(types.getSelectedValue());
                return packs.length;
            }

            @Override
            public WorkspaceModelManager.WorkspaceModelPack getElementAt(int index) {
                return packs[index];
            }
        });


        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton(GlobalUtil.getInstance().t("pm.cancel"));
        cancel.addActionListener(a->{
            NewWorkspaceDialog.this.setVisible(false);
        });
        JButton ok = new JButton("Ok");
        ok.addActionListener(a->{
            var pack = (WorkspaceModelManager.WorkspaceModelPack)generators.getSelectedItem();
            PotatoMakerApplication.getInstance().createWorkspace(pack,pathSelectorPanel.getCurrentFile());
            NewWorkspaceDialog.this.setVisible(false);
        });
        JButton help = new JButton(UIRE.getInstance().get("help"));
        help.addActionListener(a->{
            String content;
            try {
                content = new String(UIRE.getInstance()
                        .getResourceByKeyWord("workspace/types/"+types.getSelectedValue()).readAllBytes());
            } catch (Exception e) {
                content = GlobalUtil.getInstance().getLangValue("dialog.newWorkspace.nullHelp");
            }
            JOptionPane.showMessageDialog(NewWorkspaceDialog.this,content,"Help",JOptionPane.INFORMATION_MESSAGE);
        });
        controlPanel.add(help);
        controlPanel.add(cancel);
        controlPanel.add(ok);

        center.add(ver,"North");
        center.add(newCom,"Center");

        add(west,"West");
        add(center,"Center");
        add(controlPanel,"South");

        boolean enable = types.getSelectedValue() != null;
        ver.setEnabled(enable);
        generators.setEnabled(enable);
        pathSelectorPanel.setEnabled(enable);
        nameFiled.setEnabled(enable);

        setSize(700,600);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
