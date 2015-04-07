/*
 * Copyright (c) 2014-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.checonfigs;

import static java.io.File.separator;

public interface Constants {

	static final String BIN_FOLDER = "bin";
	static final String CONF_FOLDER = "conf";
	static final String SETENV_SH = "setenv.sh";
	static final String SETENV_bat = "setenv.bat";
	static final String SERVER_XML = "server.xml";

	static final String CONNECTOR_XML_TAG = "Connector";
	static final String SERVER_TAG = "Server";
	static final String PORT_ATTRIBUTE = "port";

	static final String EXPORT_SERVER_PORT = "export SERVER_PORT=";
	static final String SET_SERVER_PORT = "set SERVER_PORT=";
	static final String DEFAULT_PORT = "8080";

	static final String CONF_SERVER_XML_LOC = separator + CONF_FOLDER + separator + SERVER_XML;
	static final String BIN_SETENV_SH_LOC = separator + BIN_FOLDER + separator + SETENV_SH;
	static final String BIN_SETENV_BAT_LOC = separator + BIN_FOLDER + separator + SETENV_bat;
	static final String CODENVY_API_CONFIGURATION_PROPERTIES = "codenvy-api-configuration.properties";
	static final String CODENVY_API_PROPERTIES_FILE =
			separator + "webapps" + separator + "api" + separator + "WEB-INF" + separator + "classes" + separator +
			"codenvy" + separator +
			CODENVY_API_CONFIGURATION_PROPERTIES;
	public static final String CATALINA_BASE_TEMP_CHE_TEMPLATES = "${catalina.base}/samples/che-templates";
	public static final String PROJECT_TEMPLATE_LOCATION_DIR = "project.template_location_dir";
	public static final String PROJECT_TEMPLATE_DESCRIPTION_DIR = "project.template_descriptions_dir";
	public static final String MAVEN_JSON_FILE_LOCATION = "${catalina.base}/conf";
}
