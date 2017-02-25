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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.bop.fractals.GeometricPatternFractalGenerator;
import org.bop.fractals.PatternEditor;
import org.bop.fractals.line.FractalLine;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class FractalPanel extends JPanel implements PatternEditor {
	// Constants to define the way panel should be repainted
	static int MODE_PATTERN = 0;
	static int MODE_FRACTAL = 1;

	// Variables to keep state and mode of the panel (operations and repaint mode)
	private boolean choosingStartLine = true;
	private int mode = FractalPanel.MODE_PATTERN;

	// Base FractalLine, vectors of FractalLine that keep the pattern and the actual lines that compose the fractal result
	private FractalLine base = null;
	private List<FractalLine> patterns = new ArrayList<FractalLine>();

	// Temporary points for one cycle of defining a FractalLine
	private Point editA, editB;

	// Temporary variables declare here to accelerate fractal process creation
	private boolean definingBase = true;
	public boolean onlyLastIter = false;
	private Color color;

	// Fractal builder
	private GeometricPatternFractalGenerator<FractalLine> fractalGenerator;

	// Constructor: define listeners for mouse operations
	public FractalPanel() {
		setBackground(Color.cyan);
		setCurrentColor(Color.black);
		setLayout(null);
		addMouseListener(new SymMouse());
		addMouseMotionListener(new SymMouseMotion());
	}

	public void validate() {
		super.validate();
		SwingUtilities.invokeLater(() -> repaint());
	}

	// Class handling click events
	class SymMouse extends MouseAdapter {
		public void mouseClicked(MouseEvent event) {
			// EDIT MODE!
			if (mode == FractalPanel.MODE_PATTERN) {
				// Definition of the first point of a FractalLine
				if (choosingStartLine) {
					editA = event.getPoint();
					choosingStartLine = false;
					return;
				}

				// Definition of the second point of a FractalLine
				if (!choosingStartLine) {
					editB = event.getPoint();
					FractalLine temp = new FractalLine(editA.x, editA.y, editB.x, editB.y, color.getRGB());
					if (definingBase) {
						base = temp;
						definingBase = false;
					} else
						patterns.add(temp);
					choosingStartLine = true;
					return;
				}
			}
		}
	}

	// Class handling mouse move events
	class SymMouseMotion extends MouseMotionAdapter {
		public void mouseMoved(MouseEvent event) {
			if (choosingStartLine) return;
			editB = event.getPoint();
			repaint();
		}
	}

	public void setCurrentColor(Color c) {
		color = c;
	}

	// paint method redefinition
	public void paintComponent(Graphics g) {
		g.setPaintMode();

		if (mode == FractalPanel.MODE_PATTERN || !onlyLastIter) {
			if (base != null)
				drawLine(g, base);
			patterns.stream().forEach(line -> drawLine(g, line));
		}

		if (!choosingStartLine && mode == FractalPanel.MODE_PATTERN)
			drawLine(g, editA.x, editA.y, editB.x, editB.y, color);

		if (mode == FractalPanel.MODE_FRACTAL)
			fractalGenerator.getFractal().parallelStream().forEach(line -> drawLine(g, line));
	}

	private void drawLine(Graphics g, FractalLine line) {
		drawLine(g, (int)line.Ax, (int)line.Ay, (int)line.Bx, (int)line.By, new Color(line.rgbColorValue));
	}

	private void drawLine(Graphics g, int Ax, int Ay, int Bx, int By, Color color) {
		g.setColor(color);
		g.drawLine(Ax, Ay, Bx, By);
	}

	// Interface with users to set definingBase state
	public void startBaseDefinition() {
		definingBase = true;
		choosingStartLine = true;
		mode = FractalPanel.MODE_PATTERN;
	}

	// Reset all the information contained in the panel related to the fractal
	public void clear() {
		fractalGenerator.stop();

		// Reset data
		base = null;
		patterns.clear();
		fractalGenerator.getFractal().clear();

		// Reset state
		startBaseDefinition();
		repaint();
	}

	public void show(int newMode) {
		mode = newMode;
		repaint();
	}

	@Override
	public void showPattern() {
	}

	@Override
	public void showFractal() {
	}

	// Interface with users to create fractal
	public void computeFractal(int numIter, boolean onlyLastIter, Consumer<Float> progressWriter) {
		this.onlyLastIter = onlyLastIter;
		fractalGenerator = new GeometricPatternFractalGenerator<FractalLine>(base, patterns, numIter, onlyLastIter, progressWriter);
		fractalGenerator.generateFractal();

	}
}


