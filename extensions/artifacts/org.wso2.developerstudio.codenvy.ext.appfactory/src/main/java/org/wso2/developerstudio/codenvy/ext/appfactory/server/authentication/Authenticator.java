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

import org.wso2.developerstudio.codenvy.ext.appfactory.server.client.HTTPAppFactoryClient;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryLoginInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryLoginResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all the authentication related stuffs from WSO2 Developer Studio to WSO2 AppFactory/ AppCloud
 */
public class Authenticator {
    private static Authenticator authenticator;
    private AppFactoryLoginInfo loginInfo;

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
     * Authenticate user using given info
     *
     * @param loginInfo contains necessary info required for login request
     * @return AFLoginResponse which contains login result
     */
    public AppFactoryLoginResponse authenticate(AppFactoryLoginInfo loginInfo) throws AppFactoryServerException {
        this.loginInfo = loginInfo;
        Map<String, String> loginRequestParams = new HashMap<>();
        loginRequestParams.put("action", "login");
        loginRequestParams.put("userName", loginInfo.getUserName());
        loginRequestParams.put("password", loginInfo.getPassword());

        return HTTPAppFactoryClient.doLogin(loginInfo.getServerURL(), loginRequestParams);
    }
}
