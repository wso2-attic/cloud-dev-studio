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
package org.wso2.developerstudio.codenvy.ext.appserver.shared;

public interface AppServerExtConstants {

	String EXT_NAME = "Application Server Extension";
	String EXT_VERSION = "1.0.0";

	String WSO2_APP_SERVER_PROJECT_CATEGORY_ID = "WSO2 Application Server";
	String WSO2_WEB_APP_PROJECT_NAME = "Java Web Application";
	String WSO2_WEB_APP_PROJECT_ID = "wso2AppServerWebAppProject";
	String WSO2_JAX_WS_PROJECT_NAME = "JAX WS Project";
	String WSO2_JAX_WS_PROJECT_ID = "wso2AppServerJAXWSProject";
	String WSO2_JAX_RS_PROJECT_NAME = "JAX RS Project";
	String WSO2_JAX_RS_PROJECT_ID = "wso2AppServerJAXRSProject";

	String WAR_PACKAGING = "war";

	String WSO2_APP_SERVER_RUNNER_NAME = "WSO2ApplicationServer";
	String WSO2_APP_SERVER_PROJECT_GENERATOR_ID = "wso2AppServerProjectGenerator";

	String JAVA_LANGUAGE = "java";
	String MAVEN_BUILDER = "maven";

	String LANGUAGE = "language";
	String LANGUAGE_VERSION = "language.version";
	String FRAMEWORK = "framework";
	String BUILDER_NAME = "builder.name";
	String BUILDER_MAVEN_SOURCE_FOLDERS = "builder.maven.source_folders";
	String RUNNER_NAME = "runner.name";

	// Project Generator Options
	String PROJECT_TYPE_ID = "ProjectType";

	String PUBLIC_VISIBILITY = "public";
	String PRIVATE_VISIBILITY = "private";

	// Sample project file names
	String WEB_APP_SAMPLE_PROJECT = "HelloWorldWebapp.zip";
	String JAX_RS_SAMPLE_PROJECT = "JAXRSSampleService.zip";
	String JAX_WS_SAMPLE_PROJECT = "JAXWSSampleService.zip";

	// generator Info
	String GENERATOR_PROJECT_TYPE = "wso2.appserver.project.generator.project.type";
	String GENERATOR_PROJECT_NAME = "wso2.appserver.project.generator.project.name";
	String GENERATOR_PACKAGE_NAME = "wso2.appserver.project.generator.package.name";
	String GENERATOR_CLASS_NAME =  "wso2.appserver.project.generator.class.name";
	String GENERATOR_WEB_CONTEXT_ROOT = "wso2.appserver.project.generator.webcontextroot";
	String GENERATOR_WEB_CONTENT_FOLDER= "wso2.appserver.project.generator.webcontenfolder";


}
