package org.bop.fractals.client.android;

import java.util.ArrayList;
import java.util.List;

import org.bop.fractalAndroid.R;
import org.bop.fractals.GeometricPatternFractalGenerator;
import org.bop.fractals.line.FractalLine;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class EditFractalPattern extends Activity {

	private static final String TAG = "EditFractalPattern";

	private LinesEditorView patternView;
	private LinesDrawerView fractalView;
	private GeometricPatternFractalGenerator<FractalLine> generator;
	private int recursions = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set full screen view
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Lock screen orientation (stops screen clearing when rotating phone)
        setRequestedOrientation(getResources().getConfiguration().orientation);

        // Attach lines editor view
        patternView = new LinesEditorView(this);
        fractalView = new LinesDrawerView(this);
        switchToView(patternView);
	}

	private void switchToView(LinesDrawerView view) {
		setContentView(view);
		view.requestFocus();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_fractal_pattern, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.rec3:
			case R.id.rec4:
			case R.id.rec5:
			case R.id.rec6:
				recursions = Integer.parseInt((String) item.getTitle());
				return true;

			case R.id.clear :
				patternView.clearLines();

			case R.id.edit :
				switchToView(patternView);
				return true;

			case R.id.generate :
				generateFractal();

			case R.id.fractal :
				switchToView(fractalView);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void generateFractal() {
		Log.d(TAG, "Generating fractal...");
		List<FractalLine> auxLineList = new ArrayList<FractalLine>(patternView.getLines());
		FractalLine base = auxLineList.remove(0);
		generator = new GeometricPatternFractalGenerator<FractalLine>(base, auxLineList, recursions, false, null);
		generator.generateFractalSync();
		auxLineList.add(base);
		auxLineList.addAll(generator.getFractal());
		fractalView.setLines(auxLineList);
		Log.d(TAG, "!!! Generated " + generator.getFractal().size() + " lines!");
	}

}
