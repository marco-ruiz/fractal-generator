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
	private static final int POINT_START = 0;
	private static final int POINT_END = 1;

	private static boolean isStart(int pointType) {
		return pointType == FractalLine.POINT_START;
	}

	public final float Ax;
	public final float Ay;
	public final float Bx;
	public final float By;

	public final int rgbColorValue;

	private float k1, k2, k3, k4;

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
		computeConstants(baseLine, FractalLine.POINT_START);
		computeConstants(baseLine, FractalLine.POINT_END);
	}

	private void computeConstants(FractalLine base, int pointType) {
		float m1, m2;
		float dx, dy, dax, day;
		float D;
		float c1, c2;


		dx = base.Bx - base.Ax;
		dy = base.By - base.Ay;

		dax = (isStart(pointType) ? Ax : Bx) - base.Ax;
		day = (isStart(pointType) ? Ay : By) - base.Ay;

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
		if (isStart(pointType)) {
			k1 = c1;
			k2 = c2;
		} else {
			k3 = c1;
			k4 = c2;
		}
	}

	public FractalLine computeGeometryEquivalentTo(FractalLine relativeBase) {
		float xDiff = Bx - Ax;
		float yDiff = By - Ay;
		float compAx = Ax + relativeBase.k1 * xDiff - relativeBase.k2 * yDiff;
		float compAy = Ay + relativeBase.k1 * yDiff + relativeBase.k2 * xDiff;
		float compBx = Ax + relativeBase.k3 * xDiff - relativeBase.k4 * yDiff;
		float compBy = Ay + relativeBase.k3 * yDiff + relativeBase.k4 * xDiff;

		return new FractalLine(compAx, compAy, compBx, compBy, relativeBase.rgbColorValue);
	}
}

