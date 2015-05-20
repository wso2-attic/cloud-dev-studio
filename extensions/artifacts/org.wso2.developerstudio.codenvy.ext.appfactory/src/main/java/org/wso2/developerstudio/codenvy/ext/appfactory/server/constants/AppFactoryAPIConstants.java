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
package org.wso2.developerstudio.codenvy.ext.appfactory.server.constants;

/**
 * Class for keeping AppFactory API related constants
 */
public class AppFactoryAPIConstants {

    /**
     * URL Suffixes
     */
    public static final String LOGIN_URL_SFX = "/appmgt/site/blocks/user/login/ajax/login.jag";
    public static final String APP_LIST_URL_SFX = "/appmgt/site/blocks/application/get/ajax/list.jag";

    /**
     * Actions
     */
    public static final String LOGIN_ACTION = "login";
    public static final String GET_APP_LIST_ACTION = "getApplicationsOfUser";

    /**
     * Request Parameters
     */
    public static final String ACTION_PARAM = "action";
    public static final String USERNAME_PARAM = "userName";
    public static final String PASSWORD_PARAM = "password";

}
