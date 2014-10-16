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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ConfigManager {

	private static final Logger logger = LoggerFactory.getLogger(ChromiumLauncher.class);

	public static final java.lang.String CODENVY_LOCAL_CONF_DIR = "codenvy.local.conf.dir";

	public static final String ANALYTICS_CONF_FILENAME = "analytics.properties";
	public static final String CODENVY_API_PROPERTIES_FILE =
			"codenvy-api-configuration.properties";
	public static final String CODENVY_DATASOURCE_PROPERTIES_FILE =
			"codenvy-datasource-configuration.properties";
	public static final String CODENVY_JAVA_CA_PROPERTIES_FILE =
			"codenvy-java-codeassistant-configuration.properties";

	public static final String WORKSPACE_ROOT_PROPERTY = "vfs.local.fs_root_dir";
	public static final String RUNNER_URL = "runner.slave_runner_urls";
	public static final String BUILDER_URL = "builder.slave_builder_urls";
	public static final String API_ENDPOINT = "api.endpoint";
	public static final String EVENT_SUBSCRIPTION_URL = "notification.client.event_subscriptions";

	/**
	 * Alters urls defined in configuration files to use passed local port
	 *
	 * @param tomcatPort Port allocated for embedded tomcat server
	 */
	public static void configureProperties(String tomcatPort) {

		try {
			Properties apiProps = loadPropertiesFromFile(CODENVY_API_PROPERTIES_FILE);

			Properties datasourceProps = loadPropertiesFromFile(CODENVY_DATASOURCE_PROPERTIES_FILE);
			Properties javaCAProps = loadPropertiesFromFile(CODENVY_JAVA_CA_PROPERTIES_FILE);

			apiProps.setProperty(RUNNER_URL,
			                     "http://localhost:" + tomcatPort + "/api/internal/runner");
			apiProps.setProperty(BUILDER_URL,
			                     "http://localhost:" + tomcatPort + "/api/internal/builder");

			datasourceProps.setProperty(API_ENDPOINT, "http://localhost:" + tomcatPort + "/api");

			javaCAProps.setProperty(EVENT_SUBSCRIPTION_URL,
			                        "ws://localhost:" + tomcatPort + "/api/ws/=vfs");

			storePropertiesToFile(CODENVY_API_PROPERTIES_FILE, apiProps);
			storePropertiesToFile(CODENVY_DATASOURCE_PROPERTIES_FILE, datasourceProps);
			storePropertiesToFile(CODENVY_JAVA_CA_PROPERTIES_FILE, javaCAProps);

		} catch (IOException e) {

			logger.error("Error configuring codenvy properties files with port :" + tomcatPort, e);
		}

	}

	/**
	 * Read value of a property defined in codenvy api configuration
	 *
	 * @param propertyKey Key of the property
	 * @return String  property value
	 * @throws IOException
	 */
	public static String getAPIProperty(String propertyKey) throws IOException {
		Properties apiProps = loadPropertiesFromFile(CODENVY_API_PROPERTIES_FILE);

		return apiProps.getProperty(propertyKey);
	}

	/**
	 * Set a codenvy api configuration property
	 *
	 * @param propertyKey   Key of the property
	 * @param propertyValue Value of the property
	 * @throws IOException
	 */
	public static void setAPIProperty(String propertyKey, String propertyValue) throws IOException {
		Properties apiProps = loadPropertiesFromFile(CODENVY_API_PROPERTIES_FILE);

	}

	/**
	 * Reads a property file and build a map
	 *
	 * @param fileName Name of the property file available in conf folder
	 * @return Property Map
	 * @throws IOException
	 */
	private static Properties loadPropertiesFromFile(String fileName) throws IOException {

		fileName = System.getProperty(CODENVY_LOCAL_CONF_DIR) + File.separator + fileName;
		Properties properties = new Properties();

		FileInputStream in = new FileInputStream(fileName);
		properties.load(in);
		in.close();

		return properties;
	}

	/**
	 * Writes modified properties back to a property file
	 *
	 * @param fileName   Name of the property file available in conf folder
	 * @param properties Properties map
	 * @throws IOException
	 */
	private static void storePropertiesToFile(String fileName, Properties properties)
			throws IOException {

		fileName = System.getProperty(CODENVY_LOCAL_CONF_DIR) + File.separator + fileName;

		FileOutputStream out = new FileOutputStream(fileName);
		properties.store(out, null);
		out.close();

	}

}
