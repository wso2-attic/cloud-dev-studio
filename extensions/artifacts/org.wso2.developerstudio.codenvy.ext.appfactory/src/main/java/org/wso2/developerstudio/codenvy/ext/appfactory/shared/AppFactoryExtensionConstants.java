/*
* Copyright (c) 2014 -2015s, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.codenvy.ext.appfactory.shared;

/**
 * Class for keeping constant values used in App Factory extension module
 */
public interface AppFactoryExtensionConstants {

    String EXT_NAME = "App Factory Tools";
    String EXT_VERSION = "1.0.0";

    String OPEN_AF_PERSPECTIVE_ACTION_ID = "wso2AFOpenPerspectiveAction";
    String OPEN_AF_PERSPECTIVE_ACTION_NAME = "App Factory Perspective";

    String WSO2_APP_FAC_VIEW_APP_LIST = "Applications List";
    String WSO2_APP_FAC_VIEW_CONSOLE = "Console";
    String WSO2_APP_FAC_VIEW_APP_DETAILS = "Application Details";

    String AF_REST_SERVICE_PATH = "afclient";
    String AF_REST_LOGIN_PATH = "login";
    String AF_REST_APP_LIST_PATH = "app_list";

    String WSO2_APP_CLOUD_URL_LABEL = "WSO2 App Cloud: *";
    String WSO2_APP_CLOUD_USER_LABEL = "Email: *";
    String WSO2_APP_FACTORY_URL_LABEL = "App Factory URL: *";
    String WSO2_APP_FACTORY_USER_LABEL = "User: *";

    String APP_CLOUD_APP_FACTORY_LOGIN_TITLE = "App Cloud/ App Factory Login";

    String URL_PREFIX_HTTP = "http:";
    String URL_PREFIX_HTTPS = "https:";
    String URL_PREFIX_FTP = "ftp:";
    String COLON_SEPARATOR = ":";

    String INVALID_HOST_URL_ERROR_MESSAGE = "Invalid Host URL";

    String LOGIN_ACTION = "Login";
    String REFRESH_ACTION = "Refresh";

    String EMPTY_STRING = "";

    String LOGIN_FAILED_PLEASE_TRY_AGAIN_MESSAGE = "Login failed, Please try again...";
    String LOADING_APPLICATIONS_FAILED_MESSGE = "Loading Applications Failed...";
    String UNEXPECTED_ERROR_OCCURRED_MESSAGE = "An Unexpected Error Occurred";
    String LOADING_APPLICATIONS_MESSAGE = "Loading Applications...";
    String APP_CLOUD_WITH_ZERO_TENANTS_ERROR_MESSAGE = "The App Cloud given has zero tenants";
    String LOGIN_WAIT_MESSAGE = "Please Wait, Logging into WSO2 ";
    String APP_CLOUD_LABEL = "App Cloud";
    String APP_FACTORY_LABEL = "App Factory";
    String LOGIN_SUCCESS_MESSAGE = "Login success to WSO2 ";
    String LOGIN_FAILED_MESSAGE = "Unable to login WSO2 ";
}
