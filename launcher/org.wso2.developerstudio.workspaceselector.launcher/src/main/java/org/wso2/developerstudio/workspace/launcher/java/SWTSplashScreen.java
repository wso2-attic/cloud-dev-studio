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

package org.wso2.developerstudio.workspace.launcher.java;

import org.developerstudio.workspace.utils.SWTResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * generating the splash screen for starting the dev-studio
 * using SWT
 */
public class SWTSplashScreen {
	static final Display splashScreenDisplay =  Display.getDefault();

	private static final Logger logger = LoggerFactory.getLogger(SWTSplashScreen.class);
	public static final int DIVIDEND_TO_GET_MIDDLE = 2; //dividend to get the midpoint of the screen, hence dividing by 2
	Shell splashScreenShell = new Shell(splashScreenDisplay, SWT.NONE);
	ProgressBar progressBar = new ProgressBar(splashScreenShell, SWT.SMOOTH);
	Label imageLabel = new Label(splashScreenShell, SWT.FILL);
	public int MAXIMUM_PROGRESS = 0;

	public SWTSplashScreen(String splashImageLoc, int shellWidth, int shellHeight,
	                       int maximumProgress, int imageLabelWidth, int imageLabelHeight,
	                       int progressBarWidth, int progressBarHeight, int progressBarXLoc, int progressBarYLoc) {

		MAXIMUM_PROGRESS = maximumProgress;
		splashScreenShell.setSize(shellWidth, shellHeight);
		splashScreenShell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		// making the workSpaceSelectorShell appear in the center of the monitor
		Monitor primary = splashScreenDisplay.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = splashScreenShell.getBounds();

		int shellXLoc = bounds.x + (bounds.width - rect.width) / DIVIDEND_TO_GET_MIDDLE;
		int shellYLoc = bounds.y + (bounds.height - rect.height) / DIVIDEND_TO_GET_MIDDLE;

		splashScreenShell.setLocation(shellXLoc, shellYLoc);
		splashScreenShell.open();

		if(SWTResourceManager.getImage(splashImageLoc) != null){
			init(splashImageLoc, imageLabelWidth, imageLabelHeight, progressBarWidth, progressBarHeight, progressBarXLoc, progressBarYLoc);
		}else { // if image is null then create a splash screen with a blank view
			logger.error("Splash Screen image resource is not available at " + splashImageLoc);
			init(progressBarWidth, progressBarHeight, progressBarXLoc, progressBarYLoc);
		}
	}
	/**
	 * initiate the view components
	 *
	 */
	private void init(int progressBarWidth, int progressBarHeight, int progressBarXLoc, int progressBarYLoc) {

		progressBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		progressBar.setMaximum(MAXIMUM_PROGRESS);
		progressBar.setBounds(progressBarXLoc, progressBarYLoc, progressBarWidth, progressBarHeight);//designed through eclipse window builder

		splashScreenShell.pack();
		logger.info("Splash Screen Initiated");
	}

	private void init(String splashImageLocation, int imageLabelWidth, int imageLabelHeight,
	                  int progressBarWidth, int progressBarHeight, int progressBarXLoc, int progressBarYLoc) {
		int imageLabelXLoc = 0; //image label should always fill the splashscreen and hence X, Y coordinates should always be zero.
		int imageLabelYLoc = 0;

		imageLabel.setAlignment(SWT.CENTER);
		imageLabel.setImage(SWTResourceManager.getImage(splashImageLocation));
		imageLabel.setBounds(imageLabelXLoc, imageLabelYLoc, imageLabelWidth, imageLabelHeight);//designed through eclipse window builder
		init(progressBarWidth, progressBarHeight, progressBarXLoc, progressBarYLoc);
	}

	/**
	 * update the progress bar of the splash screen
	 * @param updateVal: the value that is set to the progress bar
	 */
	public void updateProgress(int updateVal) {

		if( updateVal < MAXIMUM_PROGRESS){
			progressBar.setSelection(updateVal);
			progressBar.setState(updateVal);
		}else {
			splashScreenShell.dispose(); //dispose the workSpaceSelectorShell when progress exceeds 1000
		}
	}
}