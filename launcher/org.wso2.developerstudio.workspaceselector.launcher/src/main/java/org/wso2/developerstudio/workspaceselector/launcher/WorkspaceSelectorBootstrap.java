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
package org.wso2.developerstudio.workspaceselector.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.workspaceselector.utils.SplashScreenDesignParameters;

import java.io.File;

public class WorkspaceSelectorBootstrap {

	private static final Logger log = LoggerFactory.getLogger(WorkspaceSelectorBootstrap.class);
	private static final String STUDIO_ROOT_ENV_VAR_NAME = "WSO2_DEVELOPER_STUDIO_PATH";
	public static final String ROOT_DIR = System.getenv(STUDIO_ROOT_ENV_VAR_NAME);

	/**
	 * The optimal splash screen parameters obtained using Eclipse window builder
	 */
	private static final class SplashScreenParameters {
		/**
		 * Image location of the splash screen Image, This Image will be loaded on the splash screen
		 */
		public static final String SPLASH_IMAGE_LOCATION =
				ROOT_DIR + File.separator + "icons" + File.separator + "splash.png";
		/**
		 * maximum progress value for the progress bar
		 */
		public static final int MAXIMUM_PROGRESS = 1000;
		/**
		 * shell width value for the splash screen
		 */
		public static final int SHELL_WIDTH = 448;
		/**
		 * shell height value for the splash screen
		 */
		public static final int SHELL_HEIGHT = 270;
		/**
		 * Image label width, this is set according to the image loaded for the splash screen
		 */
		public static final int IMAGE_LABEL_WIDTH = 450;
		/**
		 * Image label height, this is set according to the image loaded for the splash screen
		 */
		public static final int IMAGE_LABEL_HEIGHT = 300;
		/**
		 * Progress bar width, this is set to span through the complete width of the splash screen shell
		 */
		public static final int PROGRESS_BAR_WIDTH = 450;
		/**
		 * splash screen progress bar height
		 */
		public static final int PROGRESS_BAR_HEIGHT = 20;
		/**
		 * Progress bar X location, the X coordinate to start the progress bar on the splash screen shell
		 */
		public static final int PROGRESS_BAR_X_LOC = 0;
		/**
		 * Progress bar Y loc, the Y coordinate from top of the splash screen shell t locate the progress bar
		 */
		public static final int PROGRESS_BAR_Y_LOC = 240;
	}

	public static void main(String args[]) {

		SplashScreenDesignParameters splashScreenDesignParameters = new SplashScreenDesignParameters();
		splashScreenDesignParameters.setSplashImageLoc(SplashScreenParameters.SPLASH_IMAGE_LOCATION);
		splashScreenDesignParameters.setImageLabelHeight(SplashScreenParameters.IMAGE_LABEL_HEIGHT);
		splashScreenDesignParameters.setImageLabelWidth(SplashScreenParameters.IMAGE_LABEL_WIDTH);
		splashScreenDesignParameters.setMaximumProgress(SplashScreenParameters.MAXIMUM_PROGRESS);
		splashScreenDesignParameters.setShellHeight(SplashScreenParameters.SHELL_HEIGHT);
		splashScreenDesignParameters.setShellWidth(SplashScreenParameters.SHELL_WIDTH);
		splashScreenDesignParameters.setProgressBarHeight(SplashScreenParameters.PROGRESS_BAR_HEIGHT);
		splashScreenDesignParameters.setProgressBarWidth(SplashScreenParameters.PROGRESS_BAR_WIDTH);
		splashScreenDesignParameters.setProgressBarXLoc(SplashScreenParameters.PROGRESS_BAR_X_LOC);
		splashScreenDesignParameters.setProgressBarYLoc(SplashScreenParameters.PROGRESS_BAR_Y_LOC);

		new SWTSplashScreen(splashScreenDesignParameters);
	}
}
