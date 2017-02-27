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

package org.bop.fractals.line;

import org.bop.fractals.Shape;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class FractalLine implements Shape<FractalLine> {

	public final float Ax;
	public final float Ay;
	public final float Bx;
	public final float By;

	public final int rgbColorValue;

	// Only pattern lines need PatternConstants. By default they are null.
	private PatternConstants kA = null;
	private PatternConstants kB = null;

	public FractalLine(float Ax, float Ay, float Bx, float By) {
		this(Ax, Ay, Bx, By, 0);
	}

	public FractalLine(float Ax, float Ay, float Bx, float By, int color) {
		this.Ax = Ax;
		this.Ay = Ay;
		this.Bx = Bx;
		this.By = By;
		this.rgbColorValue = color;
	}

	public void computeConstants(FractalLine baseLine) {
		kA = computeConstantsForPoint(baseLine, Ax, Ay);
		kB = computeConstantsForPoint(baseLine, Bx, By);
	}

	private PatternConstants computeConstantsForPoint(FractalLine base, float x, float y) {
		float m1, m2;
		float dx, dy, dax, day;
		float D;
		float Cx, Cy;

		dx = base.Bx - base.Ax;
		dy = base.By - base.Ay;

		dax = x - base.Ax;
		day = y - base.Ay;

		D = dx * dx + dy * dy;
		if ((D == 0) || (dx * dax + dy * day == 0) || (dx * dax + dy * day - D == 0)) {
			// TODO: Handle error
		}
		m1 = ((dx * day - dy * dax) / (dx * dax + dy * day));
		m2 = ((dx * day - dy * dax) / (dx * dax + dy * day - D));

		// Constants
		Cx = m2 / (m2 - m1);
		Cy = m1 * Cx;

		return new PatternConstants(Cx, Cy);
	}

	public FractalLine computeGeometryEquivalentTo(FractalLine patternLine) {
		PatternConstants constantsA = patternLine.kA;
		PatternConstants constantsB = patternLine.kB;

		float xDiff = Bx - Ax;
		float yDiff = By - Ay;

		float compAx = Ax + constantsA.x * xDiff - constantsA.y * yDiff;
		float compAy = Ay + constantsA.x * yDiff + constantsA.y * xDiff;
		float compBx = Ax + constantsB.x * xDiff - constantsB.y * yDiff;
		float compBy = Ay + constantsB.x * yDiff + constantsB.y * xDiff;

		return new FractalLine(compAx, compAy, compBx, compBy, patternLine.rgbColorValue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(Ax);
		result = prime * result + Float.floatToIntBits(Ay);
		result = prime * result + Float.floatToIntBits(Bx);
		result = prime * result + Float.floatToIntBits(By);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FractalLine other = (FractalLine) obj;
		if (Float.floatToIntBits(Ax) != Float.floatToIntBits(other.Ax))
			return false;
		if (Float.floatToIntBits(Ay) != Float.floatToIntBits(other.Ay))
			return false;
		if (Float.floatToIntBits(Bx) != Float.floatToIntBits(other.Bx))
			return false;
		if (Float.floatToIntBits(By) != Float.floatToIntBits(other.By))
			return false;
		return true;
	}
}

class PatternConstants {
	public final float x, y;

	public PatternConstants(float x, float y) {
		this.x = x;
		this.y = y;
	}
}


