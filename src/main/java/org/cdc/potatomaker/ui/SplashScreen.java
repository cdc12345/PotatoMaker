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

package org.cdc.potatomaker.ui;

import org.cdc.potatomaker.ui.component.ImagePanel;
import org.cdc.potatomaker.ui.component.ProgressBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class SplashScreen extends JWindow {

	private final ProgressBar initloadprogress = new ProgressBar();
	private final JLabel loadstate = new JLabel();

	public SplashScreen() {
		Font splashFont = new Font("Sans-Serif", Font.PLAIN, 13);

		JPanel imagePanel = null;
		try {
			imagePanel = new ImagePanel(ImageIO.read(
					Objects.requireNonNull(this.getClass().getResourceAsStream("/images/splash.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		imagePanel.setLayout(null);
		imagePanel.setBackground(new Color(50, 50, 50));

		initloadprogress.setEmptyColor(null);
		initloadprogress.setOpaque(false);
		initloadprogress.setForeground(Color.white);
		initloadprogress.setMaximalValue(100);
		initloadprogress.init();
		initloadprogress.setBounds(30 + 10 - 4, 283 - 10, 568, 3);
		imagePanel.add(initloadprogress);

		loadstate.setFont(splashFont.deriveFont(12f));
		loadstate.setForeground(Color.white);
		loadstate.setBounds(30 + 10 - 4, 283 - 39 - 10, 500, 45);
		imagePanel.add(loadstate);

		add(imagePanel);
		setSize(640, 380);
		setLocationRelativeTo(null);
		setVisible(true);
		requestFocus();
		requestFocusInWindow();
		toFront();
	}

	public void setProgress(int percentage, String message) {
		initloadprogress.setCurrentValue(percentage);
		loadstate.setText(message);

	}

}
