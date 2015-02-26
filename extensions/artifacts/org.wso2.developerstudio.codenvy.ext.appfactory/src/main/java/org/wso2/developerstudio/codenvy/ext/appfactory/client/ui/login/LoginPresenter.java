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

import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.utils.AppFactoryExtensionUtils;

/**
 * Provides a view handler to the login dialog box used in App Factory/ App Cloud server login
 */
public class LoginPresenter implements LoginView.ActionDelegate {

    private LoginView loginView;
    private String hostURL;

    /**
     * Creates a Login Presenter with GWT injected login view object
     */
    @Inject
    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.loginView.setDelegate(this);
    }

    /**
     * Show Login dialog.
     */
    public void showDialog() {
        loginView.showLoginPrompt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAppCloudChosen() {
        loginView.setHostURLLabel(AppFactoryExtensionConstants.WSO2_APP_CLOUD_URL_LABEL);
        loginView.setUserNameLabel(AppFactoryExtensionConstants.WSO2_APP_CLOUD_USER_LABEL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAppFactoryChosen() {
        loginView.setHostURLLabel(AppFactoryExtensionConstants.WSO2_APP_FACTORY_URL_LABEL);
        loginView.setUserNameLabel(AppFactoryExtensionConstants.WSO2_APP_FACTORY_USER_LABEL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelButtonClicked() {
        loginView.closeLoginPrompt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onValueChanged() {
        hostURL = loginView.getHostURL();
        boolean isValidURL = AppFactoryExtensionUtils.isValidHostUrl(hostURL);
        if (!isValidURL) {
            loginView.setErrorMessage(AppFactoryExtensionConstants.INVALID_HOST_URL_ERROR_MESSAGE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOKButtonClicked() {
        //TODO - Need to implement the logic with App Factory server side implementation
    }

}



