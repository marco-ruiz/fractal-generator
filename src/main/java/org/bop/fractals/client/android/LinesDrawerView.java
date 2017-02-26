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

package org.bop.fractals.client.android;

import java.util.ArrayList;
import java.util.List;

import org.bop.fractals.line.FractalLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * @author Marco Ruiz
 * @since Feb 25, 2017
 */
public class LinesDrawerView extends View {

	private static final String TAG = "LinesDrawerView";

	protected Paint paint = new Paint();
	protected List<FractalLine> lines = new ArrayList<FractalLine>();

	public LinesDrawerView(Context context) {
		super(context);

		setBackgroundColor(Color.BLACK);
		setFocusable(true);
		setFocusableInTouchMode(true);

		paint.setAntiAlias(true);
	}

	public void clearLines() {
		setLines(new ArrayList<FractalLine>());
	}

	public List<FractalLine> getLines() {
		return lines;
	}

	public void setLines(List<FractalLine> lines) {
		this.lines = lines;
	}

	@Override
	public void onDraw(Canvas canvas) {
		paint.setColor(Color.WHITE);

/*
		Log.d(TAG, "Drawing " + lines.size() + " lines!");
		float[] array = getLinesAsFloatArray();
		canvas.drawLines(array, paint);
*/

		for (FractalLine line : lines) {
			Log.d(TAG, "Drawing line: (" + line.Ax + "," + line.Ay + ") <-> ("+ line.Bx + + line.By + ")");
			canvas.drawLine(line.Ax, line.Ay, line.Bx, line.By, paint);
		}
	}

	public float[] getLinesAsFloatArray() {
		int baseIndex = 0;
		FractalLine line;
		float[] result = new float[lines.size() * 4];

		for (int idx = 0; idx < lines.size(); idx++) {
			baseIndex = idx * 4;
			line = lines.get(idx);
			result[baseIndex + 0] = line.Ax;
			result[baseIndex + 1] = line.Ay;
			result[baseIndex + 2] = line.Bx;
			result[baseIndex + 3] = line.By;
		}

		return result;
	}
}
