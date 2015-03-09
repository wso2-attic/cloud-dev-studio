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

import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.AsyncRequestFactory;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.codenvy.ide.rest.Unmarshallable;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryLoginInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryLoginResponse;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.utils.AppFactoryExtensionUtils;

/**
 * Provides a view handler to the login dialog box used in App Factory/ App Cloud server login
 */
public class LoginPresenter implements LoginView.ActionDelegate {

    private LoginView loginView;
    private String hostURL;
    private final DtoFactory dtoFactory;
    private final DtoUnmarshallerFactory unmarshallerFactory;
    private final AsyncRequestFactory requestFactory;
    private final String restContext;

    /**
     * Creates a Login Presenter with GWT injected login view object
     */
    @Inject
    public LoginPresenter(@Named("restContext") String restContext, LoginView loginView, DtoFactory dtoFactory, DtoUnmarshallerFactory unmarshallerFactory, AsyncRequestFactory requestFactory) {
        this.loginView = loginView;
        this.loginView.setDelegate(this);
        this.dtoFactory = dtoFactory;
        this.unmarshallerFactory = unmarshallerFactory;
        this.requestFactory = requestFactory;
        this.restContext = restContext;
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
        AppFactoryLoginInfo appFactoryLoginInfo = dtoFactory.createDto(AppFactoryLoginInfo.class);
        appFactoryLoginInfo.setServerURL(loginView.getHostURL());
        appFactoryLoginInfo.setUserName(loginView.getUserName());
        appFactoryLoginInfo.setPassword(loginView.getPassword());
        appFactoryLoginInfo.setAppCloud(loginView.isAppCloudMode());

        Unmarshallable<AppFactoryLoginResponse> unmarshaller = unmarshallerFactory.newUnmarshaller(AppFactoryLoginResponse.class);

        requestFactory.createPostRequest(restContext + "/" + AppFactoryExtensionConstants.AF_CLIENT_REST_SERVICE_PATH
                + "/" + AppFactoryExtensionConstants.AF_CLIENT_LOGIN_METHOD_PATH, appFactoryLoginInfo)
                .send(new AsyncRequestCallback<AppFactoryLoginResponse>(unmarshaller) {
                    @Override
                    protected void onSuccess(AppFactoryLoginResponse result) {
                        String loginSuccessMessage = "Login success to WSO2 "
                                + (loginView.isAppCloudMode() ? "App Cloud" : "App Factory");
                        Window.alert(loginSuccessMessage);
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        String loginFailureMessage = "Unable to login WSO2 "
                                + (loginView.isAppCloudMode() ? "App Cloud" : "App Factory");
                        Window.alert(loginFailureMessage);
                    }
                });
    }

}



