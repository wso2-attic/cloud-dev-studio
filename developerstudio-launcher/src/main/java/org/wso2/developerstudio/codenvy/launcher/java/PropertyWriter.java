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

public class PropertyWriter {

	private static final Logger logger = LoggerFactory.getLogger(ChromiumLauncher.class);

	private static final java.lang.String CODENVY_LOCAL_CONF_DIR = "codenvy.local.conf.dir";

	private static final String ANALYTICS_CONF_FILENAME = "analytics.properties";
	private static final String CODENVY_API_PROPERTIES_FILE =
															"codenvy-api-configuration.properties";

	private static final String WORKSPACE_ROOT_PROPERTY = "vfs.local.fs_root_dir";
	private static final String RUNNER_URL = "runner.slave_runner_urls";
	private static final String BUILDER_URL = "builder.slave_builder_urls";

	public static void configureProperties(String tomcatPort) throws IOException {

		Properties apiProps = loadPropertiesFromFile(CODENVY_API_PROPERTIES_FILE);

		apiProps.setProperty(RUNNER_URL, "http://localhost:" + tomcatPort + "/api/internal/runner");
		apiProps.setProperty(BUILDER_URL, "http://localhost:" + tomcatPort + "/api/internal/builder");

		storePropertiesToFile(CODENVY_API_PROPERTIES_FILE, apiProps);

	}

	private static Properties loadPropertiesFromFile(String fileName) throws IOException {

		fileName = System.getProperty(CODENVY_LOCAL_CONF_DIR) + File.separator + fileName;
		Properties properties = new Properties();

		FileInputStream in = new FileInputStream(fileName);
		properties.load(in);
		in.close();

		return properties;
	}

	private static void storePropertiesToFile(String fileName, Properties properties)
			throws IOException {

		fileName = System.getProperty(CODENVY_LOCAL_CONF_DIR) + File.separator + fileName;

		FileOutputStream out = new FileOutputStream(fileName);
		properties.store(out, null);
		out.close();

	}

}
