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

package org.bop.fractals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class GeometricPatternFractalBuilder<GEOMETRY_T extends GeometricPattern<GEOMETRY_T>> extends GeometricFractalBuilder<GEOMETRY_T> {

	protected List<GEOMETRY_T> patterns;
	protected int numIter;
	protected boolean lastIterOnly;

	public GeometricPatternFractalBuilder(GEOMETRY_T base, List<GEOMETRY_T> patterns, int numIter, boolean outputLastIterOnly, Consumer<Double> progressRepainter) {
		super(progressRepainter);
		this.patterns = patterns;
		this.numIter = numIter;
		this.lastIterOnly = outputLastIterOnly;

		this.patterns.stream().forEach(patternUnit -> patternUnit.computeConstants(base));
	}

	public long computeNumGeometriesToCompute() {
		int numPatterns = patterns.size();
		return (long) ((numPatterns * (Math.pow(numPatterns, numIter) - 1) / (numPatterns - 1)) - numPatterns);
	}

/*
	public void buildFractal2() {
		patterns.stream().forEach(pattern -> buildFractal(pattern, numIter));
	}

	// Recursive method to create fractal based on base FractalLine, "lines" vector "num_lines" value (size of "lines") and number of iterations
	public int buildFractal(GEOMETRY_T relBase, int numIter) {
		GEOMETRY_T equivalentGeometry;

		if (numIter == 1 || interrupted) return 0;

		for (int idx = 0; idx < patterns.size(); idx++) {
			equivalentGeometry = relBase.computeGeometryEquivalentTo(patterns.get(idx));

			if (!lastIterOnly || numIter == 2)
				output.add(equivalentGeometry);

			if (buildFractal(equivalentGeometry, numIter - 1) == 1) return 1;
		}
		return 0;
	}
*/

	private Map<Integer, List<GEOMETRY_T>> iterOuts = new HashMap<>();

	public void buildFractal() {
		iterOuts.computeIfAbsent(0, ArrayList::new).addAll(patterns);
		IntStream.range(1, numIter).forEach(this::computeIter);

		output = (lastIterOnly) ?
			iterOuts.get(numIter - 1) :
			iterOuts.values().stream()
					.flatMap(List::stream)
					.collect(Collectors.toList());
	}

	private void computeIter(int iterNum) {
		List<GEOMETRY_T> prevIterOuts = iterOuts.get(iterNum - 1);
		List<GEOMETRY_T> currIterOuts =
				patterns.parallelStream()
					.map(pattern -> computeEquivalencesOf(prevIterOuts, pattern))
					.flatMap(List::stream)
					.collect(Collectors.toList());
		iterOuts.put(iterNum, currIterOuts);
	}

	private List<GEOMETRY_T> computeEquivalencesOf(List<GEOMETRY_T> prevIterOuts, GEOMETRY_T pattern) {
		return prevIterOuts.stream()
			.filter(relBase -> !interrupted)
			.map(relBase -> relBase.computeGeometryEquivalentTo(pattern))
			.collect(Collectors.toList());
	}
}


