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

/**
 * Defines constants
 */
public interface UITestConstants {

	/*Testing about menu popup*/
	public static final String WSO2_DEV_STUDIO_GWT_ID = "file.devstudio.id";
	public static final String WSO2_ABOUT_ACTION_GWT_ID = "wso2.about.action.id";
	public static final String WSO2_ABOUT_ACTION_SELECTION_CLICK_ID = "about.action.selection.click.id";
	public static final String WSO_ABOUT_ACTION_CLOSE_BUTTON_ID = "about.action.close.button.click.id";

	/*UI Elem locator keys*/
	public static final String WSO2_FILE_MENU_ID = "new.file.menu.id";
	public static final String WSO2_FILE_NEW_OPTION_ID = "file.new.option.id";
	public static final String WSO2_NEW_PROJECT_OPTION_ID = "new.wso2.project.type.id";
	public static final String WSO2_NEW_PROJECT_NAME_OPTION_ID = "new.project.name.id";
	public static final String WSO2_NEW_PROJECT_DESCRIPTION_ID = "new.project.description.id";
	public static final String WSO2_NEW_PROJECT_OPTIONS_DROP_DOWN_XPATH =
			"new.wso2.appserver.project.types.dropdown.xpath";
	public static final String WSO2_NEW_PROJECT_TYPE_XPATH =
			"new.project.type.appserver.xpath";
	public static final String WSO2_NEW_PROJECT_ARTIFACTID_ID = "new.project.artifact.id.textbox";
	public static final String WSO2_NEW_PROJECT_GROUPID_ID = "new.project.group.id.testbox";
	public static final String WSO2_NEW_PROJECT_VERION_ID = "new.project.version.textbox";

	public static final String PROJECT_CREATION_WIZARD_NEXT_BUTTON_XPATH = "project.creation.wizard.next.button.xpath";
	public static final String PROJECT_CREATION_WIZARD_CREATE_BUTTON_XPATH = "project.creation.wizard.create.button.xpath";

	public static final String TEST_JAXRS_PROJECT_NAME = "Test_JAX_RS_Project";
	public static final String TEST_JAXWS_PROJECT_NAME = "Test_JAX_WS_Project";
	public static final String TEST_WEBAPP_PROJECT_NAME = "Test_WEB_APP_Project";
	public static final String TEST_PROJECT_DESCRIPTION = "Testing Description";
	public static final String TEST_PROJECT_ARTIFACT_ID = "sampleproject";
	public static final String TEST_PROJECT_GROUP_ID = "org.wso2.test.project";
	public static final String TEST_PROJECT_VERSION_ID = "1.0.0";

	public static final String JAX_RS_PROJECT_TYPE = "JAX RS Project";
	public static final String JAX_WS_PROJECT_TYPE = "JAX WS Project";
	public static final String WEB_APP_PROJECT_TYPE = "Java Web Application";

	public static final int WAITING_TIME_CONSTANT = 30; //time out constant
	public static final int NO_OF_APP_SERVER_PROJECTS = 3; // no of app server project

	/*folder structure testing for app server*/
	public static final String SOURCE = "src";
	public static final String MAIN = "main";
	public static final String TEST = "test";
	public static final String JAVA = "java";
	public static final String MANIFEST_XML = "manifest.xml";
	public static final String WEB_XML = "web.xml";
	public static final String WEB_INF = "WEB-INF";
	public static final String POM_XML = "pom.xml";
	public static final String WEB_APP = "webapp";
	public static final String META_INF = "META-INF";
	public static final String WEB_CONTENT = "WebContent";
	public static final String WEB_APP_CLASS_LOADING_XML = "webapp-classloading.xml";
	public static final String CXF_SERVLET_XML = "cxf-servlet.xml";
	public static final String RESOURCES = "resources";

	public static final String LINUX_PACK = "wso2-developer-studio-linux64";
	public static final String TARGET_FOLDER_LOC = System.getProperty("devs.home.target");
	public static final String CONF_FILE = "devstudio-api-configuration.properties";
	public static final String WEB_DRIVER_FIREFOX_PROFILE = "webdriver.firefox.profile";
	public static final String DEFAULT = "default";
	public static final String COMMAND_EXT_TO_RUN_AND_PROCEED = " &"; // since we need the tests to proceed while the .sh command is running
	public static final String TEST_WORK_SPACE_DIR = TARGET_FOLDER_LOC + File.separator + "DevStudioTestWorkSpace";
	public static final String SH_COMMAND = "sh ";

	//for testing
	public static final String DEVELOPER_STUDIO_IDE_URL = "developerstudio.ide.url";
	public static final String TEST_IDE_RUN_LOC = TARGET_FOLDER_LOC + File.separator + LINUX_PACK +
	                                              File.separator + "bin" + File.separator + "wso2studio_server.sh";
	public static final String TEST_IDE_WORKSPACE_RUN_LOC = TARGET_FOLDER_LOC + File.separator + LINUX_PACK +
	                                              File.separator + "bin" + File.separator + "wso2studio_workspace.sh";
	public static final String LOCAL_IDE_LOCATION = TARGET_FOLDER_LOC + File.separator + LINUX_PACK + ".zip";

	public static final String PROPERTY_FILE_LOC = TARGET_FOLDER_LOC + File.separator + LINUX_PACK +
	                                               File.separator + "configuration";
	public static String UI_ELEM_PROPERTY_FILE_LOC = "uielement_locators.properties";

}
