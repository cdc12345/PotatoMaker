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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.potatomaker.preference.PreferenceManager;
import org.cdc.potatomaker.resourcepack.themes.ColorScheme;
import org.cdc.potatomaker.resourcepack.themes.Theme;
import org.cdc.potatomaker.resourcepack.themes.ThemeManager;
import org.cdc.potatomaker.util.locale.GlobalUtil;
import org.cdc.potatomaker.util.resource.UIRE;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MCreatorTheme extends OceanTheme {

	private static final Logger LOG = LogManager.getLogger("Theme");

	public static final List<String> SYSTEM_FONT_LANGUAGES = Arrays.asList("zh", "ja", "ko", "th", "hi", "he", "iw");

	public static final Color MAIN_TINT_DEFAULT = new Color(0x93c54b);
	private Color MAIN_TINT;
	private final ColorScheme colorScheme;

	public static Font secondary_font;
	public static Font console_font;

	private static Font default_font;

	public MCreatorTheme(Theme theme) {
		this.colorScheme = theme.getColorScheme();

		if (colorScheme.getInterfaceAccentColor() != null) {
			try {
				MAIN_TINT = Color.decode(colorScheme.getInterfaceAccentColor());
			} catch (NumberFormatException exception) {
				LOG.warn(colorScheme.getInterfaceAccentColor()
								+ " in the current theme is not a valid hexadecimal number. The color defined by the user will be used.",
						exception.getMessage());
				MAIN_TINT = (Color)PreferenceManager.getPreferences().getOrDefault("ui.interfaceAccentColor",MAIN_TINT_DEFAULT);
			}
		} else {
			MAIN_TINT = (Color)PreferenceManager.getPreferences().getOrDefault("ui.interfaceAccentColor",MAIN_TINT_DEFAULT);
		}

		try {
			default_font = UIManager.getFont(ThemeManager.commonFont);
			secondary_font = default_font;

			String lang = GlobalUtil.getInstance().getCurrentLang();
			if (!SYSTEM_FONT_LANGUAGES.contains(lang) && !theme.useDefaultFontForSecondary()) {
				InputStream secondaryFontStream = UIRE.getInstance().getResourceByKeyWord(
						"fonts/secondary_font.ttf");
				if (secondaryFontStream != null) { // Font loaded from a file in the theme
					secondary_font = Font.createFont(Font.TRUETYPE_FONT, secondaryFontStream);
				} else { // Default secondary front (from the default_dark theme)
					secondary_font = UIManager.getFont(ThemeManager.commonFont);
					LOG.info("PM将会改用默认字体作为主字体.");
				}
			}

			InputStream consoleFontStream = UIRE.getInstance().getResourceByKeyWord("fonts/console_font.ttf");
			if (consoleFontStream != null) {
				console_font = Font.createFont(Font.TRUETYPE_FONT, consoleFontStream);
			} else {
				// Default main front (from the default_dark theme)
				console_font = default_font;
				LOG.info("控制台字体将会改用默认字体");
			}
		} catch (NullPointerException | FontFormatException | IOException e2) {
			LOG.info("无法载入MCreator主题! Error " + e2.getMessage());
		}
	}

	public Color getMainTint() {
		return MAIN_TINT;
	}

	public ColorScheme getColorScheme() {
		return colorScheme;
	}

	protected void initMCreatorThemeColors(UIDefaults table) {
		table.put("MCreatorLAF.BLACK_ACCENT", colorScheme.getSecondAltBackgroundColor());
		table.put("MCreatorLAF.DARK_ACCENT", colorScheme.getBackgroundColor());
		table.put("MCreatorLAF.LIGHT_ACCENT", colorScheme.getAltBackgroundColor());
		table.put("MCreatorLAF.GRAY_COLOR", colorScheme.getAltForegroundColor());
		table.put("MCreatorLAF.BRIGHT_COLOR", colorScheme.getForegroundColor());
		table.put("MCreatorLAF.MAIN_TINT", MAIN_TINT);
	}

	@Override public void addCustomEntriesToTable(UIDefaults table) {

		super.addCustomEntriesToTable(table);

		initMCreatorThemeColors(table);

		Set<Object> keySet = table.keySet();
		for (Object key : keySet) {
			if (key == null)
				continue;
			if (key.toString().toLowerCase(Locale.ENGLISH).contains("font")) {
				table.put(key, secondary_font.deriveFont(Font.PLAIN,Theme.getInstance().getFontSize()));
			} else if (key.toString().toLowerCase(Locale.ENGLISH).contains("bordercolor")) {
				table.put(key, MAIN_TINT);
			} else if (key.toString().toLowerCase(Locale.ENGLISH).endsWith(".background")) {
				table.put(key, colorScheme.getBackgroundColor());
			} else if (key.toString().toLowerCase(Locale.ENGLISH).endsWith(".foreground")) {
				table.put(key, colorScheme.getForegroundColor());
			} else if (key.toString().toLowerCase(Locale.ENGLISH).endsWith(".inactiveforeground")) {
				table.put(key, colorScheme.getAltForegroundColor());
			} else if (key.toString().toLowerCase(Locale.ENGLISH).endsWith(".disabledbackground")) {
				table.put(key, colorScheme.getBackgroundColor());
			} else if (key.toString().toLowerCase(Locale.ENGLISH).endsWith(".disabledforeground")) {
				table.put(key, colorScheme.getAltForegroundColor());
			} else if (key.toString().toLowerCase(Locale.ENGLISH).endsWith(".caretforeground")) {
				table.put(key, colorScheme.getForegroundColor());
			}
		}

		table.put("TabbedPane.contentOpaque", false);

		table.put("Tree.rendererFillBackground", false);

		table.put("TitledBorder.titleColor", colorScheme.getForegroundColor());

		table.put("SplitPane.dividerFocusColor", colorScheme.getAltBackgroundColor());
		table.put("SplitPane.darkShadow", colorScheme.getAltBackgroundColor());
		table.put("SplitPane.shadow", colorScheme.getAltBackgroundColor());
		table.put("SplitPaneDivider.draggingColor", MAIN_TINT);

		table.put("OptionPane.messageForeground", colorScheme.getForegroundColor());

		table.put("Label.foreground", colorScheme.getForegroundColor());
		table.put("Label.disabledForeground", colorScheme.getForegroundColor());
		table.put("Label.inactiveforeground", colorScheme.getForegroundColor());
		table.put("Label.textForeground", colorScheme.getForegroundColor());

		table.put("Button.toolBarBorderBackground", colorScheme.getForegroundColor());
		table.put("Button.disabledToolBarBorderBackground", colorScheme.getAltBackgroundColor());
		table.put("ToolBar.rolloverBorder",
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(colorScheme.getBackgroundColor(), 1),
						BorderFactory.createCompoundBorder(
								BorderFactory.createLineBorder(colorScheme.getAltBackgroundColor(), 1),
								BorderFactory.createLineBorder(colorScheme.getBackgroundColor(), 3))));

		table.put("ScrollBarUI", SlickDarkScrollBarUI.class.getName());
		table.put("SpinnerUI", DarkSpinnerUI.class.getName());
		table.put("SplitPaneUI", DarkSplitPaneUI.class.getName());
		table.put("SliderUI", DarkSliderUI.class.getName());
		table.put("ComboBoxUI", DarkComboBoxUI.class.getName());

		table.put("Menu.border", BorderFactory.createEmptyBorder(3, 4, 3, 4));
		table.put("MenuItem.border", BorderFactory.createEmptyBorder(3, 4, 3, 4));

		table.put("PopupMenu.border", BorderFactory.createLineBorder(colorScheme.getAltBackgroundColor()));

		table.put("Separator.foreground", colorScheme.getAltBackgroundColor());
		table.put("Separator.background", colorScheme.getBackgroundColor());

		table.put("Menu.foreground", colorScheme.getForegroundColor());
		table.put("MenuItem.foreground", colorScheme.getForegroundColor());

		table.put("ComboBox.foreground", colorScheme.getForegroundColor());
		table.put("ComboBox.background", colorScheme.getAltBackgroundColor());
		table.put("ComboBox.disabledForeground", colorScheme.getAltForegroundColor());

		table.put("Spinner.foreground", colorScheme.getForegroundColor());
		table.put("Spinner.background", colorScheme.getAltBackgroundColor());

		table.put("FormattedTextField.foreground", colorScheme.getForegroundColor());
		table.put("FormattedTextField.inactiveForeground", colorScheme.getAltForegroundColor());
		table.put("FormattedTextField.background", colorScheme.getAltBackgroundColor());
		table.put("FormattedTextField.border", BorderFactory.createEmptyBorder(2, 5, 2, 5));

		table.put("TextField.foreground", colorScheme.getForegroundColor());
		table.put("TextField.inactiveForeground", colorScheme.getAltForegroundColor());
		table.put("TextField.background", colorScheme.getAltBackgroundColor());
		table.put("TextField.border", BorderFactory.createEmptyBorder(2, 5, 2, 5));

		table.put("PasswordField.foreground", colorScheme.getForegroundColor());
		table.put("PasswordField.inactiveForeground", colorScheme.getAltForegroundColor());
		table.put("PasswordField.background", colorScheme.getAltBackgroundColor());
		table.put("PasswordField.border", BorderFactory.createEmptyBorder(2, 5, 2, 5));

		table.put("ComboBox.border", null);

		List<?> buttonGradient = Arrays.asList(0f, 0f, new ColorUIResource(colorScheme.getForegroundColor()),
				new ColorUIResource(colorScheme.getForegroundColor()),
				new ColorUIResource(colorScheme.getForegroundColor()));

		table.put("Button.gradient", buttonGradient);
		table.put("Button.rollover", true);

		table.put("CheckBox.gradient", buttonGradient);
		table.put("CheckBox.rollover", true);

		table.put("RadioButton.gradient", buttonGradient);
		table.put("RadioButtonMenuItem.gradient", buttonGradient);
		table.put("RadioButton.rollover", true);
		table.put("RadioButtonMenuItem.rollover", true);
		table.put("RadioButtonMenuItem.border", BorderFactory.createEmptyBorder(3, 4, 3, 4));

		table.put("ToggleButton.gradient", buttonGradient);
		table.put("ToggleButton.rollover", true);

		List<?> sliderGradient = Arrays.asList(0f, 0f, new ColorUIResource(colorScheme.getBackgroundColor()),
				new ColorUIResource(colorScheme.getBackgroundColor()),
				new ColorUIResource(colorScheme.getBackgroundColor()));

		table.put("Slider.altTrackColor", new ColorUIResource(colorScheme.getBackgroundColor()));
		table.put("Slider.gradient", sliderGradient);
		table.put("Slider.focusGradient", sliderGradient);

		table.put("Spinner.border", BorderFactory.createEmptyBorder());

		table.put("List.focusCellHighlightBorder", null);

		table.put("List.border", null);
		table.put("ScrollPane.border", null);
		table.put("Tree.border", null);

		table.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		table.put("ToggleButton.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		table.put("CheckBox.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		table.put("TabbedPane.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		table.put("RadioButton.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		table.put("RadioButtonMenuItem.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		table.put("Slider.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		table.put("ComboBox.focus", new ColorUIResource(new Color(0, 0, 0, 0)));

		table.put("CheckBox.icon", new CheckBoxIcon());
		table.put("RadioButton.icon", new RadioButtonIcon());
		table.put("RadioButtonMenuItem.icon", new RadioButtonIcon());

		table.put("TabbedPane.contentAreaColor", colorScheme.getBackgroundColor());
		table.put("TabbedPane.contentBorderInsets", new Insets(4, 2, 3, 3));
		table.put("TabbedPane.selected", colorScheme.getAltBackgroundColor());
		table.put("TabbedPane.tabAreaBackground", colorScheme.getAltBackgroundColor());
		table.put("TabbedPane.tabAreaInsets", new Insets(2, 2, 0, 6));
		table.put("TabbedPane.unselectedBackground", colorScheme.getBackgroundColor());

		table.put("ToolTip.border", BorderFactory.createLineBorder(colorScheme.getForegroundColor()));
		table.put("ToolTip.foreground", colorScheme.getForegroundColor());
		table.put("ToolTip.background", colorScheme.getBackgroundColor());

		table.put("ScrollBar.width", 7);

		table.put("SplitPane.border", BorderFactory.createEmptyBorder());

		table.put("FileChooser.homeFolderIcon", UIRE.getInstance().get("laf.homeFolder.gif"));
		table.put("FileChooser.newFolderIcon", UIRE.getInstance().get("laf.newFolder.gif"));
		table.put("FileChooser.upFolderIcon", UIRE.getInstance().get("laf.upFolder.gif"));
		table.put("FileChooser.computerIcon", UIRE.getInstance().get("laf.computer.gif"));
		table.put("FileChooser.hardDriveIcon", UIRE.getInstance().get("laf.hardDrive.gif"));
		table.put("FileChooser.floppyDriveIcon", UIRE.getInstance().get("laf.floppy.gif"));
		table.put("FileChooser.closedIcon", UIRE.getInstance().get("laf.newFolder.gif"));

		table.put("Tree.closedIcon", UIRE.getInstance().get("laf.newFolder.gif"));
		table.put("Tree.openIcon", UIRE.getInstance().get("laf.upFolder.gif"));
		table.put("Tree.leafIcon", UIRE.getInstance().get("laf.file.gif"));

		table.put("FileView.directoryIcon", UIRE.getInstance().get("laf.directory.gif"));
		table.put("FileView.fileIcon", UIRE.getInstance().get("laf.file.gif"));

		table.put("OptionPane.warningIcon", UIRE.getInstance().get("laf.warning"));
		table.put("OptionPane.errorIcon", UIRE.getInstance().get("laf.error"));
		table.put("OptionPane.questionIcon", UIRE.getInstance().get("laf.question"));
		table.put("OptionPane.informationIcon", UIRE.getInstance().get("laf.info"));

		table.put("MenuItem.acceleratorForeground", colorScheme.getAltForegroundColor());
	}

	@Override public String getName() {
		return "MCreator";
	}

	@Override protected ColorUIResource getPrimary1() {
		return new ColorUIResource(colorScheme.getBackgroundColor());
	}

	@Override protected ColorUIResource getPrimary2() {
		return new ColorUIResource(MAIN_TINT);
	}

	@Override protected ColorUIResource getPrimary3() {
		return new ColorUIResource(MAIN_TINT);
	}

	@Override protected ColorUIResource getSecondary1() {
		return new ColorUIResource(colorScheme.getAltBackgroundColor());
	}

	@Override protected ColorUIResource getSecondary2() {
		return new ColorUIResource(colorScheme.getAltBackgroundColor());
	}

	@Override protected ColorUIResource getSecondary3() {
		return new ColorUIResource(0xeeeeee);
	}

	@Override public ColorUIResource getControl() {
		return new ColorUIResource(colorScheme.getAltBackgroundColor());
	}

	@Override public ColorUIResource getControlHighlight() {
		return new ColorUIResource(colorScheme.getAltBackgroundColor());
	}

	@Override public ColorUIResource getPrimaryControlHighlight() {
		return new ColorUIResource(colorScheme.getAltForegroundColor());
	}

	@Override public FontUIResource getControlTextFont() {
		return new FontUIResource(default_font);
	}

	@Override public FontUIResource getSystemTextFont() {
		return new FontUIResource(default_font);
	}

	@Override public FontUIResource getUserTextFont() {
		return new FontUIResource(default_font);
	}

	@Override public FontUIResource getMenuTextFont() {
		return new FontUIResource(default_font);
	}

	@Override public FontUIResource getWindowTitleFont() {
		return new FontUIResource(default_font);
	}

	@Override public FontUIResource getSubTextFont() {
		return new FontUIResource(default_font);
	}

}
