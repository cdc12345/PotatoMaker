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

import org.cdc.potatomaker.ui.laf.MCreatorTheme;
import org.cdc.potatomaker.util.StringUtils;
import org.cdc.potatomaker.util.images.ImageUtils;
import org.cdc.potatomaker.util.resource.UIRE;
import org.cdc.potatomaker.workspace.Workspace;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

class RecentWorkspacesRenderer extends JLabel implements ListCellRenderer<Workspace> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Workspace> list,
			Workspace value, int index, boolean isSelected, boolean cellHasFocus) {
		setOpaque(isSelected);
		setBackground((Color) UIManager.get("MCreatorLAF.DARK_ACCENT"));
		setForeground(isSelected ?
				(Color) UIManager.get("MCreatorLAF.MAIN_TINT") :
				(Color) UIManager.get("MCreatorLAF.GRAY_COLOR"));
		setBorder(BorderFactory.createEmptyBorder(2, 5, 3, 0));

		setFont(MCreatorTheme.secondary_font.deriveFont(16.0f));

		String path = value.getWorkspaceConfig().getWorkspaceFolder().getParentFile().getAbsolutePath().replace("\\", "/");

		if (!Objects.equals(value.getGenerator(), "default")) {
			ImageIcon icon = null;
			try {
				icon = new ImageIcon(
						ImageUtils.darken(ImageUtils.toBufferedImage(ImageIO.read(UIRE.getInstance().
								getResourceByKeyWord("generator/"+value.getGenerator()+".png")))));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (isSelected) {
				setIcon(ImageUtils.colorize(icon, (Color) UIManager.get("MCreatorLAF.MAIN_TINT"), false));
			} else {
				setIcon(icon);
			}

			setIconTextGap(10);
			setText("<html><font style=\"font-size: 15px;\">" + StringUtils.abbreviateString(value.getName(), 18)
					+ "</font><small>"+value.getGenerator()+"<br>" + StringUtils.abbreviateStringInverse(path, 34));
		} else {
			setIcon(null);

			setIconTextGap(0);
			setText("<html><font style=\"font-size: 15px;\">" + StringUtils.abbreviateString(value.getWorkspaceConfig().getWorkspaceName(), 20)
					+ "</font><small>"+value.getGenerator()+"<br>" + StringUtils.abbreviateStringInverse(path, 37));
		}

		return this;
	}

}
