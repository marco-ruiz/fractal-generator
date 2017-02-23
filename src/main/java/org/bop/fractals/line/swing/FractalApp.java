/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bop.fractals.line.swing;

import java.awt.Color;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.bop.fractals.progress.IProgressUpdater;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class FractalApp extends BaseSwingApp {

	private static Color getColor(String name) {
		try {
		    return (Color)Color.class.getField(name.toLowerCase()).get(null);
		} catch (Exception e) {}
		return Color.black;
	}

    public static void main(String[] args) {
        new FractalApp();
    }

	private JTextField txtIters;
	private JCheckBox chkOnlyLast;
	private JTextField txtGenerated;
	private FractalPanel pnlFractal;

	public FractalApp() {
		super("Fractal Generator", 800, 800);
		setLayout(null);
		setSize(750, 750);

		addButton("Calculate Fractal", 252, 12, 108, 33,
				e -> pnlFractal.computeFractal(Integer.parseInt(txtIters.getText()), chkOnlyLast.isSelected(), this::updateProgress));
		addButton("Define Base", 24, 12, 108, 33, event -> pnlFractal.startBaseDefinition());
		addButton("Reset", 24, 60, 108, 33, event -> pnlFractal.clear());
		addButton("Show Pattern", 144, 12, 96, 33, event -> pnlFractal.show(FractalPanel.MODE_PATTERN));
		addButton("Show Fractal", 144, 60, 96, 33, event -> pnlFractal.show(FractalPanel.MODE_FRACTAL));
		addColorCombo();

		txtIters = addComponent(new JTextField("5"), 372, 20, 36, 20);
		chkOnlyLast = addComponent(new JCheckBox("Only Last Iteration"), 444, 20, 120, 20);
		txtGenerated  = addComponent(new JTextField(""), 440, 65, 125, 22);
		pnlFractal = addComponent(new FractalPanel(), 25, 100, 750, 650);
		appFrame.validate();
	}

	private void updateProgress(double progress) {
		txtGenerated.setBackground(Color.yellow);
		txtGenerated.setText("" + progress);
		if (progress == IProgressUpdater.FINISHED_PROGRESS) {
			// Fractal finished!
			txtGenerated.setBackground(Color.white);
		}
		txtGenerated.repaint();
	}

	public JComboBox<String> addColorCombo() {
		JComboBox<String> chColor = new JComboBox<>(new String[]
				{"Black", "Blue", "Gray", "Green", "Magenta", "Orange", "Pink", "Red", "Yellow", "White"});
		chColor.setSelectedIndex(0);
		chColor.addItemListener(event -> pnlFractal.setCurrentColor(getColor((String) chColor.getSelectedItem())));
		return addComponent(chColor, 252, 65, 151, 25);
	}
}


