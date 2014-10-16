/*
* Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.codenvy.ext.appfactory.server.jaggery.api.client;

/**
 * App Factory jaggery Rest API Constants
 */
public interface APIConstants {

	String APP_INFO_URL_SFX = "/appmgt/site/blocks/application/get/ajax/list.jag";
	String LOGIN_URL_SFX = "/appmgt/site/blocks/user/login/ajax/login.jag";
	String BUILD_NUMBER_URL_SFX = "/appmgt/site/blocks/build/list/ajax/list.jag";
	String BUILD_INFO_URL_SFX = "/appmgt/site/blocks/build/get/ajax/get.jag";
	String LAST_BUILD_URL_SFX = "/appmgt/site/blocks/buildandrepo/list/ajax/list.jag";
	String Build_APPLICATION_URL_SFX = "/appmgt/site/blocks/lifecycle/add/ajax/add.jag";
	String APP_USER_ROLES_URL_SFX = "/appmgt/site/blocks/application/users/get/ajax/list.jag";
	String APP_DB_INFO_URL = "/appmgt/site/blocks/rssmanager/add/ajax/add.jag";
	String APP_DS_INFO_URL = "/appmgt/site/blocks/resources/datasource/get/ajax/list.jag";
	String FORKED_VERSION_INFO_URL = "/appmgt/site/blocks/buildandrepo/list/ajax/list.jag";

	String USER_APP_LIST__ACTION = "getApplicationsOfUser";
	String App_NIFO_ACTION = "getAppVersionsInStage";
	String App_BUILD_NUMBER_ACTION = "buildinfobyappid";
	String App_BUILD_URL_ACTIONL = "getBuildLogsUrl";
	String App_BUILD_INFO_ACTION = "getbuildandrepodata";
	String FORKED_REPO_INFO_ACTION = "getbuildandrepodataforkedrepo";
	String App_BUILD_ACTION = "createArtifact";
	String App_USERS_ROLES_ACTION = "getUsersOfApplication";
	String App_DB_INFO_ACTION = "getDbUserTemplateInfoForStages";
	String App_DS_INFO_ACTION = "getDatasources";
}
