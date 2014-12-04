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

package org.wso2.developerstudio.codenvy.ui.integration.test;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.wso2.developerstudio.codenvy.ui.integration.test.action.AboutActionTest;
import org.wso2.developerstudio.codenvy.ui.integration.test.action.NewProjectCreationTest;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.DevSWebDriver;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.ProjectDirectoryStructureTest;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.UITestConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Main test class for UI tests and backend tests that depends on the UI tests.
 * run the tests for UI and related backend
 * Currently implemented only to test the developer-studio-linux64 pack
 */
public class UITestRunnerTestCase {

	static DevSWebDriver driver;
	AboutActionTest aboutDialogOpen;
	NewProjectCreationTest createNewProject;


	@Test(description = "Get about Developer Studio About Action")
	/**
	 * This method tests the about dialog in the dev studio web application
	 */
	public void testAboutDialogUI() throws Exception {
		driver = new DevSWebDriver();
		System.setProperty(UITestConstants.WEB_DRIVER_FIREFOX_PROFILE, UITestConstants.DEFAULT);
		// Allows WebDriver to poll the DOM for a certain duration until the elements load
		driver.manage().timeouts().implicitlyWait(UITestConstants.WAITING_TIME_CONSTANT, TimeUnit.SECONDS);
		// Navigates to the web page
		driver.get(System.getProperty(UITestConstants.DEVELOPER_STUDIO_IDE_URL));
		// Initialize elements to be used
		aboutDialogOpen = PageFactory.initElements(driver, AboutActionTest.class);
		aboutDialogOpen.selectWSO2DevStudioMenu();// test the about wizard
	}

	@Test(description = "Get about Developer Studio New Project Creation Wizard",
	      dependsOnMethods = "testAboutDialogUI")
	/**
	 * This method tests the new project creation wizard UI on wso2 dev studio
	 */
	public void testNewProjectCreationUI() throws IOException {
		createNewProject = PageFactory.initElements(driver, NewProjectCreationTest.class);
		createNewProject.createNewWSO2AppServerProject();// test the new project creation wizard for All WSO2 Application Server Project Types
	}

	@Test(description = "validating and deleting the folder structures of JAXRSProject created",
	      dependsOnMethods = "testNewProjectCreationUI")
	/**
	 *This method tests the project structure of the JAX RS project created by the dev studio
	 */
	public void testJaxRSProjectStructure() {
		ProjectDirectoryStructureTest projectStructureValidation = new ProjectDirectoryStructureTest();
		projectStructureValidation
				.testDirectoryStructure(UITestConstants.JAX_RS_PROJECT_TYPE, UITestConstants.TEST_JAXRS_PROJECT_NAME);
	}

	@Test(description = "validating and deleting the folder structures of JAXWSProject created",
	      dependsOnMethods = "testNewProjectCreationUI")
	/**
	 *This method tests the project structure of the JAX WS project created by the dev studio
	 */
	public void testJaxWSProjectStructure() {
		ProjectDirectoryStructureTest projectStructureValidation = new ProjectDirectoryStructureTest();
		projectStructureValidation
				.testDirectoryStructure(UITestConstants.JAX_WS_PROJECT_TYPE, UITestConstants.TEST_JAXWS_PROJECT_NAME);
	}

	@Test(description = "validating and deleting the folder structures of Web Application created",
	      dependsOnMethods = "testNewProjectCreationUI")
	/**
	 *This method tests the project structure of the Web Application project created by the dev studio
	 */
	public void testWebAppProjectStructure() {
		ProjectDirectoryStructureTest projectStructureValidation = new ProjectDirectoryStructureTest();
		projectStructureValidation
				.testDirectoryStructure(UITestConstants.WEB_APP_PROJECT_TYPE, UITestConstants.TEST_WEBAPP_PROJECT_NAME);
	}

	@AfterClass(alwaysRun = true)
	/**
	 * finally close the browser and delete all directories used for testing, clean up
	 */
	public void closeBrowser() throws IOException, InterruptedException {
		driver.quit();
	}
}
