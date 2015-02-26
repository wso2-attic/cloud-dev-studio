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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;

import javax.annotation.Nonnull;

/**
 * Class that constructs login dialog box for App Factory login using GWT components
 */
public class LoginViewImpl extends DialogBox implements LoginView {

    /**
     * Interface defines the UI binder for <code>LoginViewImpl</code> class with <code>LoginViewImpl.ui.xml</code> file
     */
    interface LoginViewImplUiBinder extends UiBinder<DialogBox, LoginViewImpl> {
        //No body, since it is only for binding purpose
    }

    private ActionDelegate delegate;
    private Widget rootElement;

    @UiField
    RadioButton appCloudCheckButton;
    @UiField
    RadioButton appFactoryCheckButton;
    @UiField
    Button okButton;
    @UiField
    Button cancelButton;
    @UiField
    TextBox hostURL;
    @UiField
    TextBox userName;
    @UiField
    PasswordTextBox password;
    @UiField
    Label hostURLLabel;
    @UiField
    Label userNameLabel;
    @UiField
    Label errorMessage;

    /**
     * Creates a Login View implementation with GWT injected UI binder
     */
    @Inject
    public LoginViewImpl(LoginViewImplUiBinder loginPromptUIBinder) {
        rootElement = loginPromptUIBinder.createAndBindUi(this);
        this.setWidget(rootElement);
        setText(AppFactoryExtensionConstants.APP_CLOUD_APP_FACTORY_LOGIN_TITLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDelegate(ActionDelegate actionDelegate) {
        this.delegate = actionDelegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Widget asWidget() {
        return rootElement;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return AppFactoryExtensionConstants.APP_CLOUD_APP_FACTORY_LOGIN_TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showLoginPrompt() {
        this.center();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeLoginPrompt() {
        this.hide();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAppCloudMode() {
        return appCloudCheckButton.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAppCloudMode(boolean isAppCloud) {
        appCloudCheckButton.setValue(isAppCloud);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAppFactoryMode() {
        return appFactoryCheckButton.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAppFactoryMode(boolean isAppFactory) {
        appFactoryCheckButton.setValue(isAppFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHostURL() {
        return hostURL.getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHostURL(@Nonnull String hostURL) {
        this.hostURL.setText(hostURL);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getUserName() {
        return userName.getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserName(@Nonnull String userName) {
        this.userName.setText(userName);
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getPassword() {
        return password.getText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(@Nonnull String password) {
        this.password.setText(password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHostURLLabel(@Nonnull String hostURLLabel) {
        this.hostURLLabel.setText(hostURLLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserNameLabel(@Nonnull String userNameLabel) {
        this.userNameLabel.setText(userNameLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage.setText(errorMessage);
    }

    @UiHandler("okButton")
    public void onOKButtonClicked(ClickEvent event) {
        delegate.onOKButtonClicked();
    }

    @UiHandler("cancelButton")
    public void onCancelButtonClicked(ClickEvent event) {
        delegate.onCancelButtonClicked();
    }

    @UiHandler("appCloudCheckButton")
    public void onAppCloudClicked(ClickEvent event) {
        delegate.onAppCloudChosen();
    }

    @UiHandler("appFactoryCheckButton")
    public void onAppFactoryClicked(ClickEvent event) {
        delegate.onAppFactoryChosen();
    }

    @UiHandler("hostURL")
    public void onHostChange(KeyUpEvent event) {
        delegate.onValueChanged();
    }

}
