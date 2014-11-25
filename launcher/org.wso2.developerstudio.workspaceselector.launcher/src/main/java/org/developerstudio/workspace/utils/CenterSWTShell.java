/*
* Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.developerstudio.workspace.utils;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/**
 * This class would Center an SWT shell in the current display
 */
public class CenterSWTShell {

	public static final int DIVIDEND_FOR_HALVING = 2;

	/**
	 * centers the given SWT shell on the current screen
	 *
	 * @param shell the SWT shell to be centered
	 */
	public static void centerShellInDisplay(Shell shell) {
		if (shell != null) {
			final Rectangle surroundingBounds = getSurroundingBounds(shell);
			final Rectangle shellBounds = shell.getBounds();
			final int leftMargin = surroundingBounds.x + (surroundingBounds.width - shellBounds.width) / DIVIDEND_FOR_HALVING;
			final int topMargin = surroundingBounds.y + (surroundingBounds.height - shellBounds.height) / DIVIDEND_FOR_HALVING;
			shell.setLocation(leftMargin, topMargin);
		}
	}

	/**
	 * get the surrounding bounds for the shell detecting the active shell for user
	 * @param shell the SWT shell to be centered
	 * @return the rectangle for the active monitor bounds
	 */
	private static Rectangle getSurroundingBounds(final Shell shell) {
		return shell.getParent() != null ? shell.getParent().getBounds() : getClosestMonitor(shell.getDisplay()).getBounds();
	}

	/**
	 * get the active monitor from the set of monitors available, and check which monitor user has the cursor
	 * we decide the current monitor by cursor
	 */
	public static Monitor getClosestMonitor(final Display toSearch) {
		Point toFind = Display.getCurrent().getCursorLocation();
		Monitor activeMonitor = toSearch.getPrimaryMonitor();
		for (final Monitor current : toSearch.getMonitors()) {
			final Rectangle clientArea = current.getBounds();
			if (clientArea.contains(toFind)) {
				activeMonitor =  current;
			}
		}
		return activeMonitor;
	}
}
