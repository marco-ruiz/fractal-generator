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

import org.bop.fractals.line.FractalLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author Marco Ruiz
 * @since Feb 25, 2017
 */
public class LinesEditorView extends LinesDrawerView implements OnTouchListener {

	private static final String TAG = LinesEditorView.class.getName();

	private PointF editA, editB;
	private int currColor = Color.WHITE;

	public LinesEditorView(Context context) {
		super(context);
		setOnTouchListener(this);
		restartEdit();
	}

	public void restartEdit() {
		editA = null;
		editB = null;
	}

	public void setCurrentColor(int color) {
		currColor = color;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();

		if (editA == null) {
			// editA not set yet
			if (action == MotionEvent.ACTION_DOWN) {
				editA = new PointF(event.getX(), event.getY());
				editB = editA;
				invalidate();
				return true;
			}
		} else {
			// editA is already set... deciding where to lift up editB
			if (action == MotionEvent.ACTION_MOVE) {
				editB = new PointF(event.getX(), event.getY());
				invalidate();
				return true;
			}
			if (action == MotionEvent.ACTION_UP) {
				editB = new PointF(event.getX(), event.getY());
				lines.add(new FractalLine(editA.x, editA.y, editB.x, editB.y, currColor));
				restartEdit();
				invalidate();
				return true;
			}
		}

		return false;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (editA != null) {
			paint.setColor(currColor);
			canvas.drawLine(editA.x, editA.y, editB.x, editB.y, paint);
		}
	}
}



