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

import java.io.*;
import java.util.Properties;

public class ConfigurationContext {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationContext.class);

	public static final String DEV_STUDIO_CONF_DIR = "developer_studio.local.conf.dir";
	public static final String DEV_STUDIO_PROPERTIES_FILE = "developer_studio.properties";
	public static final String IDE_URL = "developer_studio.ide.url";

	public static final String WORKSPACE_ROOT_PROPERTY = "vfs.local.fs_root_dir";
	public static final String WORKSPACE_ROOT_INDEX_PROPERTY = "vfs.local.fs_index_root_dir";
	public static final String IS_DEFAULT_WORKSPACE = "set.workspace.as.default";

	/**
	 * Sets system properties used by Eclipse Che
	 *
	 * @param tomcatPort Port assigned for embedded tomcat server
	 *
	 */
	public static void setServerSystemProperties(String tomcatPort){

		setSystemProperty("SERVER_PORT", tomcatPort);

		try {
			setSystemProperty("WORKSPACE_ROOT",
			                  getDevStudioProperty(WORKSPACE_ROOT_PROPERTY));
			setSystemProperty("WORKSPACE_INDEX_ROOT",
			                  getDevStudioProperty(WORKSPACE_ROOT_INDEX_PROPERTY));
		} catch (IOException e) {
			logger.error("Error setting workspace root in system properties.", e);
		}

	}

	private static void setSystemProperty(String propertyKey, String propertyValue){
		System.setProperty(propertyKey, propertyValue);
		if(logger.isDebugEnabled()){
			logger.debug("System property {} is set to {}.", propertyKey, propertyValue);
		}
	}

	/**
	 * Read value of a property defined in Dev Studio properties
	 *
	 * @param propertyKey Key of the property
	 * @return String  property value
	 * @throws IOException
	 */
	public static String getDevStudioProperty(String propertyKey) throws IOException {
		Properties properties = loadPropertiesFromFile(DEV_STUDIO_PROPERTIES_FILE);
		return properties.getProperty(propertyKey);
	}

	/**
	 * Set a Dev Studio configuration property
	 *
	 * @param propertyKey   Key of the property
	 * @param propertyValue Value of the property
	 * @throws IOException
	 */
	public static void setDevStudioProperty(String propertyKey, String propertyValue)
			throws IOException {
		Properties properties = loadPropertiesFromFile(DEV_STUDIO_PROPERTIES_FILE);
		properties.setProperty(propertyKey, propertyValue);
		storePropertiesToFile(DEV_STUDIO_PROPERTIES_FILE, properties);
		if (logger.isDebugEnabled()) {
			logger.debug("Configuration property {} is set to {}.", propertyKey, propertyValue);
		}
	}

	/**
	 * Reads a property file and build a map
	 *
	 * @param fileName Name of the property file available in conf folder
	 * @return Property Map
	 * @throws IOException
	 */
	private static Properties loadPropertiesFromFile(String fileName) throws IOException {
		fileName = System.getProperty(DEV_STUDIO_CONF_DIR) + File.separator + fileName;
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
		fileName = System.getProperty(DEV_STUDIO_CONF_DIR) + File.separator + fileName;
		FileOutputStream out = new FileOutputStream(fileName);
		properties.store(out, null);
		out.close();
	}

	/**
	 * Sets workspace root directory
	 *
	 * @param workspace File path for the workspace
	 * @throws IOException
	 */
	public static void setWorkspaceRoot(String workspace) throws IOException {
		setDevStudioProperty(WORKSPACE_ROOT_PROPERTY, workspace);
		setDevStudioProperty(WORKSPACE_ROOT_INDEX_PROPERTY, workspace);
	}

	/**
	 * Read workspace root directory
	 *
	 * @return workspace root
	 * @throws IOException
	 */
	public static String getWorkspaceRoot() throws IOException {
		return getDevStudioProperty(WORKSPACE_ROOT_PROPERTY);
	}

	/**
	 * Set current workspace as default
	 *
	 * @param isDefaultWorkspace Set current workspace as default
	 * @throws IOException
	 */
	public static void setAsDefaultWorkSpace(boolean isDefaultWorkspace) throws IOException {
		setDevStudioProperty(IS_DEFAULT_WORKSPACE, String.valueOf(isDefaultWorkspace));
	}

	/**
	 * Indicates whether current workspace has been set as default
	 *
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean isSetAsDefaultWorkSpace() throws IOException {
		return Boolean.parseBoolean(getDevStudioProperty(IS_DEFAULT_WORKSPACE));
	}

	/**
	 * Sets current URL of the IDE web application
	 *
	 * @param IDEUrl current URL of the IDE web application
	 * @throws IOException
	 */
	public static void setIDEUrl(String IDEUrl) throws IOException {
		setDevStudioProperty(IDE_URL, IDEUrl);
	}

	/**
	 * Reads current URL of the IDE web application
	 *
	 * @return URL of the ide web app
	 * @throws IOException
	 */
	public static String getIdeUrl() throws IOException {
		return getDevStudioProperty(IDE_URL);
	}

}
