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

package org.cdc.potatomaker.ui.laf;

import org.cdc.potatomaker.util.resource.UIRE;

import javax.swing.*;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.TreePath;
import java.awt.*;

public class SlickTreeUI extends MetalTreeUI {

	@Override public Icon getCollapsedIcon() {
		return UIRE.getInstance().get("16px.collapsed");
	}

	@Override public Icon getExpandedIcon() {
		return UIRE.getInstance().get("16px.expanded");
	}

	@Override protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
	}

	@Override protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds, Insets insets, TreePath path) {
	}
}
