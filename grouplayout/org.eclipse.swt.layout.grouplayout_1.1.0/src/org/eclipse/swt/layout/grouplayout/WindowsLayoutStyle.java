/*
 * Copyright (C) 2005 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */ 

package org.eclipse.swt.layout.grouplayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * An implementation of <code>LayoutStyle</code> for the Windows look and feel.
 * This information comes from:
 * http://msdn.microsoft.com/library/default.asp?url=/library/en-us/dnwue/html/ch14e.asp
 *
 * @version $Revision: 1.4 $
 */
class WindowsLayoutStyle extends LayoutStyle {
	protected static final int HORIZONTAL_DIALOG_UNIT_PER_CHAR = 4;
	protected static final int VERTICAL_DIALOG_UNITS_PER_CHAR = 8;
	protected FontMetrics m_fontMetrics;
	//
    public int getPreferredGap(Class source, int sourceStyle, Class target, int targetStyle,
            int type, int position) {
        // Invoke super to check arguments.
        super.getPreferredGap(source, sourceStyle, target, targetStyle, type, position);

        if (type == INDENT) {
            if (position == SWT.LEFT || position == SWT.RIGHT) {
                return 10;
            }
            // Treat vertical INDENT as RELATED
            type = RELATED;
        }
        if (type == UNRELATED) {
            // Between unrelated controls: 7
            return dlusToPixels(7, position);
        }
        else { //type == RELATED
            boolean sourceLabel = source.isAssignableFrom(Label.class);
            boolean targetLabel = target.isAssignableFrom(Label.class);

            if (((sourceLabel && !targetLabel) ||
                 (targetLabel && !sourceLabel)) &&
                (position == SWT.LEFT ||
                 position == SWT.RIGHT)) {
                return dlusToPixels(3, position);
            }
            // Between related controls: 4
            return dlusToPixels(4, position);
        }
    }

    public int getContainerGap(Class controlClass, int controlStyle, int position) {
        super.getContainerGap(controlClass, controlStyle, position);
        return dlusToPixels(7, position);
    }
    
    private int dlusToPixels(int dlus, int direction) {
		if (direction == SWT.LEFT || direction == SWT.RIGHT) {
			return (getDefaultFontMetrics().getAverageCharWidth() * dlus + HORIZONTAL_DIALOG_UNIT_PER_CHAR / 2)
					/ HORIZONTAL_DIALOG_UNIT_PER_CHAR;
		} else if (direction == SWT.UP || direction == SWT.DOWN) {
			return (getDefaultFontMetrics().getHeight() * dlus + VERTICAL_DIALOG_UNITS_PER_CHAR / 2)
					/ VERTICAL_DIALOG_UNITS_PER_CHAR;
		}
		throw new IllegalArgumentException("direction = " + direction);
	}

    protected FontMetrics getDefaultFontMetrics() {
		if (m_fontMetrics == null) {
			Font font;
			Display currentDisplay = Display.getCurrent();
			Shell shell = new Shell();
			if (currentDisplay == null) {
				font = new Font(null, shell.getFont().getFontData());
			} else {
				font = new Font(currentDisplay, currentDisplay.getSystemFont().getFontData());
			}
			GC gc = new GC(shell);
			gc.setFont(font);
			m_fontMetrics = gc.getFontMetrics();
			gc.dispose();
			shell.dispose();
			font.dispose();
		}
		return m_fontMetrics;
	}
}
