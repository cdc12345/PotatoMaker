/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.cdc.potatomaker.ui.workspace.selector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.events.Events;
import org.cdc.potatomaker.events.workspace.WorkspaceOpenedEvent;
import org.cdc.potatomaker.resourcepack.themes.Theme;
import org.cdc.potatomaker.resourcepack.themes.ThemeManager;
import org.cdc.potatomaker.ui.component.JEmptyBox;
import org.cdc.potatomaker.ui.component.PathSelectorPanel;
import org.cdc.potatomaker.ui.dialogs.NewWorkspaceDialog;
import org.cdc.potatomaker.ui.dialogs.PreferencesDialog;
import org.cdc.potatomaker.ui.dialogs.UserFolderSelector;
import org.cdc.potatomaker.util.*;
import org.cdc.potatomaker.util.fold.UserFolderManager;
import org.cdc.potatomaker.util.images.ImageUtils;
import org.cdc.potatomaker.util.locale.GlobalUtil;
import org.cdc.potatomaker.util.resource.UIRE;
import org.cdc.potatomaker.workspace.Workspace;
import org.cdc.potatomaker.workspace.WorkspaceManager;
import org.cdc.potatomaker.workspace.type.WorkspaceModelManager;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class WorkspaceSelector extends JFrame implements DropTargetListener {

	private static final Logger LOG = LogManager.getLogger("Workspace Selector");

	private final JPanel recentPanel = new JPanel(new GridLayout());
	private final WorkspaceOpenListener workspaceOpenListener;
	private RecentWorkspaces recentWorkspaces = new RecentWorkspaces();

	public WorkspaceSelector() {

		this.workspaceOpenListener = (workspaceFolder, comp) -> Events.invokeEvent(new WorkspaceOpenedEvent(WorkspaceSelector.this, WorkspaceManager.getOrCreateWorkspace(workspaceFolder)));

		reloadTitle();
		setIconImage(UIRE.getInstance().get("icon").getImage());
		setFont(UIManager.getFont(ThemeManager.commonFont));

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		addWorkspaceButton(GlobalUtil.getInstance().t("dialog.workspace_selector.new_workspace"), UIRE.getInstance().get("addwrk"), e -> {
			if (WorkspaceModelManager.getTypes().size() == 0){
				JOptionPane.showMessageDialog(WorkspaceSelector.this,GlobalUtil.getInstance().t("dialog.newWorkspace.noTypes"),"Warning",JOptionPane.WARNING_MESSAGE);
			} else new NewWorkspaceDialog(WorkspaceSelector.this);
		}, actions);

		addWorkspaceButton(GlobalUtil.getInstance().t("dialog.workspace_selector.open_workspace"), UIRE.getInstance().get("opnwrk"), e -> {
			var path = PathSelectorPanel.showSelector(this,JFileChooser.DIRECTORIES_ONLY,false, UserFolderManager.getInstance().getWorkspaceFolder());
			PotatoMakerApplication.getInstance().openWorkspace(path);
		}, actions);

/*		addWorkspaceButton(GlobalUtil.getInstance().t("dialog.workspace_selector.import"), UIRE.getInstance().getResourceByKeyWord("impfile.png"), e -> {
			File file = FileDialogs.getOpenDialog(this, new String[] { ".zip" });
			if (file != null) {
				File workspaceDir = FileDialogs.getWorkspaceDirectorySelectDialog(this, UserFolderManager.getFileFromUserFolder("/MCreatorWorkspaces"));
				if (workspaceDir != null) {
					File workspaceFile = ShareableZIPManager.importZIP(file, workspaceDir, this);
					if (workspaceFile != null)
						workspaceOpenListener.workspaceOpened(workspaceFile);
				}
			}
		}, actions);*/

/*		addWorkspaceButton(GlobalUtil.getInstance().t("dialog.workspace_selector.clone"), UIRE.getInstance().get("vcsclone"), e -> {
			VCSInfo vcsInfo = VCSSetupDialogs.getVCSInfoDialog(this, GlobalUtil.getInstance().t("dialog.workspace_selector.vcs_info"));
			if (vcsInfo != null) {
				File workspaceFolder = FileDialogs.getWorkspaceDirectorySelectDialog(this, null);
				if (workspaceFolder != null) {
					try {
						setCursor(new Cursor(Cursor.WAIT_CURSOR));
						CloneWorkspace.cloneWorkspace(this, vcsInfo, workspaceFolder);
						try {
							File workspaceFile = WorkspaceUtils.getWorkspaceFileForWorkspaceFolder(workspaceFolder);
							workspaceOpenListener.workspaceOpened(workspaceFile);
						} catch (Exception ex) {
							throw new Exception("The remote repository is not a MCreator workspace or is corrupted");
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(this,
								GlobalUtil.getInstance().t("dialog.workspace_selector.clone.setup_failed", ex.getMessage()),
								GlobalUtil.getInstance().t("dialog.workspace_selector.clone.setup_failed.title"),
								JOptionPane.ERROR_MESSAGE);
					}
					setCursor(Cursor.getDefaultCursor());
				}
			}
		}, actions);*/

		JPanel logoPanel = new JPanel(new BorderLayout());
		JLabel logo = new JLabel(new ImageIcon(ImageUtils.resizeAA(UIRE.getInstance().get("logo.png").getImage(), 250, 45)));
		logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		logoPanel.add("North", PanelUtils.join(FlowLayout.LEFT, logo));
		JLabel version = GlobalUtil.getInstance().label("dialog.workspace_selector.version", PMVersion.getInstance().getVersion());
		version.setHorizontalTextPosition(SwingConstants.LEFT);
		version.setIcon(UIRE.getInstance().get("info"));
		version.setCursor(new Cursor(Cursor.HAND_CURSOR));

		ComponentUtils.deriveFont(version, 18);
		version.setForeground((Color) UIManager.get("MCreatorLAF.GRAY_COLOR"));
		logoPanel.add("South", version);

		logoPanel.setBorder(BorderFactory.createEmptyBorder(45, 26 + 25, 0, 10));
		actions.setBorder(BorderFactory.createEmptyBorder(25, 24 + 25, 2, 10));

		JPanel southcenter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southcenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 26, 60 - 1));

		JLabel donate = GlobalUtil.getInstance().label("dialog.workspace_selector.donate");
		donate.setIcon(UIRE.getInstance().get("donate"));
		donate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		ComponentUtils.deriveFont(donate, 13);
		donate.setForeground((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"));
		donate.setBorder(BorderFactory.createEmptyBorder());
		donate.setHorizontalTextPosition(JLabel.LEFT);
		donate.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent mouseEvent) {
				DesktopUtils.browseSafe("https://www.bilibili.com/video/BV1GJ411x7h7/?p=1");
			}
		});
		southcenter.add(donate);

		southcenter.add(new JEmptyBox(7, 5));

		JLabel prefs = new JLabel(GlobalUtil.getInstance().t("dialog.workspace_selector.preferences")) {
			@Override protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					String flagpath =
							"flags/" + GlobalUtil.getInstance().getCurrentLang().split("_")[1].toUpperCase(Locale.ENGLISH) + ".png";
					BufferedImage image = ImageIO.read(Objects.requireNonNull(UIRE.getInstance().getResource(flagpath)));
					g.drawImage(ImageUtils.crop(image, new Rectangle(1, 2, 14, 11)), getWidth() - 15, 5, this);
				} catch (Exception ignored) { // flag not found, ignore
				}
			}

			@Override public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width + 21, super.getPreferredSize().height);
			}
		};
		prefs.setIcon(UIRE.getInstance().get("settings"));
		prefs.setCursor(new Cursor(Cursor.HAND_CURSOR));
		prefs.setFont(UIManager.getFont(ThemeManager.commonFont));
		ComponentUtils.deriveFont(prefs, 13);
		prefs.setForeground(Theme.getInstance().getColorScheme().getForegroundColor());
		prefs.setBorder(BorderFactory.createEmptyBorder());
		prefs.setHorizontalTextPosition(JLabel.LEFT);
		prefs.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent mouseEvent) {
				new PreferencesDialog(WorkspaceSelector.this);
			}
		});
		southcenter.add(prefs);

		add("Center",
				PanelUtils.centerAndSouthElement(PanelUtils.northAndCenterElement(logoPanel, actions), southcenter));

		recentPanel.setBorder(
				BorderFactory.createMatteBorder(0, 0, 0, 1, (Color) UIManager.get("MCreatorLAF.LIGHT_ACCENT")));
		recentPanel.setPreferredSize(new Dimension(220, 10));

		add("West", recentPanel);

		new DropTarget(this, DnDConstants.ACTION_MOVE, this, true, null);

		setSize(790, 460);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	private void reloadTitle() {
		setTitle(GlobalUtil.getInstance().getLangValue("pm.title")+" " + PMVersion.getInstance().getVersion());
	}

	@Override public void dragEnter(DropTargetDragEvent dtde) {
		processDrag(dtde);
	}

	@Override public void dragOver(DropTargetDragEvent dtde) {
		processDrag(dtde);
	}

	@Override public void dropActionChanged(DropTargetDragEvent dtde) {

	}

	@Override public void dragExit(DropTargetEvent dtde) {

	}

	@Override public void drop(DropTargetDropEvent dtde) {
		Transferable transferable = dtde.getTransferable();
		if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			dtde.acceptDrop(dtde.getDropAction());
			try {
				List<?> transferData = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
				if (transferData.size() > 0) {
					Object transfObj = transferData.get(0);
					if (transfObj instanceof File workspaceFile) {
						if (workspaceFile.getName().endsWith(".mcreator")) {
							workspaceOpenListener.workspaceOpened(workspaceFile);
						} else {
							Toolkit.getDefaultToolkit().beep();
						}
					}
				}
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}
		} else {
			dtde.rejectDrop();
		}
	}

	private void processDrag(DropTargetDragEvent dtde) {
		if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			dtde.acceptDrag(DnDConstants.ACTION_MOVE);
		} else {
			dtde.rejectDrag();
		}
	}

	public void addOrUpdateRecentWorkspace(Workspace recentWorkspaceEntry) {
		reloadRecents();
		if (!recentWorkspaces.getList().contains(recentWorkspaceEntry))
			recentWorkspaces.getList().add(recentWorkspaceEntry);
		else
			recentWorkspaces.getList().get(recentWorkspaces.getList().indexOf(recentWorkspaceEntry))
					.update(recentWorkspaceEntry);

		ListUtils.rearrange(recentWorkspaces.getList(), recentWorkspaceEntry);
		saveRecentWorkspaces();
	}

	private void removeRecentWorkspace(Workspace recentWorkspace) {
		recentWorkspaces.getList().remove(recentWorkspace);
		saveRecentWorkspaces();
	}

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();

	private void saveRecentWorkspaces() {
		String serialized = gson.toJson(recentWorkspaces);
		if (serialized != null && !serialized.isEmpty()) {
			UserFolderManager.getInstance().writeResource(serialized,"recentworkspaces");
		}
	}

	private void reloadRecents() {
		if (UserFolderManager.getInstance().getOuterResourceAsFile("recentworkspaces").isFile()) {
			try {
				recentWorkspaces = gson.fromJson(
						new String(Files.readAllBytes(UserFolderManager.getInstance().getOuterResourceAsFile("recentworkspaces").toPath())),
						RecentWorkspaces.class);
				if (recentWorkspaces != null) {
					List<Workspace> recentWorkspacesFiltered = new ArrayList<>();
					for (Workspace recentWorkspaceEntry : recentWorkspaces.getList())
						if (recentWorkspaceEntry.getWorkspaceConfig().getWorkspaceFolder().isFile())
							recentWorkspacesFiltered.add(recentWorkspaceEntry);
					recentWorkspaces = new RecentWorkspaces(recentWorkspacesFiltered);
				}
			} catch (Exception e) {
				recentWorkspaces = null;
				LOG.warn("无法载入最近项目", e);
			}
		}

		recentPanel.removeAll();

		if (recentWorkspaces != null && recentWorkspaces.getList().size() > 0) {
			DefaultListModel<Workspace> defaultListModel = new DefaultListModel<>();
			recentWorkspaces.getList().forEach(defaultListModel::addElement);
			JList<Workspace> recentsList = new JList<>(defaultListModel);
			recentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


			JPopupMenu popup = new JPopupMenu();

			popup.addPopupMenuListener(new PopupMenuListener() {
				@Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					var location = MouseInfo.getPointerInfo().getLocation();
					recentsList.setSelectedIndex(recentsList.locationToIndex(new Point(0,location.y - recentsList.getLocationOnScreen().y)));

				}

				@Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

				}

				@Override public void popupMenuCanceled(PopupMenuEvent e) {

				}
			});

			JMenuItem open = new JMenuItem("打开项目");
			open.addActionListener(e -> workspaceOpenListener.workspaceOpened(recentsList.getSelectedValue().getPath()));
			popup.add(open);
			JMenuItem openInCompatibilityMode = new JMenuItem("以兼容模式打开");
			openInCompatibilityMode.addActionListener(a->workspaceOpenListener.workspaceOpened(recentsList.
					getSelectedValue().getWorkspaceConfig().getWorkspaceFolder(),true));
			popup.add(openInCompatibilityMode);
			JMenuItem delete = new JMenuItem("从最近删除");
			delete.addActionListener(e -> {
				removeRecentWorkspace(recentsList.getSelectedValue());
				reloadRecents();
			});
			popup.add(delete);
			JMenuItem openInExplorer = new JMenuItem("在资源管理器打开");
			openInExplorer.addActionListener(e -> DesktopUtils.openSafe(recentsList.getSelectedValue().getPath().getParentFile()));
			popup.add(openInExplorer);
			recentsList.setComponentPopupMenu(popup);


			recentsList.addMouseListener(new MouseAdapter() {
				@Override public void mouseClicked(MouseEvent mouseEvent) {
					if (mouseEvent.getButton() == MouseEvent.BUTTON2) {
						int idx = recentsList.locationToIndex(mouseEvent.getPoint());
						removeRecentWorkspace(defaultListModel.elementAt(idx));
						reloadRecents();
					} else if (mouseEvent.getButton() == MouseEvent.BUTTON1&&mouseEvent.getClickCount() == 2) {
						workspaceOpenListener.workspaceOpened(recentsList.getSelectedValue().getWorkspaceConfig().getWorkspaceFolder());
					}
				}
			});
			recentsList.addKeyListener(new KeyAdapter() {
				@Override public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						workspaceOpenListener.workspaceOpened(recentsList.getSelectedValue().getWorkspaceConfig().getWorkspaceFolder());
					} else if (e.getKeyCode() == KeyEvent.VK_DELETE){
						removeRecentWorkspace(recentsList.getSelectedValue());
					}
				}
			});
			recentsList.setCellRenderer(new RecentWorkspacesRenderer());
			JScrollPane scrollPane = new JScrollPane(recentsList);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			recentPanel.add(scrollPane);
		} else if (recentWorkspaces == null) {
			JLabel norecents = GlobalUtil.getInstance().label("dialog.workspace_selector.no_workspaces_loaded");
			norecents.setForeground((Color) UIManager.get("MCreatorLAF.GRAY_COLOR"));
			recentPanel.add(PanelUtils.totalCenterInPanel(norecents));
		} else {
			JLabel norecents = GlobalUtil.getInstance().label("dialog.workspace_selector.no_workspaces");
			norecents.setForeground((Color) UIManager.get("MCreatorLAF.GRAY_COLOR"));
			recentPanel.add(PanelUtils.totalCenterInPanel(norecents));
		}

		recentPanel.revalidate();
	}

	@Override public void setVisible(boolean b) {
		super.setVisible(b);
		if (b)
			reloadRecents();
	}

	private void addWorkspaceButton(String text, ImageIcon icon, ActionListener event, JPanel container) {
		JButton newWorkspace = new JButton(text);
		newWorkspace.setFont(this.getFont());
		ComponentUtils.deriveFont(newWorkspace,10);
		newWorkspace.setForeground((Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"));
		newWorkspace.setPreferredSize(new Dimension(100, 100));
		newWorkspace.setMargin(new Insets(0, 0, 0, 0));
		newWorkspace.setIcon(icon);
		newWorkspace.addActionListener(event);
		newWorkspace.setVerticalTextPosition(SwingConstants.BOTTOM);
		newWorkspace.setHorizontalTextPosition(SwingConstants.CENTER);
		newWorkspace.setBorder(
				BorderFactory.createLineBorder(((Color) UIManager.get("MCreatorLAF.LIGHT_ACCENT")).brighter(), 1));
		newWorkspace.setCursor(new Cursor(Cursor.HAND_CURSOR));
		container.add(newWorkspace);
	}

	@NotNull
	public RecentWorkspaces getRecentWorkspaces() {
		if (recentWorkspaces == null)
			this.recentWorkspaces = new RecentWorkspaces();

		return recentWorkspaces;
	}

}
