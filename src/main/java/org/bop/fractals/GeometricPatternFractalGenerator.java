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

	private SHAPE_T base;
	protected List<SHAPE_T> patterns;
	protected int numRecursions;
	protected boolean lastIterOnly;

	private ThresholdProgressUpdater syncProgressUpdater;
	private Map<Integer, List<SHAPE_T>> shapesByRecursionLevel = new HashMap<>();

	public GeometricPatternFractalGenerator(SHAPE_T base, List<SHAPE_T> patterns, int numIter, boolean lastIterOnly, Consumer<Float> progressWriter) {
		setBase(base);
		setPatterns(patterns);
		setNumRecursions(numIter);
		setLastIterOnly(lastIterOnly);
		this.syncProgressUpdater = new ThresholdProgressUpdater(progressWriter, calculateNumGeometriesToCompute(), 2);

		this.patterns.stream().forEach(patternUnit -> patternUnit.computeConstants(base));
		setProgressUpdater(syncProgressUpdater);
	}

	public SHAPE_T getBase() {
		return base;
	}

	public void setBase(SHAPE_T base) {
		this.base = base;
	}

	public List<SHAPE_T> getPatterns() {
		return patterns;
	}

	public void setPatterns(List<SHAPE_T> patterns) {
		this.patterns = patterns;
	}

	public void addPattern(SHAPE_T pattern) {
		this.patterns.add(pattern);
	}

	public void setNumRecursions(int numIter) {
		this.numRecursions = numIter;
	}

	public void setLastIterOnly(boolean lastIterOnly) {
		this.lastIterOnly = lastIterOnly;
	}

	public void setProgressListener(Consumer<Float> progressListener) {
		this.syncProgressUpdater = new ThresholdProgressUpdater(progressListener, calculateNumGeometriesToCompute(), 2);
	}

	protected long calculateNumGeometriesToCompute() {
		int numPatterns = patterns.size();
		return (long) ((numPatterns * (Math.pow(numPatterns, numRecursions) - 1) / (numPatterns - 1)) - numPatterns);
	}

	public List<SHAPE_T> getFractal(int recursionLevel) {
		return shapesByRecursionLevel.get(recursionLevel);
	}

	protected void buildFractalShapes() {
		shapesByRecursionLevel.computeIfAbsent(0, ArrayList::new).addAll(patterns);
		IntStream.range(1, numRecursions).forEach(this::computeRecursionLevel);

		computedShapes = (lastIterOnly) ?
			shapesByRecursionLevel.get(numRecursions - 1) :
			shapesByRecursionLevel.values().stream()
					.flatMap(List::stream)
					.collect(Collectors.toList());
	}

	private void computeRecursionLevel(int recursionLevel) {
		List<SHAPE_T> prevRecShapes = shapesByRecursionLevel.get(recursionLevel - 1);
		List<SHAPE_T> currRecShapes =
				patterns.parallelStream()
					.map(pattern -> computeEquivalencesOf(prevRecShapes, pattern))
					.flatMap(List::stream)
					.collect(Collectors.toList());
		shapesByRecursionLevel.put(recursionLevel, currRecShapes);
	}

	private List<SHAPE_T> computeEquivalencesOf(List<SHAPE_T> prevRecShapes, SHAPE_T pattern) {
		return prevRecShapes.stream()
			.filter(relBase -> !interrupted)
			.map(relBase -> relBase.computeGeometryEquivalentTo(pattern))
			.peek(equiv -> syncProgressUpdater.incrementGenerated())
			.collect(Collectors.toList());
	}
}

