package org.eclipse.swt.widgets.baseline;

import java.lang.reflect.Field;

import org.eclipse.swt.widgets.Control;

/**
 * Implementation for Cocoa SWT baseline.
 * 
 * @author mitin_aa
 */
public class CocoaBaseline extends Baseline {
	static {
		Library.loadLibrary("baseline-cocoa");
	}
	int fetchBaseline(Control control, int width, int height) {
		try {
			int baseline = _fetchBaseline(getID(control, "view"));
			if (baseline != -1) {
				return baseline;
			}
		} catch (Throwable e) {
			// ignore errors 
		}
		return super.fetchBaseline(control, width, height);
	}
	/**
	 * @return the Cocoa id field.
	 * @throws Exception
	 */
	private long getID(Control control, String name) throws Exception {
		Field handleField = Control.class.getDeclaredField(name);
		handleField.setAccessible(true);
		Object handleValue = handleField.get(control);
		Field idField = handleValue.getClass().getField("id");
		idField.setAccessible(true);
		Number idValue = (Number) idField.get(handleValue);
		return idValue.longValue();
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// native
	//
	////////////////////////////////////////////////////////////////////////////
	private native int _fetchBaseline(long widgetHandle);
}
