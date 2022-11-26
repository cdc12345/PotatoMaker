/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2012-2020, Pylo
 * Copyright (C) 2020-2021, Pylo, opensource contributors
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

package org.cdc.potatomaker.resourcepack.themes;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.util.resource.UIRE;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;
/**
 * from MCreator;
 */
@SuppressWarnings("unused") public class Theme {
	private static Theme theme;

	public static Theme getInstance(){
		if (theme == null) theme = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
				.fromJson(fromInputStream(UIRE.getInstance().getResourceByKeyWord("mcreator/theme.json")),Theme.class);
		return theme;
	}
	@Expose
	@Nullable private String version;
	@Expose
	@Nullable private String credits;
	@Expose
	private boolean useDefaultFontForSecondary;
	@Expose
	private int fontSize;

	@Expose
	@Nullable private ColorScheme colorScheme;

	private ImageIcon icon;



	/**
	 * @return <p>A String with optional credits to give to someone.</p>
	 */
	@Nullable public String getCredits() {
		return credits;
	}

	/**
	 * @return <p>The theme's version if provided</p>
	 */
	@Nullable public String getVersion() {
		return version;
	}

	/**
	 * <p>The main font size changes the size of the text for the main font. Usually, this parameter should not be changed except if the font is too big or too small with the default value.</p>
	 *
	 * @return <p>The main font size</p>
	 */
	public int getFontSize() {
		int forceTextSize = (int) PreferenceManager.getPreferences().getOrDefault("pm.forceSize",-1);
		if (forceTextSize > 0){
			return forceTextSize;
		} else if (fontSize != 0){
			return fontSize;
		} else {
			return 12;
		}
	}


	/**
	 * @return <p>Use the default font as the main font</p>
	 */
	public boolean useDefaultFontForSecondary() {
		return useDefaultFontForSecondary;
	}

	public ColorScheme getColorScheme() {
		if (colorScheme != null)
			return colorScheme;
		else
			return (ColorScheme) UIManager.get(ThemeManager.colorScheme);
	}
	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

}
