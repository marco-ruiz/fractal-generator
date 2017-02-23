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

import org.bop.fractals.progress.ThresholdProgressUpdater;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public class GeometricPatternFractalGenerator<SHAPE_T extends Shape<SHAPE_T>> extends GeometricFractalGenerator<SHAPE_T> {

	private Map<Integer, List<SHAPE_T>> iterOuts = new HashMap<>();
	protected List<SHAPE_T> patterns;
	protected int numIter;
	protected boolean lastIterOnly;

	private ThresholdProgressUpdater syncProgressUpdater;

	public GeometricPatternFractalGenerator(SHAPE_T base, List<SHAPE_T> patterns, int numIter, boolean lastIterOnly, Consumer<Float> progressWriter) {
		this.patterns = patterns;
		this.numIter = numIter;
		this.lastIterOnly = lastIterOnly;
		this.syncProgressUpdater = new ThresholdProgressUpdater(progressWriter, calculateNumGeometriesToCompute(), 2);

		this.patterns.stream().forEach(patternUnit -> patternUnit.computeConstants(base));
		setProgressUpdater(syncProgressUpdater);
	}

	protected long calculateNumGeometriesToCompute() {
		int numPatterns = patterns.size();
		return (long) ((numPatterns * (Math.pow(numPatterns, numIter) - 1) / (numPatterns - 1)) - numPatterns);
	}

	protected void buildFractalShapes() {
		iterOuts.computeIfAbsent(0, ArrayList::new).addAll(patterns);
		IntStream.range(1, numIter).forEach(this::computeIter);

		computedGeometries = (lastIterOnly) ?
			iterOuts.get(numIter - 1) :
			iterOuts.values().stream()
					.flatMap(List::stream)
					.collect(Collectors.toList());
	}

	private void computeIter(int iterNum) {
		List<SHAPE_T> prevIterOuts = iterOuts.get(iterNum - 1);
		List<SHAPE_T> currIterOuts =
				patterns.parallelStream()
					.map(pattern -> computeEquivalencesOf(prevIterOuts, pattern))
					.flatMap(List::stream)
					.collect(Collectors.toList());
		iterOuts.put(iterNum, currIterOuts);
	}

	private List<SHAPE_T> computeEquivalencesOf(List<SHAPE_T> prevIterOuts, SHAPE_T pattern) {
		return prevIterOuts.stream()
			.filter(relBase -> !interrupted)
			.map(relBase -> relBase.computeGeometryEquivalentTo(pattern))
			.peek(equiv -> syncProgressUpdater.incrementGenerated())
			.collect(Collectors.toList());
	}
}

