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
package org.wso2.developerstudio.codenvy.ext.appserver.server.project;

import com.codenvy.api.core.ConflictException;
import com.codenvy.api.core.ForbiddenException;
import com.codenvy.api.core.ServerException;
import com.codenvy.api.project.server.FolderEntry;
import com.codenvy.api.project.server.ProjectGenerator;
import com.codenvy.api.project.server.ProjectGeneratorRegistry;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.AppServerExtConstants;

import java.util.Map;

/**
 * Project generator for wso2 app server web apps
 */
@Singleton
public class WSO2AppServerProjectGenerator implements ProjectGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(WSO2AppServerProjectGenerator.class);

	@Inject
	public WSO2AppServerProjectGenerator(ProjectGeneratorRegistry registry) {
		registry.register(this);
	}

	@Override
	public String getId() {
		return AppServerExtConstants.WSO2_APP_SERVER_PROJECT_GENERATOR_ID;
	}

	@Override
	public void generateProject(FolderEntry folderEntry, Map<String, String> options)
			throws ForbiddenException,
			       ConflictException, ServerException {

		// TODO: Change below sample code to create sample projects as necessary
		try {

			if (options.get(AppServerExtConstants.PROJECT_TYPE_ID).equals(AppServerExtConstants
					                                                              .WSO2_WEB_APP_PROJECT_ID)) {

				folderEntry.createFolder("src/main/webapp/");
				folderEntry.createFolder("src/main/webapp/WEB-INF");
				String content = "<web-app xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
				                 "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				                 "\txsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee\n" +
				                 "\thttp://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"\n" +
				                 "\tversion=\"2.5\">\n" +
				                 "</web-app>";

				folderEntry.createFile("src/main/webapp/WEB-INF/web.xml", content.getBytes(),
				                       "text/xml");

			} else if (options.get(AppServerExtConstants.PROJECT_TYPE_ID)
			                  .equals(AppServerExtConstants
					                          .WSO2_JAX_WS_PROJECT_ID)) {
				folderEntry.createFile("jaxws.txt", "App Server JAXWS Service Artifact.".getBytes(),
				                       "text/plain");

			} else if (options.get(AppServerExtConstants.PROJECT_TYPE_ID)
			                  .equals(AppServerExtConstants
					                          .WSO2_JAX_RS_PROJECT_ID)) {
				folderEntry.createFile("jaxrs.txt", "App Server JAXRS Service Artifact.".getBytes(),
				                       "text/plain");
			} else {
				LOG.error("Error generating project " + folderEntry.getName() +
				          ".\nError : Project type is not " +
				          "supported by " + getId() + ".");
			}

		} catch (Exception e) {
			LOG.error("Error generating project " + folderEntry.getName() + "\nError Msg : " +
			          e.getLocalizedMessage
					          (), e);

		}

	}

}