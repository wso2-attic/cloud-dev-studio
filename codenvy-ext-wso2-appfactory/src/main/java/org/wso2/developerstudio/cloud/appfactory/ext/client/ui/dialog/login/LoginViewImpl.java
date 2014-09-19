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

import com.codenvy.ide.api.parts.PartStackUIResources;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import javax.annotation.Nonnull;

public class LoginViewImpl extends DialogBox implements LoginView {

    interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl> {
    }

    private ActionDelegate delegate;

    private Widget rootElement;

    @UiField
    RadioButton radioBtnAppCloud;
    @UiField
    RadioButton radioBtnAppFactory;
    @UiField
    Button btnOK;
    @UiField
    Button btnCancel;
    @UiField
    TextBox inputHostURL;
    @UiField
    TextBox inputUserName;
    @UiField
    PasswordTextBox inputPassword;


    @Inject
    public LoginViewImpl(LoginViewImplUiBinder loginPromptUIBinder) {
        rootElement = loginPromptUIBinder.createAndBindUi(this);
        this.setWidget(rootElement);
    }

    @UiHandler("btnOK")
    public void onOKButtonClicked(ClickEvent event) {
        delegate.onOKButtonClicked();
    }

    @UiHandler("btnCancel")
    public void onCancelButtonClicked(ClickEvent event) {
        delegate.onCancelButtonClicked();
    }

    @UiHandler("radioBtnAppCloud")
    public void onAppCloudClicked(ClickEvent event) {
        delegate.onAppCloudChosen();
    }

    @UiHandler("radioBtnAppFactory")
    public void onAppFactoryClicked(ClickEvent event) {
        delegate.onAppFactoryChosen();
    }

    @UiHandler("inputHostURL")
    public void onHostChange(KeyUpEvent event) {
        delegate.onValueChanged();
    }

    @Override
    public void setDelegate(ActionDelegate actionDelegate) {
        this.delegate = actionDelegate;
    }

    @Override
    public Widget asWidget() {
        return rootElement;
    }

    @Override
    public void showLoginPrompt() {
        this.center();
        this.show();
    }

    @Override
    public void close() {
        this.hide();
    }

    @Override
    public boolean isAppCloudLoginRequest() {
        return radioBtnAppCloud.getValue();
    }

    @Override
    public boolean isAppFactoryLoginRequest() {
        return radioBtnAppFactory.getValue();
    }

    @Override
    public void setAppCloudMode(boolean isAppCloud) {
        radioBtnAppCloud.setValue(isAppCloud);
    }

    @Override
    public void setAppFactoryMode(boolean isAppFactory) {
        radioBtnAppFactory.setValue(isAppFactory);
    }

    @Override
    public String getHostURL() {
        return inputHostURL.getText();
    }

    @Override
    public void setHostURL(@Nonnull String hostURL) {
        inputHostURL.setText(hostURL);
    }

    @Nonnull
    @Override
    public String getUserName() {
        return inputUserName.getText();
    }

    @Override
    public void setUserName(@Nonnull String userName) {
        inputUserName.setText(userName);
    }

    @Nonnull
    @Override
    public String getPassword() {
        return inputPassword.getText();
    }

    @Override
    public void setPassword(@Nonnull String password) {
        this.inputPassword.setText(password);
    }


}
