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
package org.wso2.developerstudio.cloud.appfactory.ext.client.ui.dialog.login;

import com.codenvy.ide.api.parts.ConsolePart;
import com.google.inject.Inject;

public class LoginPresenter implements LoginView.ActionDelegate {

    private LoginView view;
    private ConsolePart consolePart;
    private String title;
    private String wso2AppCloud;
    private String email;
    private String password;

    @Inject
    public LoginPresenter(LoginView view, ConsolePart consolePart) {
        this.view = view;
        this.consolePart = consolePart;
        this.view.setDelegate(this);
    }

    @Override
    public void onValueChanged() {

        wso2AppCloud = view.getHostURL();
        email = view.getUserName();
        password = view.getPassword();

    }

    /**
     * Show Login dialog.
     */
    public void showDialog() {
        view.showLoginPrompt();
    }

    @Override
    public void minimize() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAppCloudChosen() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAppFactoryChosen() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelButtonClicked() {
        view.close();
    }

    @Override
    public void onOKButtonClicked() {

    }

}



