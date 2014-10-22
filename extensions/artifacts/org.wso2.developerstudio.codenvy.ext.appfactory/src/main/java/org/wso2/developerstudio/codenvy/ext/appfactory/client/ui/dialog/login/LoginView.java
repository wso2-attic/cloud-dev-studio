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
package org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.dialog.login;

import javax.annotation.Nonnull;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.google.inject.ImplementedBy;

@ImplementedBy(LoginViewImpl.class)
public interface LoginView extends View<LoginView.ActionDelegate> {

	/**
	 * Interface for view action delegation
	 */
	public interface ActionDelegate extends BaseActionDelegate {

		/**
		 * Action Handler for OK button
		 */
		void onOKButtonClicked();

		/**
		 * Action Handler for cancel Button
		 */
		void onCancelButtonClicked();

		/**
		 * Performs some actions in response to a app cloud.
		 */
		void onAppCloudChosen();

		/**
		 * Performs some actions in response to a app fac.
		 */
		void onAppFactoryChosen();

		/**
		 * Performs any actions appropriate in response to the user having changed something.
		 */
		void onValueChanged();

	}

	/**
	 * Show Login dialog.
	 */
	void showLoginPrompt();

	/**
	 * @return <code>true</code> if app cloud is chosen, and <code>false</code> otherwise
	 */
	boolean isAppCloudLoginRequest();

	/**
	 * @return <code>true</code> if app factory is chosen, and <code>false</code> otherwise
	 */
	boolean isAppFactoryLoginRequest();

	/**
	 * Select app cloud button.
	 *
	 * @param isAppCloud <code>true</code> to select app cloud, <code>false</code> not to select
	 */
	void setAppCloudMode(boolean isAppCloud);

	/**
	 * Select app fac button.
	 *
	 * @param isAppFactory <code>true</code> to select app factory, <code>false</code> not to select
	 */
	void setAppFactoryMode(boolean isAppFactory);

	/**
	 * @return Wso2 App Cloud
	 */
	@Nonnull String getHostURL();

	/**
	 * Set Wso2 Appcloud.
	 *
	 * @param hostURL Cloud
	 */
	void setHostURL(@Nonnull String hostURL);

	/**
	 * @return Email
	 */
	@Nonnull String getUserName();

	/**
	 * Set Email.
	 *
	 * @param userName
	 */
	void setUserName(@Nonnull String userName);

	/**
	 * @return Wso2 App Cloud
	 */
	@Nonnull String getPassword();

	/**
	 * Set Password .
	 *
	 * @param password
	 */
	void setPassword(@Nonnull String password);

	/**
	 * Close dialog.
	 */
	void close();
}
