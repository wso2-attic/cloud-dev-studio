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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.workspaceselector.utils.SWTResourceManager;
import org.wso2.developerstudio.workspaceselector.utils.SWTShellManager;
import org.wso2.developerstudio.workspaceselector.utils.SplashScreenDesignParameters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * This class is generating the splash screen for starting the dev-studio
 * using SWT
 */
public class SWTSplashScreen {
	private static final Logger log = LoggerFactory.getLogger(SWTSplashScreen.class);

	private static final String STUDIO_ROOT_ENV_VAR_NAME = "WSO2_DEVELOPER_STUDIO_PATH";
	private static final Display SPLASH_SCREEN_DISPLAY = Display.getDefault();
	private static final String PORT = "PORT";
	private static final String BIN = "bin";
	private final Shell splashScreenShell;
	private final ProgressBar progressBar;

	public static final String ROOT_DIR = System.getenv(STUDIO_ROOT_ENV_VAR_NAME);

	public SWTSplashScreen(SplashScreenDesignParameters splashScreenDesignParameters) {
		splashScreenShell = new Shell(SPLASH_SCREEN_DISPLAY, SWT.NONE);
		progressBar = new ProgressBar(splashScreenShell, SWT.HORIZONTAL | SWT.INDETERMINATE);
		splashScreenInit(splashScreenDesignParameters);
	}

    public void showSplashScreen() {
        splashScreenShell.open();
        if (log.isDebugEnabled()) {
            log.debug("Splash Screen Initiated.");
        }
	    /**
	     * default value of port is set to "-1" so that on user operation other depending applications will exit
	     * if port = -1
	     */
        int port = -1;
        if (!isDefaultWorkSpaceSet()) {
            SWTWorkspaceSelector workspaceSelector = new SWTWorkspaceSelector();
            workspaceSelector.openDialog();
            if (workspaceSelector.isUserWorkSpaceSet()){
               port = getAvailablePort();
            } else {
               log.info("workspace cancelled ");
            }
        }

        writePortToFile(port);

        if (port > 0) {
            /**
             * check whether IDE web app is finished deploying.
             */
            ServerClient serverClient = new ServerClient(port);
            Thread serverClientThread = new Thread(serverClient);
            serverClientThread.start();

            /**
             * Standard Eclipse recommended way of keeping an SWT widget alive until disposed by source,
             * eg: http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Fguide%2Fswt.htm
             */
            while (!splashScreenShell.isDisposed() && serverClientThread.isAlive()) {
                if (!SPLASH_SCREEN_DISPLAY.readAndDispatch()) {
                    SPLASH_SCREEN_DISPLAY.sleep();
                }
            }
        }

        SPLASH_SCREEN_DISPLAY.dispose();
    }

	/**
	 * initiate the view components
	 * if the image is available it is created with the image, if image resource is not there
	 * a blank splash screen shell will be created with the progress bar
	 */
	private void splashScreenInit(SplashScreenDesignParameters splashScreenDesignParameters) {

        splashScreenShell.setSize(splashScreenDesignParameters.getShellWidth(),
                splashScreenDesignParameters.getShellHeight());
        splashScreenShell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        // making the workSpaceSelectorShell appear in the centerShellInDisplay of the monitor
        SWTShellManager shellManager = new SWTShellManager();
        shellManager.centerShellInDisplay(splashScreenShell);

		final Label imageLabel = new Label(splashScreenShell, SWT.FILL);
		String imageLocation = splashScreenDesignParameters.getSplashImageLoc();
		Image splashScreenImage = SWTResourceManager.getImage(imageLocation);
		if (splashScreenImage != null) {
			//image label should always fill the splash screen and hence X, Y coordinates should always be zero.
			int imageLabelXLoc = 0;
			int imageLabelYLoc = 0;

			imageLabel.setAlignment(SWT.CENTER);
			imageLabel.setImage(splashScreenImage);
			imageLabel.setBounds(imageLabelXLoc, imageLabelYLoc, splashScreenDesignParameters.getImageLabelWidth(),
			                     splashScreenDesignParameters.getImageLabelHeight());
		} else { // if image is null then create a splash screen with a blank view
			log.error("Splash Screen image resource is not available at " + imageLocation);
		}

		progressBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
		progressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		progressBar.setBounds(splashScreenDesignParameters.getProgressBarXLoc(),
		                      splashScreenDesignParameters.getProgressBarYLoc(),
		                      splashScreenDesignParameters.getProgressBarWidth(),
		                      splashScreenDesignParameters.getProgressBarHeight());

        splashScreenShell.pack();
	}

	private void writePortToFile(int port) {
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(ROOT_DIR + File.separator + BIN + File.separator + PORT);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(Integer.toString(port));
		} catch (Exception e) {
			log.error("Error writing the port to file.", e);
			System.exit(1);
		} finally {
			try {
				if (null != bufferedWriter) {
					bufferedWriter.flush();
					bufferedWriter.close();
				}
				if (null != fileWriter) {
					fileWriter.close();
				}
			} catch (IOException e) {
				log.error("Error closing the file writer. " + e);
			}
		}
	}

	private int getAvailablePort() {
		ServerSocket socket = null;
		int port = -1;
		try {
			socket = new ServerSocket(0);
			port = socket.getLocalPort();
        } catch (IOException e) {
            log.error("Error while getting a vacant port for IDE", e);
		} finally {
			if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("Error while closing the opened socket", e);
                }
            }
		}
		return port;
	}

	private static boolean isDefaultWorkSpaceSet() {
		try {
			return ConfigurationContext.isSetAsDefaultWorkSpace();
		} catch (IOException e) {
			log.error("error reading whether user has set default workspace in properties" + e);
			return false;
		}
	}
}