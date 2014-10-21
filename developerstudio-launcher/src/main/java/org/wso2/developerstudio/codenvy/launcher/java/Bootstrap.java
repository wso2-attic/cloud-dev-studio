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

import org.apache.catalina.startup.Tomcat;
import org.embedded.browser.SampleWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class Bootstrap {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	public static final String DEFAULT_RELATIVE_WEB_ROOT = "tomcat/webroot";
	public static final String STUDIO_ROOT_ENV_VAR_NAME = "WSO2_DEVELOPER_STUDIO_PATH";

	private static String rootDir;
	private static final Map<String, String> mapContextToWebApp = new HashMap<>();
	private static String webAppRoot;

	static {
		mapContextToWebApp.put("/api", "api");
		mapContextToWebApp.put("/datasource", "datasource");
		mapContextToWebApp.put("/java-ca", "java-ca");
		mapContextToWebApp.put("/ide", "ide");
	}

	public static void main(String args[]) {		

		rootDir = System.getenv(STUDIO_ROOT_ENV_VAR_NAME);
		logger.info("Root dir is" + rootDir);
		logger.info("Starting WSO2 Developer Studio 4.0.0");

		//webAppRoot = rootDir + File.separator + args[0];
		boolean defaultWorkSpaceSelected = getIsDefaultWorkSpaceSet();
		if(!defaultWorkSpaceSelected) {
			WorkSpaceLauncher.openWorkSpaceBrowser();
			if(!WorkSpaceLauncher.isUserWorkSpaceSet()){
				System.exit(0);
			}
		}

		splashScreenInvoker(0, "Starting WSO2 Developer Studio");

		if (webAppRoot.equals("")) {
			webAppRoot = rootDir + File.separator + DEFAULT_RELATIVE_WEB_ROOT;
		}

		logger.info("Tomcat web app root is set to : " + webAppRoot);
		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setBaseDir(rootDir + File.separator + "tomcat");

			Integer port = getAvailablePort();
			tomcat.setPort(port);
			logger.info("Tomcat port is set to: " + port);

			// Alter codenvy properties to use custom tomcat port
			ConfigManager.configureProperties(port.toString());
			String ideURL = "http://localhost:" + port + "/ide";
			logger.info("IDE URL is set to: " + ideURL);
			DeveloperStudioSplashScreen.updateProgress(300, "Configuring ports" + 30 + "%");
			addWebApps(tomcat);

			logger.info("Starting chromium in background");
//			ChromiumLauncher chromiumLauncher = new ChromiumLauncher(ideURL);
//			Thread chromiumBrowser = new Thread(chromiumLauncher);
//			chromiumBrowser.start();
			DeveloperStudioSplashScreen.updateProgress(350, "Starting tomcat in background" + 35 + "%");
			logger.info("Starting tomcat in background");
			TomcatLauncher launcher = new TomcatLauncher(tomcat);
			Thread tomcatServer = new Thread(launcher);
			DeveloperStudioSplashScreen.updateProgress(400, "Starting tomcat" + 40 + "%");
			tomcatServer.start();
			// FIXME : Implement a logic to see whether tomcat webapp deployment is finished.
			try {
				for (int i = 1; i < 26; i++) { //to sleep the thread for 25,000 while updating progress bar
					int progressVal = 400 + i * 24; //calculation of the progress bar percentage with thread sleep
					Thread.sleep(1000);
					DeveloperStudioSplashScreen.updateProgress(progressVal,
					               "Opening WSO2 Developer Studio" + progressVal / 10 + "%");
				}
				DeveloperStudioSplashScreen.updateProgress(1500,
				                           "Opening WSO2 Developer Studio" + 100 +"%");
			} catch (InterruptedException e) {
				logger.error("Chromium launcher error", e);
			}
			SampleWindow.single_browser(ideURL);

		} catch (IOException e) {
			logger.error("Error querying available local ports for tomcat", e);
		} catch (ServletException e) {
			logger.error("Error adding Web apps", e);
		}
	}

	/**
	 *
	 * @param progress
	 * @param messageToDisplay
	 * method to call the start up in Splash Screen to display the progress on IDE opening process to the user
	 */
	private static void splashScreenInvoker(int progress, String messageToDisplay) {
		DeveloperStudioSplashScreen.startUp(progress, messageToDisplay);
	}

	private static void addWebApps(Tomcat tomcat) throws ServletException {

		for (Object o : mapContextToWebApp.entrySet()) {
			Map.Entry webAppEntry = (Map.Entry) o;
			tomcat.addWebapp(webAppEntry.getKey().toString(),
			     new File(webAppRoot + File.separator + webAppEntry.getValue().toString()).getAbsolutePath());

			logger.info("Adding web app : " +
			     new File(webAppRoot + File.separator + webAppEntry.getValue().toString()).getAbsolutePath());
		}
	}

	private static Integer getAvailablePort() throws IOException {

		ServerSocket socket = new ServerSocket(0);
		Integer port = socket.getLocalPort();
		socket.close();
		return port;

	}

	private static boolean getIsDefaultWorkSpaceSet() {
		String defaultWorkSpace = null;
		try {
			defaultWorkSpace = ConfigManager.getDefaultWorkSpaceProperty();
		} catch (IOException e) {
			logger.error("error in reading whether user has set default workspace from properties" + e);
		}
		return Boolean.parseBoolean(defaultWorkSpace);
	}

}
