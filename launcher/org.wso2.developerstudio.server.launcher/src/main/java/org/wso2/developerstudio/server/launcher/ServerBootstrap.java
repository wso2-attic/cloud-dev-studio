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
package org.wso2.developerstudio.server.launcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.workspaceselector.launcher.ConfigurationContext;

public class ServerBootstrap {

	private static final String WEBAPP_WS = "ws";

	private static final String WS_CONTEXTPATH = "/ws";

	private static final String JAVA_CA_WEBAPP = "java-ca";

	private static final String JAVA_CA_CONTEXTPATH = "/java-ca";

	public static final String WEBAPP_DATASOURCE = "datasource";

	public static final String DATASOURCE_CONTEXTPATH = "/datasource";

	public static final String WEBAPP_API = "api";

	public static final String API_CONTEXTPATH = "/api";

	private static final Logger logger = LoggerFactory
			.getLogger(ServerBootstrap.class);

	private static String rootDir;
	private static final Map<String, String> mapContextToWebApp = new HashMap<>();
	private static String webAppRoot;
	private static int port;

	// FIXME - Should load from the property file since this change with the SDK
	static {
		mapContextToWebApp.put(API_CONTEXTPATH, WEBAPP_API);
		mapContextToWebApp.put(DATASOURCE_CONTEXTPATH, WEBAPP_DATASOURCE);
		mapContextToWebApp.put(JAVA_CA_CONTEXTPATH, JAVA_CA_WEBAPP);
		mapContextToWebApp.put(WS_CONTEXTPATH, WEBAPP_WS);
	}

	public static void main(String args[]) {
		try {
			rootDir = System.getenv(Constants.STUDIO_ROOT_ENV_VAR_NAME);
			String pid = System.getProperty("app.pid");
			Files.write(Paths.get(rootDir + File.separator + "bin" + File.separator + "pid"),
			            pid.getBytes());
			while (true) {
				if (Files.exists(Paths.get(
						rootDir + File.separator + "bin" + File.separator + "PORT"))) {
					byte[] portBytes = Files.readAllBytes(
							Paths.get(rootDir + File.separator + "bin" + File.separator + "PORT"));
					String portStr = new String(portBytes);
					System.setProperty("server.port", portStr);
					port = Integer.parseInt(portStr);
					Files.deleteIfExists(
							Paths.get(rootDir + File.separator + "bin" + File.separator + "PORT"));
					break;
				} else {
					Thread.sleep(500);
				}
			}
			logger.info("Root dir is" + rootDir);
			logger.info("Starting WSO2 Developer Studio 4.0.0");
			webAppRoot = rootDir + File.separator + args[0];

			if (webAppRoot.equals("")) {
				webAppRoot = rootDir + File.separator
				             + Constants.DEFAULT_RELATIVE_WEB_ROOT;
			}

			logger.info("Tomcat web app root is set to : " + webAppRoot);

			Tomcat tomcat = new DevsTomcatServer();
			tomcat.setBaseDir(rootDir + File.separator + Constants.WEBAPPS_DIR);
			tomcat.setPort(port);
			logger.info("Tomcat port is set to: " + port);

			// Alter codenvy properties to use custom tomcat port
			ConfigurationContext.setServerSystemProperties(Integer.toString(port));
			final String ideURL = "http://localhost:" + port + WS_CONTEXTPATH;
			logger.info("IDE URL is set to: " + ideURL);
			ConfigurationContext.setIDEUrl(ideURL);
			addWebApps(tomcat);
			tomcat.addWebapp("/", rootDir + File.separator
			                      + Constants.WEBAPPS_DIR + File.separator);
			logger.info("Starting tomcat in background");
			tomcat.start();
			tomcat.getServer().await();

		} catch (IOException e) {
			logger.error("Error querying available local ports for tomcat", e);
			System.exit(1);
		} catch (LifecycleException e) {
			logger.error("Server startup failed ! " + e.getMessage(), e);
			System.exit(1);
		} catch (ServletException e1) {
			logger.error("Server startup failed ! " + e1.getMessage(), e1);
			System.exit(1);
		} catch (InterruptedException e) {
			logger.error("Server startup failed ! " + e.getMessage(), e);
			System.exit(1);
		}
	}

	/**
	 * method to call the start up in Splash Screen to display the
	 * progress on IDE opening process to the user
	 *
	 * @param tomcat Tomcat Server
	 */
	private static void addWebApps(Tomcat tomcat) throws ServletException {
		for (Object o : mapContextToWebApp.entrySet()) {
			@SuppressWarnings("rawtypes")
			Map.Entry webAppEntry = (Map.Entry) o;
			tomcat.addWebapp(webAppEntry.getKey().toString(), new File(
					webAppRoot + File.separator
					+ webAppEntry.getValue().toString())
					.getAbsolutePath());
			logger.info("Adding web app : "
			            + new File(webAppRoot + File.separator
			                       + webAppEntry.getValue().toString())
					.getAbsolutePath());
		}
	}
}
