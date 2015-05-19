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
package org.wso2.developerstudio.codenvy.ext.appfactory.server.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.client.CloudAdminServiceClient;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.client.HTTPAppFactoryClient;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.constants.AppFactoryAPIConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.model.UserCredentials;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.LoginRequestInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.LoginResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all the authentication related stuffs from WSO2 Developer Studio to WSO2 AppFactory/ AppCloud
 */
public class Authenticator {
    private static final Logger log = LoggerFactory.getLogger(Authenticator.class);

    private static Authenticator authenticator;
    private static String serverURL;
    private static UserCredentials userCredentials;
    private static boolean appCloud;
    private static String selectedTenant;

    /**
     * Private constructor for limiting object creation outside the class
     */
    private Authenticator() {
    }

    /**
     * Method returns a single instance of the class
     */
    public static Authenticator getInstance() {
        if (authenticator == null) {
            authenticator = new Authenticator();
        }
        return authenticator;
    }

    /**
     * Authenticate App Factory/ App Cloud user
     *
     * @param loginInfo contains necessary info required for login request
     * @return AFLoginResponse which contains login result
     */
    public LoginResponse authenticate(LoginRequestInfo loginInfo) throws AppFactoryServerException {
        serverURL = loginInfo.getServerURL();
        userCredentials = new UserCredentials(loginInfo.getUserName(), loginInfo.getPassword());
        appCloud = loginInfo.isAppCloud();

        //If App Cloud is chosen, assigning a tenant to authenticator
        if (isAppCloud()) {
            Map<String, String> tenants = CloudAdminServiceClient.getTenantDomains(userCredentials);
            if (tenants.size() == 0) {
                log.error(AppFactoryExtensionConstants.APP_CLOUD_WITH_ZERO_TENANTS_ERROR_MESSAGE);
                LoginResponse loginResponse = org.eclipse.che.dto.server.DtoFactory.getInstance().
                        createDto(LoginResponse.class);
                loginResponse.setLoggedIn(false);
                loginResponse.setErroneousRequest(true);
                loginResponse.setErrorMessage(AppFactoryExtensionConstants.APP_CLOUD_WITH_ZERO_TENANTS_ERROR_MESSAGE);
                return loginResponse;
            } else if (tenants.size() >= 1) {
                //TODO If there are more than 1 tenant, allow user to select tenant
                selectedTenant = tenants.entrySet().iterator().next().getValue();
            }
        }

        //Populating the request params into a hash map
        Map<String, String> loginRequestParams = new HashMap<>();
        loginRequestParams.put(AppFactoryAPIConstants.ACTION_PARAM, AppFactoryAPIConstants.LOGIN_ACTION);
        loginRequestParams.put(AppFactoryAPIConstants.USERNAME_PARAM, userCredentials.getUserName());
        loginRequestParams.put(AppFactoryAPIConstants.PASSWORD_PARAM, userCredentials.getPassword());

        return HTTPAppFactoryClient.doLogin(loginInfo.getServerURL() + AppFactoryAPIConstants.LOGIN_URL_SFX,
                loginRequestParams);
    }

    public boolean isAppCloud() {
        return appCloud;
    }

    public String getSelectedTenant() {
        return selectedTenant;
    }

    public String getServerURL() {
        return serverURL;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }
}
