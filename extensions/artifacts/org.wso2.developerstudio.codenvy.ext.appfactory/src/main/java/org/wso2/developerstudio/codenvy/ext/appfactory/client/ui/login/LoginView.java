/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.login;

import javax.annotation.Nonnull;

import org.eclipse.che.ide.api.mvp.View;
import com.google.inject.ImplementedBy;

/**
 * Interface for defining login dialog box view specific operations
 */
@ImplementedBy(LoginViewImpl.class)
public interface LoginView extends View<LoginView.ActionDelegate> {

    /**
     * Interface for view action delegation
     */
    public interface ActionDelegate {

        /**
         * Action Handler for OK button clicked
         */
        void onOKButtonClicked();

        /**
         * Action Handler for cancel Button clicked
         */
        void onCancelButtonClicked();

        /**
         * Performs some actions in response to a app cloud is chosen
         */
        void onAppCloudChosen();

        /**
         * Performs some actions in response to a app fac is chosen
         */
        void onAppFactoryChosen();

        /**
         * Performs any actions appropriate in response to the user having changed something
         */
        void onValueChanged();

    }

    /**
     * Show login dialog
     */
    void showLoginPrompt();

    /**
     * Close login dialog
     */
    void closeLoginPrompt();

    /**
     * @return <code>true</code> if app cloud is chosen, and <code>false</code> otherwise
     */
    boolean isAppCloudMode();

    /**
     * Select app cloud button
     *
     * @param isAppCloud <code>true</code> to select app cloud, <code>false</code> not to select
     */
    void setAppCloudMode(boolean isAppCloud);

    /**
     * @return <code>true</code> if app factory is chosen, and <code>false</code> otherwise
     */
    boolean isAppFactoryMode();

    /**
     * Select app fac button
     *
     * @param isAppFactory <code>true</code> to select app factory, <code>false</code> not to select
     */
    void setAppFactoryMode(boolean isAppFactory);

    /**
     * @return App Factory/ App Cloud URL
     */
    String getHostURL();

    /**
     * Set App Factory/ App Cloud URL
     *
     * @param hostURL App Factory/ App Cloud URL
     */
    void setHostURL(@Nonnull String hostURL);

    /**
     * @return User ID for login, it can be email or username
     */
    @Nonnull
    String getUserName();

    /**
     * Set User ID for login, it can be email or username
     *
     * @param userName User Id
     */
    void setUserName(@Nonnull String userName);

    /**
     * @return Wso2 App Cloud
     */
    @Nonnull
    String getPassword();

    /**
     * Set Password .
     *
     * @param password for login
     */
    void setPassword(@Nonnull String password);

    /**
     * Set host URL label
     *
     * @param hostURLLabel App Factory/ App Cloud URL text box label
     */
    void setHostURLLabel(@Nonnull String hostURLLabel);

    /**
     * Set user label
     *
     * @param userNameLabel User text box label
     */
    void setUserNameLabel(@Nonnull String userNameLabel);

    /**
     * Set error message to show in login window
     *
     * @param errorMessage message
     */
    void setMessage(String errorMessage);

    /**
     * Enable Login button of Login window
     *
     */
    void enableLoginButton(boolean enable);

}
