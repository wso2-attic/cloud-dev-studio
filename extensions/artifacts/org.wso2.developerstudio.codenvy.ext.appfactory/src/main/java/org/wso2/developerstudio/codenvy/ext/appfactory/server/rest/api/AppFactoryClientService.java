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

import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.jaggery.api.client.AppFactoryClient;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AFLoginResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST; 
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

/**
 * Implements the logic for connecting cloud DevStudio with App Factory/ App Cloud server using Rest API
 */
@Path(AppFactoryExtensionConstants.AF_CLIENT_REST_SERVICE_PATH)
public class AppFactoryClientService {

    @Inject
    AppFactoryClient aFClient;

    @POST
    @Path(AppFactoryExtensionConstants.AF_CLIENT_LOGIN_METHOD_PATH)
    public AFLoginResponse login(@FormParam("userName") String userName,
                                 @FormParam("password") String password,
                                 @Context HttpServletRequest request) {
        boolean loggedIn = false;

        HttpSession session = request.getSession();

        if (session.getAttribute("wso2AFLoggedInUser") != null) {
            //Need to implement the logic with App Factory server side implementation
        }

        return new AFLoginResponse(loggedIn);
    }
}
