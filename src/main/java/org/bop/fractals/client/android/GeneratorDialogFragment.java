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

import org.bop.fractals.GeometricPatternFractalGenerator;
import org.bop.fractals.line.FractalLine;

import android.util.Log;

/**
 * @author Marco Ruiz
 * @since Feb 25, 2017
 */
public class GeneratorDialogFragment extends ValuePickerDialogFragment<Integer> {

	private static final String TAG = GeneratorDialogFragment.class.getName();

	public GeneratorDialogFragment() {
		super("How many recursions ?", 3, 4, 5, 6, 7, 8, 9);
	}

	public void onValueSelected(int index, Integer recursions) {
		EditPatternActivity activity = (EditPatternActivity) getActivity();
		Log.d(TAG, "Generating fractal...");
		List<FractalLine> pattern = new ArrayList<FractalLine>(activity.getFractalPattern());
		GeometricPatternFractalGenerator<FractalLine> generator =
				new GeometricPatternFractalGenerator<FractalLine>(pattern, recursions, false, null);
		generator.generateFractalSync();
		pattern.addAll(generator.getFractal());
		activity.setFractalLines(pattern);
		Log.d(TAG, "!!! Generated " + generator.getFractal().size() + " lines!");
	}
}
