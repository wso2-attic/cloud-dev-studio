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
	public static final int MAXIMUM_PROGRESS = 1000; // parameters to design the splashscreen, needs to be sent to a properties file
	public static final int SHELL_WIDTH = 448; // parameters to design the splashscreen
	public static final int SHELL_HEIGHT = 270;// parameters to design the splashscreen
	public static final int IMAGE_LABEL_WIDTH = 450;// parameters to design the splashscreen
	public static final int IMAGE_LABEL_HEIGHT = 300;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_WIDTH = 450;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_HEIGHT = 20;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_X_LOC = 0;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_Y_LOC = 240;// parameters to design the splashscreen
	private static String rootDir;
	private static final Map<String, String> mapContextToWebApp = new HashMap<>();
	private static String webAppRoot;

	public static final String SPLASH_SCREEN_IMAGE = "splash.png";
	public static final String ICONS_FOLDER = "icons";

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

		webAppRoot = rootDir + File.separator + args[0];
		String SplashImageResource = rootDir + File.separator + ICONS_FOLDER + File.separator + SPLASH_SCREEN_IMAGE;

		SWTSplashScreen swtSplashScreen = new SWTSplashScreen(SplashImageResource, SHELL_WIDTH,
		                                                      SHELL_HEIGHT,
		                                                      MAXIMUM_PROGRESS, IMAGE_LABEL_WIDTH,
		                                                      IMAGE_LABEL_HEIGHT,
		                                                      PROGRESS_BAR_WIDTH,
		                                                      PROGRESS_BAR_HEIGHT,
		                                                      PROGRESS_BAR_X_LOC,
		                                                      PROGRESS_BAR_Y_LOC);
		swtSplashScreen.updateProgress(0);
		logger.info("opened splash screen");

		if (!isDefaultWorkSpaceSet()) {
			WorkSpaceLauncher workSpaceLauncher = new WorkSpaceLauncher();
			if (!workSpaceLauncher.isUserWorkSpaceSet()) {
				logger.info("exit system due to user operation");
				System.exit(0);
			}
		}

		if (webAppRoot.equals("")) {
			webAppRoot = rootDir + File.separator + DEFAULT_RELATIVE_WEB_ROOT;
		}

		logger.info("Tomcat web app root is set to : " + webAppRoot);
		try {
			Tomcat tomcat = new Tomcat();
			tomcat.setBaseDir(rootDir + File.separator + "temp");

			Integer port = getAvailablePort();
			tomcat.setPort(port);
			logger.info("Tomcat port is set to: " + port);

			// Alter codenvy properties to use custom tomcat port
			ConfigManager.configureProperties(rootDir, port.toString());
			String ideURL = "http://localhost:" + port + "/ide";
			logger.info("IDE URL is set to: " + ideURL);
			swtSplashScreen.updateProgress(300);
			addWebApps(tomcat);

			logger.info("Starting chromium in background");
			//			ChromiumLauncher chromiumLauncher = new ChromiumLauncher(ideURL);
			//			Thread chromiumBrowser = new Thread(chromiumLauncher);
			//			chromiumBrowser.start();
			swtSplashScreen.updateProgress(350);
			logger.info("Starting tomcat in background");
			TomcatLauncher launcher = new TomcatLauncher(tomcat);
			Thread tomcatServer = new Thread(launcher);
			swtSplashScreen.updateProgress(400);
			tomcatServer.start();
			// FIXME : Implement a logic to see whether tomcat webapp deployment is finished.
			try {
				for (int i = 1; i < 26; i++) { //to sleep the thread for 25,000 while updating progress bar
					int progressVal = 400 + i * 24; //calculation of the progress bar percentage with thread sleep
					Thread.sleep(1000);
					swtSplashScreen.updateProgress(progressVal);
				}
				swtSplashScreen.updateProgress(1500);
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

	private static void addWebApps(Tomcat tomcat) throws ServletException {

		for (Object o : mapContextToWebApp.entrySet()) {
			Map.Entry webAppEntry = (Map.Entry) o;
			tomcat.addWebapp(webAppEntry.getKey().toString(),
			                 new File(webAppRoot + File.separator +
			                          webAppEntry.getValue().toString()).getAbsolutePath());

			logger.info("Adding web app : " +
			            new File(webAppRoot + File.separator + webAppEntry.getValue().toString())
					            .getAbsolutePath());
		}
	}

	private static Integer getAvailablePort() throws IOException {

		ServerSocket socket = new ServerSocket(0);
		Integer port = socket.getLocalPort();
		socket.close();
		return port;

	}

	private static boolean isDefaultWorkSpaceSet() {
		String defaultWorkSpace = null;
		try {
			defaultWorkSpace = ConfigManager.getDefaultWorkSpaceProperty();
		} catch (IOException e) {
			logger.error(
					"error reading whether user has set default workspace in properties" + e);
		}
		return Boolean.parseBoolean(defaultWorkSpace);
	}

	public static String getRootDir() {
		return rootDir;
	}

}
