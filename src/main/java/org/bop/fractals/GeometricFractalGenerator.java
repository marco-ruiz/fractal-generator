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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bop.fractals.progress.IProgressInspector;
import org.bop.fractals.progress.IProgressListener;
import org.bop.fractals.progress.IProgressUpdater;
import org.bop.fractals.progress.PollingProgressUpdater;

/**
 * @author Marco Ruiz
 * @since Feb 21, 2017
 */
public abstract class GeometricFractalGenerator<SHAPE_T> implements Runnable {

	private static ExecutorService generatorService = Executors.newFixedThreadPool(1);

	private IProgressUpdater progressUpdater;
	protected List<SHAPE_T> computedShapes = new ArrayList<SHAPE_T>();

	protected boolean interrupted = false;
	protected AtomicBoolean computing = new AtomicBoolean(false);
	private long totalNumGeometries;

	public GeometricFractalGenerator() {
		this((IProgressUpdater)null);
	}

	public GeometricFractalGenerator(IProgressListener progressListener) {
		IProgressInspector progressInspector = new IProgressInspector() {
			public float inspectCurrentProgress() { return getPercentageProgress(); }
		};
		setProgressUpdater(new PollingProgressUpdater(progressInspector, progressListener));
	}

	public GeometricFractalGenerator(IProgressUpdater progressUpdater) {
		setProgressUpdater(progressUpdater);
	}

	public void setProgressUpdater(IProgressUpdater progressUpdater) {
		this.progressUpdater = progressUpdater;
	}

	public void generateFractal() {
		if (!computing.get()) generatorService.submit(this);
	}

	public final void run() {
		computing.set(true);
		totalNumGeometries = calculateNumGeometriesToCompute();
		computedShapes = new ArrayList<SHAPE_T>();
		progressUpdater.start();
		buildFractalShapes();
		progressUpdater.updateComplete();
		computing.set(false);
	}

	protected abstract void buildFractalShapes();
	protected abstract long calculateNumGeometriesToCompute();

	public void reset() {
		computedShapes.clear();
	}

	public void stop() {
		interrupted = true;
	}

	public List<SHAPE_T> getFractal() {
		return computedShapes;
	}

	public float getPercentageProgress() {
		return 100 * computedShapes.size() / totalNumGeometries;
	}

	public long getTotalNumGeometries() {
		return totalNumGeometries;
	}
}

