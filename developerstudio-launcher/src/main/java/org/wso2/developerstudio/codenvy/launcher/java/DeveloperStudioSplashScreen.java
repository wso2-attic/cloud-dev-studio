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

package org.wso2.developerstudio.codenvy.launcher.java;

import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Opening the splash screen window for wso2 dev studio
 */
public class DeveloperStudioSplashScreen {
	private static final String SPLASH_IMAGE = "SplashScreen.png";
	public static final String CLASS_NOT_FOUND_EXCEPTION = "Class not found Exception";
	public static final String INITIATION_EXCEPTION = "Initiation Exception";
	public static final String ILLEGAL_EXCEPTION = "Illegal Exception";
	public static final String UNSUPPORTED_LOOK_AND_FEEL_EXCEPTION =
			"Unsupported Look and Feel Exception";
	public static final int MAX_PROGRESS = 1000; // maximum progress value for the progress bar
	static SplashScreen screen;
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DeveloperStudioSplashScreen.class);

	private DeveloperStudioSplashScreen(int proLevel, String proMessage) {
		// initialize the splash screen
		splashScreenInit();
		screen.setProgress(proMessage, proLevel); // progress bar with the message
	}

	/**
	 * set the progress bar of the splash screen after initiation
	 * @param updateProLevel
	 * @param updateProMessage
	 */
	public static void updateProgress(int updateProLevel, String updateProMessage) {
		screen.setProgress(updateProMessage, updateProLevel);  // progress bar with the message
		if (updateProLevel >= MAX_PROGRESS) { // update if the progress value is less than the maximum progress
			screen.setScreenVisible(false);
			screen.dispose();
		}
	}

	/**
	 * initiate the splash screen
	 */
	private void splashScreenInit() {
		ImageIcon splashImage = null;
		if (this.getClass().getResource(SPLASH_IMAGE) != null) {
			splashImage = new ImageIcon(this.getClass().getResource(SPLASH_IMAGE));
		} else {
			logger.error("Image Resource could not be found for the Splash Screen Initiation");
		}
		screen = new SplashScreen(splashImage);
		screen.setLocationRelativeTo(null);
		screen.setProgressMax(MAX_PROGRESS); //maximum value of the progress bar is set
		screen.setScreenVisible(true);
	}

	public static void startUp(int progressLevel, String progressMessage) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			logger.error(CLASS_NOT_FOUND_EXCEPTION);
		} catch (InstantiationException e) {
			logger.error(INITIATION_EXCEPTION);
		} catch (IllegalAccessException e) {
			logger.error(ILLEGAL_EXCEPTION);
		} catch (UnsupportedLookAndFeelException e) {
			logger.error(UNSUPPORTED_LOOK_AND_FEEL_EXCEPTION);
		}
		new DeveloperStudioSplashScreen(progressLevel, progressMessage);
	}

}
