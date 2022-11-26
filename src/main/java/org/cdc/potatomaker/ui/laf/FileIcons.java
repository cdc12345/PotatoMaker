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
import java.io.File;

public class FileIcons {

	public static ImageIcon getIconForFile(File file) {
		return getIconForFile(file.getName());
	}

	public static ImageIcon getIconForFile(String file) {
		if (file.endsWith(".java"))
			return UIRE.getInstance().get("16px.class.gif");
		if (file.endsWith(".ogg"))
			return UIRE.getInstance().get("16px.sound");
		if (file.equals("mcmod.info") || file.endsWith(".json") || file.endsWith(".mcmeta"))
			return UIRE.getInstance().get("16px.json.gif");
		if (file.endsWith(".yaml") || file.endsWith(".yml"))
			return UIRE.getInstance().get("16px.yaml.gif");
		if (file.endsWith(".gradle"))
			return UIRE.getInstance().get("16px.gradle.gif");
		if (file.endsWith(".txt"))
			return UIRE.getInstance().get("laf.text.gif");
		if (file.endsWith(".png") || file.endsWith(".gif"))
			return UIRE.getInstance().get("laf.image.gif");

		return UIRE.getInstance().get("laf.file.gif");
	}

}
