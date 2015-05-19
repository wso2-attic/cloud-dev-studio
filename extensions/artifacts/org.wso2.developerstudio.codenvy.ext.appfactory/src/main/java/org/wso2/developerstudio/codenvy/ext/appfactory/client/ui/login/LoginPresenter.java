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

import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.DtoUnmarshallerFactory;
import org.eclipse.che.ide.rest.Unmarshallable;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.LoginRequestInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.LoginResponse;

/**
 * Provides a view handler to the login dialog box used in App Factory/ App Cloud server login
 */
public class LoginPresenter implements LoginView.ActionDelegate {

    private LoginView loginView;
    private final DtoFactory dtoFactory;
    private final DtoUnmarshallerFactory unmarshallerFactory;
    private final AsyncRequestFactory requestFactory;
    private final String restContext;
    private NotificationManager notificationManager;

    /**
     * Creates a Login Presenter with GWT injected login view object
     */
    @Inject
    public LoginPresenter(@Named("restContext") String restContext, LoginView loginView, DtoFactory dtoFactory,
                          DtoUnmarshallerFactory unmarshallerFactory, AsyncRequestFactory requestFactory,
                          NotificationManager notificationManager) {
        this.loginView = loginView;
        this.loginView.setDelegate(this);
        this.dtoFactory = dtoFactory;
        this.unmarshallerFactory = unmarshallerFactory;
        this.requestFactory = requestFactory;
        this.restContext = restContext;
        this.notificationManager = notificationManager;
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
        if (!loginView.getHostURL().trim().isEmpty() && !loginView.getUserName().trim().isEmpty() &&
                !loginView.getPassword().trim().isEmpty()) {
            loginView.enableLoginButton(true);
        } else {
            loginView.enableLoginButton(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOKButtonClicked() {
        loginView.enableLoginButton(false);

        LoginRequestInfo loginRequestInfo = dtoFactory.createDto(LoginRequestInfo.class);
        loginRequestInfo.setServerURL(loginView.getHostURL());
        loginRequestInfo.setUserName(loginView.getUserName());
        loginRequestInfo.setPassword(loginView.getPassword());
        loginRequestInfo.setAppCloud(loginView.isAppCloudMode());

        String waitMessage = "Please Wait, Logging into WSO2 "
                + (loginView.isAppCloudMode() ? "App Cloud" : "App Factory") + "...";
        loginView.setMessage(waitMessage);

        Unmarshallable<LoginResponse> unmarshaller = unmarshallerFactory.newUnmarshaller(LoginResponse.class);

        requestFactory.createPostRequest(restContext + "/" + AppFactoryExtensionConstants.AF_REST_SERVICE_PATH
                + "/" + AppFactoryExtensionConstants.AF_REST_LOGIN_PATH, loginRequestInfo)
                .send(new AsyncRequestCallback<LoginResponse>(unmarshaller) {
                    @Override
                    protected void onSuccess(LoginResponse loginResponse) {
                        String loginSuccessMessage = "Login success to WSO2 "
                                + (loginView.isAppCloudMode() ? "App Cloud" : "App Factory");
                        String loginFailureMessage = "Unable to login WSO2 "
                                + (loginView.isAppCloudMode() ? "App Cloud" : "App Factory");
                        if (loginResponse.isLoggedIn()) {
                            notificationManager.showInfo(loginSuccessMessage);
                            loginView.closeLoginPrompt();
                        } else {
                            loginFailureMessage += "\n\nReason: " + loginResponse.getErrorMessage();
                            notificationManager.showError(loginFailureMessage);
                            loginView.setMessage("Login failed, Please try again...");
                        }
                        loginView.enableLoginButton(true);
                        loginView.setMessage(AppFactoryExtensionConstants.EMPTY_STRING);
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        String loginFailureMessage = "Unable to login WSO2 "
                                + (loginView.isAppCloudMode() ? "App Cloud" : "App Factory");
                        Window.alert(loginFailureMessage);
                        loginView.enableLoginButton(true);
                        loginView.setMessage(AppFactoryExtensionConstants.EMPTY_STRING);
                    }
                });
    }

}



