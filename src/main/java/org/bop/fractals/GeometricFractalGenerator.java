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
import java.util.List;
import java.util.function.Consumer;

import org.bop.fractals.progress.IProgressUpdater;
import org.bop.fractals.progress.PollingProgressUpdater;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public abstract class GeometricFractalGenerator<GEOMETRY_T> implements Runnable {

	private IProgressUpdater progressUpdater;
	protected List<GEOMETRY_T> computedGeometries = new ArrayList<>();

	protected boolean interrupted = false;
	protected boolean computing = false;
	private long totalNumGeometries;

	public GeometricFractalGenerator() {
		this((IProgressUpdater)null);
	}

	public GeometricFractalGenerator(Consumer<Float> progressWriter) {
		setProgressUpdater(new PollingProgressUpdater(() -> getPercentageProgress(), progressWriter));
	}

	public GeometricFractalGenerator(IProgressUpdater progressUpdater) {
		setProgressUpdater(progressUpdater);
	}

	public void setProgressUpdater(IProgressUpdater progressUpdater) {
		this.progressUpdater = progressUpdater;
	}

	public void run() {
		computing = true;
		totalNumGeometries = computeNumGeometriesToCompute();
		computedGeometries = new ArrayList<>();
		progressUpdater.start();
		buildFractal();
		computing = false;

		progressUpdater.updateComplete();
	}

	public abstract void buildFractal();
	public abstract long computeNumGeometriesToCompute();

	public boolean isComputing() {
		return computing;
	}

	public void reset() {
		computedGeometries.clear();
	}

	public void stop() {
		interrupted = true;
	}

	public List<GEOMETRY_T> getFractal() {
		return computedGeometries;
	}

	public float getPercentageProgress() {
		return 100 * computedGeometries.size() / totalNumGeometries;
	}

	public long getTotalNumGeometries() {
		return totalNumGeometries;
	}
}
