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
import lombok.Setter;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.util.resource.UIRE;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.regex.Pattern;

import static org.cdc.potatomaker.util.ResourceManager.fromInputStream;
@Setter
@SuppressWarnings("unused") public class Theme {
	private static Theme instance;

	public static Theme getInstance() {
		if (instance == null) {
			instance = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(fromInputStream(UIRE.getInstance().getResource(Pattern.compile("theme\\.json"))),Theme.class);
		}
		return instance;
	}




	@Expose
	@Nullable private String version;
	@Expose
	@Nullable private String credits;
	@Expose
	@Nullable private String defaultFont;
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
		int forceTextSize = (Integer) PreferenceManager.getPreferences().getOrDefault("ui.forceSize",0);
		if (forceTextSize > 0){
			return forceTextSize;
		} else if (fontSize != 0){
			return fontSize;
		} else {
			return 12;
		}
	}

	/**
	 * @return The default font to use with some languages.
	 */
	public String getDefaultFont() {
		if (defaultFont != null)
			return defaultFont;
		else
			return "Sans-Serif";
	}

	/**
	 * @return <p>Use the default font as the main font</p>
	 */
	public boolean useDefaultFontForSecondary() {
		return useDefaultFontForSecondary;
	}
	public ColorScheme getColorScheme() {
		return colorScheme;
	}

	/**
	 *
	 * @return <p>An {@link ImageIcon} representing the plugin.</p>
	 */
	public ImageIcon getIcon() {
		return icon;
	}

	/**
	 * <p>To be detected, the name of the image file needs to be "icon.png" located into the main folder.</p>
	 *
	 */
	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Theme{" +
				"version='" + version + '\'' +
				", credits='" + credits + '\'' +
				", defaultFont='" + defaultFont + '\'' +
				", useDefaultFontForSecondary=" + useDefaultFontForSecondary +
				", fontSize=" + fontSize +
				", colorScheme=" + colorScheme +
				", icon=" + icon +
				'}';
	}
}
