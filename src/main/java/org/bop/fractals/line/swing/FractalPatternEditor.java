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

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class FractalPatternEditor {
/*
	// Constants to define the values that define the line
	static int VALUE_AX = 0;
	static int VALUE_AY = 1;
	static int VALUE_BX = 2;
	static int VALUE_BY = 3;

	public FractalLine base = null;
	public List<FractalLine> pattern = new ArrayList<FractalLine>();

	public void addPattern(float aX, float aY, float bX, float bY, Color c) {
		pattern.add(createLine(aX, aY, bX, bY, c));
	}

	public void editBase(float aX, float aY, float bX, float bY, Color c) {
		base = createLine(aX, aY, bX, bY, c);
	}

	private FractalLine createLine(float aX, float aY, float bX, float bY, Color c) {
		return new FractalLine(new FractalPoint(aX, aY), new FractalPoint(bX, bY), c);
	}

	public void editBase(Color c) {
		base.color = c;
	}

	public void editBase(int type, float value) {
		editLine(base, type, value);
	}

	public void invertBase() {
		float tempX = base.A.x;
		float tempY = base.A.y;

		base.A.x = base.B.x;
		base.A.y = base.B.y;
		base.B.x = tempX;
		base.B.y = tempY;
	}

	public void invertPattern(int index) {
		float tempX = pattern.get(index).A.x;
		float tempY = pattern.get(index).A.y;

		pattern.get(index).A.x = pattern.get(index).B.x;
		pattern.get(index).A.y = pattern.get(index).B.y;
		pattern.get(index).B.x = tempX;
		pattern.get(index).B.y = tempY;
	}

	public void editPattern(int index, Color c) {
		pattern.get(index).color = c;
	}

	public void editPattern(int index, int type, float value) {
		editLine(pattern.get(index), type, value);
	}

	private void editLine(FractalLine line, int type, float value) {
		if (type == VALUE_AX) line.A.x = value;
		if (type == VALUE_AY) line.A.y = value;
		if (type == VALUE_BX) line.B.x = value;
		if (type == VALUE_BY) line.B.y = value;
	}

	public void deletePattern(int index) {
		pattern.remove(index);
	}
*/
}
