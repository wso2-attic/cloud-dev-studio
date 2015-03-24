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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.wso2.developerstudio.checonfigs.EclipseCheConfigurations;
import org.wso2.developerstudio.workspaceselector.utils.SWTResourceManager;
import org.wso2.developerstudio.workspaceselector.utils.SWTShellManager;
import org.wso2.developerstudio.workspaceselector.utils.SplashScreenDesignParameters;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;

import static java.io.File.separator;

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
	private static final String CONF_SERVER_XML_LOC = separator + "conf" + separator + "server.xml";
	private static final String CONNECTOR_XML_TAG = "Connector";
	private static final String SERVER_XML_TAG = "Server";
	private static final String PORT_ATTRIBUTE = "port";
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
        SWTWorkspaceSelector workspaceSelector = new SWTWorkspaceSelector();
        if (!isDefaultWorkSpaceSet()) {
			workspaceSelector.openDialog();
		}
        if (workspaceSelector.isUserWorkSpaceSet() || isDefaultWorkSpaceSet()) {
            String[] portValues = getTomcatRunningPort(ROOT_DIR);
            String currentRunPort = portValues[0];
            String currentShutPort = portValues[1];

            if (currentRunPort != null && currentShutPort != null) {
                int currentStartUpPort = Integer.parseInt(currentRunPort);
                int currentShutDownPort = Integer.parseInt(currentShutPort);
                if (isLocalPortInUse(currentStartUpPort) ||
                        isLocalPortInUse(currentShutDownPort)) {
                    EclipseCheConfigurations eclipseCheConfigurations = new EclipseCheConfigurations();
                    int startUpPort = getAvailablePort();
                    int shutDownPort = getAvailablePort();
                    if (eclipseCheConfigurations.changeRunningPort(String.valueOf(startUpPort),
                            String.valueOf(shutDownPort),
                            ROOT_DIR, currentRunPort)) {
                        port = startUpPort;
                    } // if initial port is in use and new port values cannot be written to files port will remain -1
                } else {
                    port = currentStartUpPort;
                }
            } // if current port cannot be read, port will remain as -1
        } else {
            log.info("workspace cancelled ");
        }

		writePortToFile(port);//if -1 is written to port native code will exit

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
			imageLabel.setBounds(imageLabelXLoc, imageLabelYLoc,
			                     splashScreenDesignParameters.getImageLabelWidth(),
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

	private static boolean isDefaultWorkSpaceSet() {
		try {
			return ConfigurationContext.isSetAsDefaultWorkSpace();
		} catch (IOException e) {
			log.warn("error reading whether user has set default workspace in properties " + e);
			return false;
		}
	}

	/**
	 * Since build will fail if it could not configure at least one of the files (server.xml and setenv files of eclipse che)
	 * checking the server.xml only is sufficient to get the running port
	 *
	 * @param rootDir developer studio root directory
	 * @return the port configured for developer studio startup
	 */
	private static String[] getTomcatRunningPort(String rootDir) {
		String[] runningPort = new String[2];

		String filePath = rootDir + CONF_SERVER_XML_LOC;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filePath);
			Node connectorNode = doc.getElementsByTagName(CONNECTOR_XML_TAG).item(0);
			NamedNodeMap connectorAttr = connectorNode.getAttributes();
			Node connectorPortAttr = connectorAttr.getNamedItem(PORT_ATTRIBUTE);
			runningPort[0] = connectorPortAttr.getTextContent();

			Node serverNode = doc.getElementsByTagName(SERVER_XML_TAG).item(0);
			NamedNodeMap serverAttr = serverNode.getAttributes();
			Node serverPortAttr = serverAttr.getNamedItem(PORT_ATTRIBUTE);
			runningPort[1] = serverPortAttr.getTextContent();

		} catch (IOException | SAXException | ParserConfigurationException e) {
			log.error("could not retrieve the running port configured in tomcat", e);
		}
		return runningPort;
	}

	private static boolean isLocalPortInUse(int port) {
		try {
			// ServerSocket try to open a LOCAL port
			new ServerSocket(port).close();
			// local port can be opened, it's available
			return false;
		} catch (IOException e) {
			// local port cannot be opened, it's in use
			return true;
		}
	}

	private static int getAvailablePort() {
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

}