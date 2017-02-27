package org.bop.fractals.client.android;

import java.util.ArrayList;
import java.util.List;

import org.bop.fractals.line.FractalLine;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class EditPatternActivity extends Activity {

	private static final String TAG = EditPatternActivity.class.getName();

	private LinesEditorView patternView;
	private LinesDrawerView fractalView;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_fractal_pattern, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.color : 	return showDialog(new ColorPickerDialogFragment());

			case R.id.clear : 	patternView.clearLines();
			case R.id.edit : 	switchToView(patternView);
								return true;

			case R.id.generate : 	return showDialog(new GeneratorDialogFragment());

			case R.id.fractal : 	switchToView(fractalView);
									return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private boolean showDialog(DialogFragment dialogFrag) {
		dialogFrag.show(getFragmentManager(), dialogFrag.getClass().getName());
		return true;
	}

	private void switchToView(LinesDrawerView view) {
		setContentView(view);
		view.requestFocus();
	}

	public void setCurrentColor(int color) {
		patternView.setCurrentColor(color);
	}

	public ArrayList<FractalLine> getFractalPattern() {
		return new ArrayList<FractalLine>(patternView.getLines());
	}

	public void setFractalLines(List<FractalLine> lines) {
		fractalView.setLines(lines);
		switchToView(fractalView);
	}
}
