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

import java.awt.Color;

import org.bop.fractals.GeometricPattern;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class FractalLine implements GeometricPattern<FractalLine> {
	private static final int POINT_START = 0;
	private static final int POINT_END = 1;

	public final FractalPoint A;
	public final FractalPoint B;
	public final Color color;

	private float k1, k2, k3, k4;

	public FractalLine(FractalPoint a, FractalPoint b) {
		this(a, b, null);
	}

	public FractalLine(FractalPoint a, FractalPoint b, Color c) {
		A = new FractalPoint(a.x, a.y);
		B = new FractalPoint(b.x, b.y);
		color = c;
	}

	public void computeConstants(FractalLine baseLine) {
		computeConstants(baseLine, A, FractalLine.POINT_START);
		computeConstants(baseLine, B, FractalLine.POINT_END);
	}

	private void computeConstants(FractalLine baseLine, FractalPoint a, int type) {
		float m1, m2;
		float dx, dy, dax, day;
		float D;
		float c1, c2;

		dx = (baseLine.B.x - baseLine.A.x);
		dy = (baseLine.B.y - baseLine.A.y);
		dax = (a.x - baseLine.A.x);
		day = (a.y - baseLine.A.y);
		D = dx * dx + dy * dy;
		if ((D == 0) || (dx * dax + dy * day == 0) || (dx * dax + dy * day - D == 0)) {
			// TODO: Handle error
		}
		m1 = ((dx * day - dy * dax) / (dx * dax + dy * day));
		m2 = ((dx * day - dy * dax) / (dx * dax + dy * day - D));

		// Constants
		c1 = m2 / (m2 - m1);
		c2 = m1 * c1;

		// Setting the appropiates constants
		if (type == FractalLine.POINT_START) {
			k1 = c1;
			k2 = c2;
		} else {
			k3 = c1;
			k4 = c2;
		}
	}

	public FractalLine computeGeometryEquivalentTo(FractalLine relativeBase) {
		float Ax = A.x + relativeBase.k1 * (B.x - A.x) - relativeBase.k2 * (B.y - A.y);
		float Ay = A.y + relativeBase.k1 * (B.y - A.y) + relativeBase.k2 * (B.x - A.x);
		float Bx = A.x + relativeBase.k3 * (B.x - A.x) - relativeBase.k4 * (B.y - A.y);
		float By = A.y + relativeBase.k3 * (B.y - A.y) + relativeBase.k4 * (B.x - A.x);

		return new FractalLine(new FractalPoint(Ax, Ay), new FractalPoint(Bx, By), relativeBase.color);
	}
}

