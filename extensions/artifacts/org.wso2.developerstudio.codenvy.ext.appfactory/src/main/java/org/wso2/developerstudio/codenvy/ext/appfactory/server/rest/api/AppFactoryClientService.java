/*
* Copyright (c) 2014-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.codenvy.ext.appfactory.server.rest.api;

import org.wso2.developerstudio.codenvy.ext.appfactory.server.authentication.Authenticator;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryLoginInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryLoginResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Implements the logic for connecting cloud Developer Studio GWT based client side with server side using Rest API
 */
@Path("/" + AppFactoryExtensionConstants.AF_CLIENT_REST_SERVICE_PATH)
public class AppFactoryClientService {

    @POST
    @Path(AppFactoryExtensionConstants.AF_CLIENT_LOGIN_METHOD_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AppFactoryLoginResponse login(AppFactoryLoginInfo loginInfo) throws AppFactoryServerException {
        return Authenticator.getInstance().authenticate(loginInfo);
    }
}
