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
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class BaseSwingApp extends JPanel {

    protected JFrame appFrame;

	public BaseSwingApp(String title, int width, int height) {
        appFrame = new JFrame(title);
        appFrame.setSize(width, height);
        appFrame.setVisible(true);
        appFrame.setContentPane(this);

        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { System.exit(1); }
        });
    }

	public void validate() {
		super.validate();
		SwingUtilities.invokeLater(() -> repaint());
	}

	public JButton addButton(String caption, int x, int y, int width, int height, ActionListener listener) {
		JButton btn = new JButton(caption);
		btn.setBackground(Color.lightGray);
		btn.addActionListener(listener);
		return addComponent(btn, x, y, width, height);
	}

	public <COMP_T extends JComponent> COMP_T addComponent(COMP_T component, int x, int y, int width, int height) {
		component.setBounds(x, y, width, height);
		add(component);
		component.setVisible(true);
		component.validate();
		return component;
	}
}
