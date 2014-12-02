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

/**
 * opens the about action on the UI
 */
public class AboutActionTest {
	final DevSWebDriver driver;

	public AboutActionTest(DevSWebDriver driver) {
		this.driver = driver;
	}

	/**
	 * This Select the WSO2 Developer Studio menu
	 *
	 * @throws IOException
	 */
	public void selectWSO2DevStudioMenu() throws IOException {
		// Create web elements using GWT IDs
		WebElement wso2DevStudioAction = getWebElementById(UITestConstants.WSO2_DEV_STUDIO_GWT_ID);
		WebElement wso2AboutAction = getWebElementById(UITestConstants.WSO2_ABOUT_ACTION_GWT_ID) ;
		WebElement wso2AboutActionClick= getWebElementById(UITestConstants.WSO2_ABOUT_ACTION_SELECTION_CLICK_ID);
		WebElement wso2AboutActionClose = getWebElementById(UITestConstants.WSO_ABOUT_ACTION_CLOSE_BUTTON_ID);

		wso2DevStudioAction.click();
		wso2AboutAction.click();
		wso2AboutActionClick.click();
		wso2AboutActionClose.click();
	}

	/**
	 * This method returns the corresponding web element by the ID
	 * @param searchVal value to search the web element
	 * @return the corresponding web element
	 * @throws IOException
	 */
	private WebElement getWebElementById(String searchVal) throws IOException {
		return driver.findElement(By.id(ConfigPropertyChanges.getUIElemProperty(searchVal)));
	}
}
