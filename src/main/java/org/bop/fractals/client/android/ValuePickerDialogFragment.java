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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

/**
 * @author Marco Ruiz
 * @since Feb 25, 2017
 */
public abstract class ValuePickerDialogFragment<VALUE_T> extends DialogFragment {

	private static final String TAG = ValuePickerDialogFragment.class.getName();

	private String title;
	private String[] strValues;
	protected VALUE_T[] values;

	private OnClickListener clickListener = new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int indexSelected) {
	    	onValueSelected(indexSelected, values[indexSelected]);
	    }
	};

	public ValuePickerDialogFragment(String title, VALUE_T... values) {
		this.title = (title != null) ? title : "";
		this.values = values;
		this.strValues = new String[values.length];
		for (int idx = 0; idx < values.length; idx++) {
			strValues[idx] = values[idx].toString();
		}
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	    builder.setTitle(title);
		builder.setItems(strValues, clickListener);
		return builder.create();
	}

	public abstract void onValueSelected(int index, VALUE_T value);
}
