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

    String BIN_FOLDER = "bin";
    String CONF_FOLDER = "conf";
    String SETENV_SH = "setenv.sh";
    String SETENV_bat = "setenv.bat";
    String SERVER_XML = "server.xml";

    String CONNECTOR_XML_TAG = "Connector";
    String SERVER_TAG = "Server";
    String PORT_ATTRIBUTE = "port";

    String EXPORT_SERVER_PORT = "export SERVER_PORT=";
    String SET_SERVER_PORT = "set SERVER_PORT=";
    String DEFAULT_PORT = "8080";

    String CONF_SERVER_XML_LOC = separator + CONF_FOLDER + separator + SERVER_XML;
    String BIN_SETENV_SH_LOC = separator + BIN_FOLDER + separator + SETENV_SH;
    String BIN_SETENV_BAT_LOC = separator + BIN_FOLDER + separator + SETENV_bat;
    String CODENVY_API_CONFIGURATION_PROPERTIES = "codenvy-api-configuration.properties";
    String CODENVY_API_PROPERTIES_FILE =
            separator + "webapps" + separator + "api" + separator + "WEB-INF" + separator + "classes" + separator +
                    "codenvy" + separator +
                    CODENVY_API_CONFIGURATION_PROPERTIES;
    String CATALINA_BASE_TEMP_CHE_TEMPLATES = "${catalina.base}/samples/che-templates";
    String PROJECT_TEMPLATE_LOCATION_DIR = "project.template_location_dir";
    String PROJECT_TEMPLATE_DESCRIPTION_DIR = "project.template_descriptions_dir";
    String MAVEN_JSON_FILE_LOCATION = "${catalina.base}/conf";
}
