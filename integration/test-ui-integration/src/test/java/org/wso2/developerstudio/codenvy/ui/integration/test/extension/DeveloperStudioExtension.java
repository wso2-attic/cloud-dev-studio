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

package org.wso2.developerstudio.codenvy.ui.integration.test.extension;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.wso2.carbon.automation.engine.extensions.ExecutionListenerExtension;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.ConfigPropertyChanges;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.UITestConstants;

import java.io.File;
import java.io.IOException;

/**
 * extension for wso2 test automation framework, to get the developer studio up and running before executing all tests,
 * and shutdown after all tests.
 * extract the pack, run the developer studio before tests.
 * stops the developer studio and clean up the directories after tests run.
 */
public class DeveloperStudioExtension extends ExecutionListenerExtension {
	private Process runningProcess;

	@Override public void initiate() throws Exception {

	}

	/**
	 * This method will start up the developer studio 4.0.0 before running any tests in the test class
	 *
	 * @throws Exception
	 */
	@Override public void onExecutionStart() throws Exception {
		unzip(UITestConstants.LOCAL_IDE_LOCATION, UITestConstants.TARGET_FOLDER_LOC); //unzip the pack for testing to the target directory so it will be deleted in the next build

		/**
		 * Does the configuration changes to avoid the SWT widget popup at startup
		 */
		File propertyFile = new File(UITestConstants.PROPERTY_FILE_LOC + File.separator + UITestConstants.CONF_FILE);
		if(propertyFile.mkdir()){
			ConfigPropertyChanges.setDefaultWorkSpacePropertyTest(String.valueOf(true));
			makeWorkSpaceDir();
			ConfigPropertyChanges.setWorkspaceDirectory(UITestConstants.TEST_WORK_SPACE_DIR);
		}
		String[] execCommand = new String[] { UITestConstants.SH_COMMAND, UITestConstants.TEST_IDE_RUN_LOC +
		                                          UITestConstants.COMMAND_EXT_TO_RUN_AND_PROCEED };

		runningProcess = Runtime.getRuntime().exec(execCommand);// get the developer studio running
	}

	/**
	 * This method will kill the running developer studio 4.0.0 on finishing the tests execution,
	 * this will also delete the corresponding workspace created and the extracted pack
	 *
	 * @throws Exception
	 */
	@Override public void onExecutionFinish() throws Exception {
		runningProcess.destroy(); // stop the process, otherwise sometimes the folder deletion might fail
		runningProcess.waitFor();
		deleteTestPack(UITestConstants.TEST_WORK_SPACE_DIR);
		deleteTestPack(UITestConstants.TARGET_FOLDER_LOC + UITestConstants.LINUX_PACK);
	}

	/**
	 * to unzip the pack for testing
	 *
	 * @param zipFilePath   location of the zip file
	 * @param destinationDirectory destination to extract the zip file
	 * @throws ZipException unable to extract the zip
	 */
	public static void unzip(String zipFilePath, String destinationDirectory) throws ZipException {
			ZipFile zipFile = new ZipFile(zipFilePath);
			zipFile.extractAll(destinationDirectory);

	}

	/**
	 * delete a directory and all its contents
	 *
	 * @param dirToDelete directory to be deleted
	 *  @throws java.io.IOException unable to delete the file
	 */
	private void deleteTestPack(String dirToDelete) throws IOException {
		File workSpaceDir = new File(dirToDelete);
			if (workSpaceDir.exists()) {
				FileUtils.deleteDirectory(workSpaceDir);
			}
	}

	/**
	 * This method creates the workspace directory for tests to carry on
	 */
	private void makeWorkSpaceDir() {
		File workSpaceDir = new File(UITestConstants.TARGET_FOLDER_LOC);
		if (!workSpaceDir.exists()) {
			workSpaceDir.mkdir(); // if fails tests should fail
		}
	}
}
