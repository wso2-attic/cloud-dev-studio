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

import static org.testng.Assert.assertTrue;

/**
 * This class tests the directory structure of Application server projects created by Developer studio
 */
public class ProjectDirectoryStructureTest {

	/**
	 * This method checks the directory structure of a project for a particular project type
	 *
	 * @param directoryTypeToTest the directory type to test the structure
	 * @param projectName         name of the project that is being tested
	 */
	public void testDirectoryStructure(String directoryTypeToTest, String projectName) {

		switch (directoryTypeToTest) {

			case UITestConstants.JAX_RS_PROJECT_TYPE:
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.JAVA), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.TEST + File.separator + UITestConstants.JAVA), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.WEB_CONTENT + File.separator +
						UITestConstants.META_INF + File.separator + UITestConstants.MANIFEST_XML), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.WEB_CONTENT + File.separator +
						UITestConstants.WEB_INF + File.separator + UITestConstants.WEB_XML), projectName);
				testFileExist(new File(projectName + File.separator + UITestConstants.POM_XML), projectName);
				break;

			case UITestConstants.JAX_WS_PROJECT_TYPE:
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.JAVA
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.TEST + File.separator + UITestConstants.JAVA
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.WEB_APP +
						File.separator + UITestConstants.WEB_INF + File.separator +
						UITestConstants.WEB_XML
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.WEB_APP +
						File.separator + UITestConstants.WEB_INF + File.separator +
						UITestConstants.CXF_SERVLET_XML
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.WEB_APP +
						File.separator + UITestConstants.META_INF + File.separator +
						UITestConstants.WEB_APP_CLASS_LOADING_XML
				), projectName);
				testFileExist(new File(projectName + File.separator + UITestConstants.POM_XML),
				              projectName);
				break;

			case UITestConstants.WEB_APP_PROJECT_TYPE:
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.JAVA
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.TEST + File.separator + UITestConstants.JAVA
				), projectName);
				testFileExist(new File(
		               projectName + File.separator + UITestConstants.SOURCE + File.separator +
		               UITestConstants.MAIN + File.separator + UITestConstants.RESOURCES
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.WEB_APP +
						File.separator + UITestConstants.WEB_INF + File.separator +
						UITestConstants.WEB_XML
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.WEB_APP +
						File.separator + UITestConstants.WEB_INF + File.separator +
						UITestConstants.CXF_SERVLET_XML
				), projectName);
				testFileExist(new File(
						projectName + File.separator + UITestConstants.SOURCE + File.separator +
						UITestConstants.MAIN + File.separator + UITestConstants.WEB_APP +
						File.separator + UITestConstants.META_INF + File.separator +
						UITestConstants.WEB_APP_CLASS_LOADING_XML
				), projectName);
				testFileExist(new File(projectName + File.separator + UITestConstants.POM_XML),
				              projectName);
				break;

			default:
				assertTrue(false, "The project type sent does not have a directory structure test case");
				break;
		}

	}

	/**
	 * This method takes the File to test and assert if exists,
	 * throws the error message with file name and project name if it does not exist
	 *
	 * @param fileToTest  the file to test for existence
	 * @param projectName project name the file belongs to
	 */
	private void testFileExist(File fileToTest, String projectName) {
		assertTrue(fileToTest.exists(), "File " + fileToTest.getName() + "does not exist in " + projectName);
	}
}
