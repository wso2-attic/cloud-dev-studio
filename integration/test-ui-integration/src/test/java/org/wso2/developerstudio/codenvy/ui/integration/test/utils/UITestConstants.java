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

	public static final int WAITING_TIME_CONSTANT = 30; //time out constant

	public static final String LINUX_PACK = "wso2-developer-studio-linux64";
	public static final String TARGET_FOLDER_LOC = System.getProperty("devs.home.target");
	public static final String CONF_FILE = "devstudio-api-configuration.properties";
	public static final String WEB_DRIVER_FIREFOX_PROFILE = "webdriver.firefox.profile";
	public static final String DEFAULT = "default";
	public static final String COMMAND_EXT_TO_RUN_AND_PROCEED = " &"; // since we need the tests to proceed while the .sh command is running
	public static final String TEST_WORK_SPACE_DIR = TARGET_FOLDER_LOC + File.separator + "DevStudioTestWorkSpace";
	public static final String SH_COMMAND = "sh ";

	//for testing
	public static final String TEST_IDE_RUN_LOC = TARGET_FOLDER_LOC + File.separator + LINUX_PACK +
	                                              File.separator + "bin" + File.separator + "wso2studio_server.sh";
	public static final String TEST_IDE_WORKSPACE_RUN_LOC = TARGET_FOLDER_LOC + File.separator + LINUX_PACK +
	                                              File.separator + "bin" + File.separator + "wso2studio_workspace.sh";
	public static final String LOCAL_IDE_LOCATION = TARGET_FOLDER_LOC + File.separator + LINUX_PACK + ".zip";
	public static final String SUREFIRE_IMAGE_SAVE_LOCATION = TARGET_FOLDER_LOC + File.separator + LINUX_PACK;


	public static final String PROPERTY_FILE_LOC = TARGET_FOLDER_LOC + File.separator + LINUX_PACK +
	                                               File.separator + "configuration";
	public static String UI_ELEM_PROPERTY_FILE_LOC = "uielement_locators.properties";

}
