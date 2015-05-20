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
import org.wso2.developerstudio.codenvy.ext.appfactory.server.client.HTTPAppFactoryClient;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.constants.AppFactoryAPIConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryHTTPResponse;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.HTTPRequestInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.LoginRequestInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.LoginResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Implements the logic for connecting cloud Developer Studio GWT based client side with server side using Rest API
 */
@Path("/" + AppFactoryExtensionConstants.AF_REST_SERVICE_PATH)
public class AppFactoryRestService {

    @POST
    @Path(AppFactoryExtensionConstants.AF_REST_LOGIN_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoginResponse login(LoginRequestInfo loginInfo) throws AppFactoryServerException {
        return Authenticator.getInstance().authenticate(loginInfo);
    }

    @POST
    @Path(AppFactoryExtensionConstants.AF_REST_APP_LIST_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AppFactoryHTTPResponse getApplicationList(HTTPRequestInfo appListRequestInfo)
            throws AppFactoryServerException {
        String serverURL = Authenticator.getInstance().getServerURL() +
                AppFactoryAPIConstants.APP_LIST_URL_SFX;
        //return appFactoryHttpResponse;
        return HTTPAppFactoryClient.sendHTTPPostRequest(serverURL,
                appListRequestInfo.getRequestParams());
    }
}
