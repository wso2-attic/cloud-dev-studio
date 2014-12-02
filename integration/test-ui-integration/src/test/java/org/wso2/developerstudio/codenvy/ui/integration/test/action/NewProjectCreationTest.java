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

package org.wso2.developerstudio.codenvy.ui.integration.test.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.ConfigPropertyChanges;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.DevSWebDriver;
import org.wso2.developerstudio.codenvy.ui.integration.test.utils.UITestConstants;

import java.io.IOException;

import static org.testng.Assert.assertTrue;

/**
 * New Project Creation Test for developer studio UI new project creation wizard
 */
public class NewProjectCreationTest {
	private static final String XPATH = "xpath";
	private static final String ID = "id";
	private final DevSWebDriver driver;

	public NewProjectCreationTest(DevSWebDriver driver) {
		this.driver = driver;
	}

	/**
	 * tests the WSO2 App Server project creation UI
	 * @throws IOException of web element not found, handled by the DevSWebDriver
	 */
	public void createNewWSO2AppServerProject() throws IOException {

		WebElement wso2FileMenu = getWebElemById(ID, UITestConstants.WSO2_FILE_MENU_ID);
		WebElement wso2NewProjectOption = getWebElemById(ID, UITestConstants.WSO2_NEW_PROJECT_OPTION_ID);
		WebElement wso2NewOption = getWebElemById(ID, UITestConstants.WSO2_FILE_NEW_OPTION_ID);
		WebElement wso2NewProjectCategoriesList = getWebElemById(ID, UITestConstants.WSO2_NEW_PROJECT_OPTIONS_DROP_DOWN_XPATH);
		WebElement wso2NewProjectName = getWebElemById(ID, UITestConstants.WSO2_NEW_PROJECT_NAME_OPTION_ID);
		WebElement wso2NewProjectDescription = getWebElemById(ID, UITestConstants.WSO2_NEW_PROJECT_DESCRIPTION_ID);
		WebElement wso2NewProjectNextButton = getWebElemById(XPATH, UITestConstants.PROJECT_CREATION_WIZARD_NEXT_BUTTON_XPATH);
		WebElement wso2NewProjectArtifactId = getWebElemById(ID, UITestConstants.WSO2_NEW_PROJECT_ARTIFACTID_ID);
		WebElement wso2NewProjectGroupId = getWebElemById(ID, UITestConstants.WSO2_NEW_PROJECT_GROUPID_ID);
		WebElement wso2NewProjectVersionId = getWebElemById(ID, UITestConstants.WSO2_NEW_PROJECT_VERION_ID);
		WebElement wso2NewProjectCreateButton = getWebElemById(XPATH, UITestConstants.PROJECT_CREATION_WIZARD_CREATE_BUTTON_XPATH);
		WebElement wso2NewProject;
		// create test projects for all three project types of App Server and test the UI
		for (int projectTypeOrder = 1; projectTypeOrder <= UITestConstants.NO_OF_APP_SERVER_PROJECTS; projectTypeOrder++) {
			wso2FileMenu.click();
			wso2NewOption.click();
			wso2NewProjectOption.click();
			wso2NewProjectCategoriesList.click();

			String dynamicProjectXpath = ConfigPropertyChanges.getUIElemProperty(
					UITestConstants.WSO2_NEW_PROJECT_TYPE_XPATH) + "div[" + projectTypeOrder + "]";
			wso2NewProject = getWebElemById(XPATH, dynamicProjectXpath);
			wso2NewProject.click();

			String projectType = wso2NewProject.getText();

			switch (projectType) {
				case UITestConstants.JAX_RS_PROJECT_TYPE:
					wso2NewProjectName.sendKeys(UITestConstants.TEST_JAXRS_PROJECT_NAME);

				case UITestConstants.JAX_WS_PROJECT_TYPE:
				    wso2NewProjectName.sendKeys(UITestConstants.TEST_JAXWS_PROJECT_NAME);

				case UITestConstants.WEB_APP_PROJECT_TYPE:
					wso2NewProjectName.sendKeys(UITestConstants.TEST_WEBAPP_PROJECT_NAME);

				default:
					assertTrue(false,"The returned new project type by xpath is an unexpected project type " + projectType);
			}

			wso2NewProjectDescription.sendKeys(UITestConstants.TEST_PROJECT_DESCRIPTION);
			wso2NewProjectNextButton.click();

			wso2NewProjectArtifactId.sendKeys(UITestConstants.TEST_PROJECT_ARTIFACT_ID);
			wso2NewProjectGroupId.sendKeys(UITestConstants.TEST_PROJECT_GROUP_ID);
			wso2NewProjectVersionId.sendKeys(UITestConstants.TEST_PROJECT_VERSION_ID);
			wso2NewProjectCreateButton.click();

		}
	}

	/**
	 * This method returns the corresponding web element by the id OR xpath
	 *
	 * @param searchType search element by type
	 * @param searchVal value to search the web element
	 * @return the corresponding web element
	 * @throws IOException of web element not found, handled by the DevSWebDriver
	 */
	private WebElement getWebElemById(String searchType, String searchVal) throws IOException {
		if (searchType.equals(XPATH)) {
			return driver.findElement(By.xpath(ConfigPropertyChanges.getUIElemProperty(searchVal)));
		} else {
			return driver.findElement(By.id(ConfigPropertyChanges.getUIElemProperty(searchVal)));
		}

	}
}
