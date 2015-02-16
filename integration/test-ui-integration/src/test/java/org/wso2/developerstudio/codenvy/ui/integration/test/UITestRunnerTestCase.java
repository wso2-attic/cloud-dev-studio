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
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.ConfigPropertyChanges;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.DevSWebDriver;
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
		String ideURL = ConfigPropertyChanges.getIDEURLTest().replace("\\", "");
		driver.get(ideURL);
		// Initialize elements to be used
		aboutDialogOpen = PageFactory.initElements(driver, AboutActionTest.class);
		aboutDialogOpen.selectWSO2DevStudioMenu();// test the about wizard
	}

	@AfterClass(alwaysRun = true)
	/**
	 * finally close the browser and delete all directories used for testing, clean up
	 */
	public void closeBrowser() throws IOException, InterruptedException {
		if (driver != null) {
			driver.quit();
		}
	}
}
