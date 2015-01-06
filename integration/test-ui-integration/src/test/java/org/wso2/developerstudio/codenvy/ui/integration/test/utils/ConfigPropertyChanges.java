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

package org.wso2.developerstudio.codenvy.ui.integration.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class Handles the configuration property value changes needed to run tests
 */
public class ConfigPropertyChanges {

	public static final String IDE_URL = "developerstudio.ide.url";
	public static final String WORKSPACE_ROOT_PROPERTY = "vfs.local.fs_root_dir";
	public static final String SET_DEFAULT_WORKSPACE = "set.workspace.as.default";
	/**
	 * This method Loads the properties from properties file
	 *
	 * @param fileName the properties file to load properties from
	 * @return the properties loaded
	 * @throws IOException
	 */
	private static Properties loadPropertiesFromFileTest(String fileName) throws IOException {
		Properties properties = new Properties();
		FileInputStream inputFileStream = null;
		try {
			inputFileStream = new FileInputStream(fileName);
			properties.load(inputFileStream);
		} finally {
			if (inputFileStream != null) {
				inputFileStream.close();
			}
		}
		return properties;
	}

	/**
	 * This method Writes modified properties back to a property file
	 *
	 * @param fileName   Name of the property file available in conf folder
	 * @param properties Properties map
	 * @throws IOException
	 */
	private static void storePropertiesToFileTest(String fileName, Properties properties) throws IOException {
		FileOutputStream outputFileStream = null;
		try {
			outputFileStream = new FileOutputStream(fileName);
			properties.store(outputFileStream, null);

		} finally {
			if (outputFileStream != null) {
				outputFileStream.close();
			}
		}
	}

	/**
	 * This method will write a specific property value into the ide configurations property file
	 *
	 * @param propertyKey   key of the property being written
	 * @param propertyValue corresponding value
	 * @throws IOException
	 */
	public static void setTestAPIPropertyTest(String propertyKey, String propertyValue) throws IOException {
		Properties apiProps = loadPropertiesFromFileTest(UITestConstants.PROPERTY_FILE_LOC +
		                                                 File.separator + UITestConstants.CONF_FILE);
		apiProps.setProperty(propertyKey, propertyValue);
		storePropertiesToFileTest(UITestConstants.PROPERTY_FILE_LOC + File.separator +
		                          UITestConstants.CONF_FILE, apiProps);
	}

	/**
	 * This method retrieves a key value pair from the configuration properties file
	 *
	 * @param propertyKey  key value of the property
	 * @param propertyFile property file to get the value from
	 * @return returns the corresponding value of the key
	 * @throws IOException
	 */
	public static String getAPIPropertyTest(String propertyKey, String propertyFile) throws IOException {
		Properties apiProps = loadPropertiesFromFileTest(propertyFile);
		return apiProps.getProperty(propertyKey);
	}

	/**
	 * This method sets whether to take the workspace set as default for the IDE in the properties file ,
	 * this value will be taken as the workspace for the IDE
	 * and will not popup the workspace selector dialog again
	 *
	 * @param propertyValue the workspace default set value as true or false by user
	 * @throws IOException
	 */
	public static void setDefaultWorkSpacePropertyTest(String propertyValue) throws IOException {
		setTestAPIPropertyTest(SET_DEFAULT_WORKSPACE, propertyValue);
	}

	/**
	 * This method saves the workspace location given by user into the properties file.
	 *
	 * @param workspace location set by user
	 * @throws IOException
	 */
	public static void setWorkspaceDirectory(String workspace) throws IOException {
		setTestAPIPropertyTest(WORKSPACE_ROOT_PROPERTY, workspace);
	}

	/**
	 * This method retrieves the currently running URL value of the IDE
	 *
	 * @return returns the IDE URL
	 * @throws IOException
	 */
	public static String getIDEURLTest() throws IOException {
		return getAPIPropertyTest(IDE_URL, UITestConstants.PROPERTY_FILE_LOC + File.separator +
		                                                 UITestConstants.CONF_FILE);

	}

	/**
	 * This method Retrieves the corresponding Xpath value OR ID value for the UI elements being tested by selenium
	 *
	 * @param propertyKey key for the UI Elem to be located
	 * @return the corresponding ID or Xpath for the element
	 * @throws IOException
	 */
	public static String getUIElemProperty(String propertyKey) throws IOException {
		return getAPIPropertyTest(propertyKey, UITestConstants.UI_ELEM_PROPERTY_FILE_LOC);
	}
}

