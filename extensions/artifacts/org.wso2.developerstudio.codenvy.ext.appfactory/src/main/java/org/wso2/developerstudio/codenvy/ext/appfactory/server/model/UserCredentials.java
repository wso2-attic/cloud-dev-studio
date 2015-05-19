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
package org.wso2.developerstudio.codenvy.ext.appfactory.server.model;

import org.wso2.developerstudio.codenvy.ext.appfactory.server.authentication.Authenticator;

/**
 * A bean class that holds App Factory/ App Cloud user login credentials
 */
public class UserCredentials {
    private String userName;
    private String password;

    public UserCredentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getTenantAwareUserName() {
        if (Authenticator.getInstance().isAppCloud()) {
            return userName.replaceFirst("@", ".");
        } else {
            return userName;
        }
    }

    public String getUserName() {
        if (Authenticator.getInstance().isAppCloud()) {
            String userName = this.userName.replaceFirst("@", ".");
            userName += "@" + Authenticator.getInstance().getSelectedTenant();  //append the tenant with username and return
            return userName;
        } else {
            return userName;
        }
    }

    public String getPassword() {
        return password;
    }
}
