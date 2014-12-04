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

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ServerBootstrap {
	private static final Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);
	public static final String DEFAULT_RELATIVE_WEB_ROOT = "tomcat/webroot";
	public static final String STUDIO_ROOT_ENV_VAR_NAME = "WSO2_DEVELOPER_STUDIO_PATH";
	private static String rootDir;
	private static final Map<String, String> mapContextToWebApp = new HashMap<>();
	private static String webAppRoot;
	private static int port;

	// FIXME - Should load from the property file since this change with the  SDK
	static {
		mapContextToWebApp.put("/api", "api");
		mapContextToWebApp.put("/datasource", "datasource");
		mapContextToWebApp.put("/java-ca", "java-ca");
		mapContextToWebApp.put("/ws", "ws");
	}

	public static void main(String args[]) {
		try {
			rootDir = System.getenv(STUDIO_ROOT_ENV_VAR_NAME);
			String pid = System.getProperty("app.pid");
			Files.write(Paths.get(rootDir + File.separator + "bin" + File.separator + "pid"), pid.getBytes());
			while (true) {
				if (Files.exists(Paths.get(rootDir + File.separator + "bin" + File.separator + "PORT"))) {
					byte[] portBytes =
							Files.readAllBytes(Paths.get(rootDir + File.separator + "bin" + File.separator + "PORT"));
					String portStr = new String(portBytes);
					port = Integer.parseInt(portStr);
					Files.deleteIfExists(Paths.get(rootDir + File.separator + "bin" + File.separator + "PORT"));
					break;
				} else {
					Thread.sleep(500);
				}
			}
			logger.info("Root dir is" + rootDir);
			logger.info("Starting WSO2 Developer Studio 4.0.0");
			webAppRoot = rootDir + File.separator + args[0];

			if (webAppRoot.equals("")) {
				webAppRoot = rootDir + File.separator + DEFAULT_RELATIVE_WEB_ROOT;
			}

			logger.info("Tomcat web app root is set to : " + webAppRoot);

			Tomcat tomcat = new Tomcat();
			tomcat.setBaseDir(rootDir + File.separator + "temp");
			tomcat.setPort(port);
			logger.info("Tomcat port is set to: " + port);

			// Alter codenvy properties to use custom tomcat port
			ConfigManager.configureProperties(rootDir, Integer.toString(port));
			final String ideURL = "http://localhost:" + port + "/ws";
			logger.info("IDE URL is set to: " + ideURL);
			System.setProperty(ConfigManager.IDE_URL, ideURL);
			addWebApps(tomcat);
			logger.info("Starting tomcat in background");
			tomcat.start();
			tomcat.getServer().await();

		} catch (IOException e) {
			logger.error("Error querying available local ports for tomcat", e);
			//needs to exit application
		} catch (LifecycleException e) {
			logger.error("Server startup failed ! " + e.getMessage(), e);
			//needs to exit application
		} catch (ServletException e1) {
			logger.error("Server startup failed ! " + e1.getMessage(), e1);
			//needs to exit application
		} catch (InterruptedException e) {
			logger.error("Server startup failed ! " + e.getMessage(), e);
			//needs to exit application
		}
	}

	/**
	 * adding the web apps to the final developer studio package
	 */
	private static void addWebApps(Tomcat tomcat) throws ServletException {
		for (Object o : mapContextToWebApp.entrySet()) {
			@SuppressWarnings("rawtypes")
			Map.Entry webAppEntry = (Map.Entry) o;
			tomcat.addWebapp(webAppEntry.getKey().toString(),
			                 new File(webAppRoot + File.separator +
			                          webAppEntry.getValue().toString()).getAbsolutePath()
			);
			logger.info("Adding web app : " +
			            new File(webAppRoot + File.separator + webAppEntry.getValue().toString())
					            .getAbsolutePath());
		}
	}
}
