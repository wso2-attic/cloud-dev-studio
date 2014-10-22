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
import com.codenvy.api.project.server.*;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.maven.tools.MavenUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.maven.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.AppServerExtConstants;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Project generator for wso2 app server web apps
 */
@Singleton
public class WSO2AppServerProjectGenerator implements ProjectGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(WSO2AppServerProjectGenerator.class);
	private InputStream pomStream;
	private BufferedReader reader;
	private Model model;

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

			if (options.get(AppServerExtConstants.GENERATOR_PROJECT_TYPE)
			           .equals(AppServerExtConstants
					                   .WSO2_WEB_APP_PROJECT_ID)) {
				String webContentFolder = options.get(
						AppServerExtConstants.GENERATOR_WEB_CONTENT_FOLDER);
				folderEntry.createFolder(webContentFolder);
				folderEntry.createFolder(webContentFolder + "/webapp/WEB-INF");
				String webINFContent = "<web-app xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
				                 "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				                 "\txsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee\n" +
				                 "\thttp://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\"\n" +
				                 "\tversion=\"2.5\">\n" +
				                 "</web-app>";

				folderEntry.createFile(
						options.get(AppServerExtConstants.GENERATOR_WEB_CONTENT_FOLDER) +
						"/WEB-INF/web.xml", webINFContent.getBytes(),
						"text/xml");



				folderEntry.createFolder(webContentFolder + "/webapp/META-INF");
				String metaINFContent = "Manifest-Version: 1.0\n" +
				                        "Class-Path: \n";
				folderEntry.createFile(
						options.get(AppServerExtConstants.GENERATOR_WEB_CONTENT_FOLDER) +
						"/META-INF/web.xml", metaINFContent.getBytes(),
						"text/xml");


			} else if (options.get(AppServerExtConstants.GENERATOR_PROJECT_TYPE)
			                  .equals(AppServerExtConstants
					                          .WSO2_JAX_WS_PROJECT_ID)) {
				folderEntry.createFolder("src/main/resources/");
				folderEntry.createFolder("src/main/webapp/");
				folderEntry.createFolder("src/main/webapp/WEB-INF");
				folderEntry.createFolder("src/main/webapp/META-INF");

				String packageName = (options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME))
						.replace(".", "/");
				String className = options.get(AppServerExtConstants.GENERATOR_CLASS_NAME);
				String classContent =
						"package " + options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME) +
						";\n\n" +
						"import javax.jws.WebService;\n" +
						"import javax.jws.WebMethod;\n" +
						"import javax.jws.WebParam;\n\n" +
						"@WebService(serviceName = \"" + className + "\")\n" +
						"public class " + className + "{\n\n" +
						"/** This is a sample web service operation */\n" +
						"@WebMethod(operationName = \"hello\")\n" +
						"\tpublic String hello(@WebParam(name = \"name\") String txt) {\n" +
						"\t\treturn \"Hello \" + txt + \" !\";\n" +
						"\t}\n" +
						"}";

				folderEntry.createFolder("src/main/java/" + packageName);
				folderEntry.createFile("src/main/java/" + packageName + "/" + className + ".java",
				                       classContent.getBytes(),
				                       MimeType.APPLICATION_JAVA);

				String classloading =
						"<Classloading xmlns=\"http://wso2.org/projects/as/classloading\">\n" +
						"\t<!-- Parent-first or child-first. Default behaviour is child-first.-->\n" +
						"\t<ParentFirst>false</ParentFirst>\n" +
						"\t<!-- Default environments that contains provides to all the webapps. This can be overridden by individual webapps by specifing required environments \n" +
						"\tTomcat environment is the default and every webapps gets it even if they didn't specify it.-->\n" +
						"\t<Environments>CXF</Environments>\n" +
						"</Classloading>";

				folderEntry.createFile("src/main/webapp/META-INF/webapp-classloading.xml",
				                       classloading.getBytes(),
				                       "text/xml");

				String web = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				             "<web-app version=\"2.5\" xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
				             "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				             "\txsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee \n" +
				             "\thttp://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\">\n" +
				             "<servlet>\n" +
				             "\t<servlet-name>cxf</servlet-name>\n" +
				             "\t<display-name>cxf</display-name>\n" +
				             "\t<description>Apache CXF Endpoint</description>\n" +
				             "\t<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>" +
				             "\t<load-on-startup>1</load-on-startup>\n" +
				             "</servlet>\n" +
				             "<servlet-mapping>" +
				             "\t<servlet-name>cxf</servlet-name>\n" +
				             "\t<url-pattern>/services/*</url-pattern>\n" +
				             "</servlet-mapping>" +
				             "<session-config>\n" +
				             "\t<session-timeout>60</session-timeout>\n" +
				             "</session-config>\n" +
				             "</web-app>\n";

				folderEntry.createFile("src/main/webapp/WEB-INF/web.xml", web.getBytes(),
				                       "text/xml");

				String serveletAddressElement =
						className.replaceAll("([A-Z])", "_$1"); // split CamelCase
				serveletAddressElement = serveletAddressElement.replaceAll("^/_", "/");
				serveletAddressElement = serveletAddressElement.toLowerCase();

				String servlet = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				                 "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:jaxrs=\"http://cxf.apache.org/jaxrs\" xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd http://cxf.apache.org/bindings/soap http://cxf.apache.org/schemas/configuration/soap.xsd http://cxf.apache.org/jaxrs http://cxf.apache.org/schemas/jaxrs.xsd\">\n" +
				                 "\t<jaxws:server id=\"" + className + "\" address=\"/" +
				                 serveletAddressElement + "\">\n" +
				                 "\t\t<jaxws:serviceBean>\n" +
				                 "\t\t\t<bean  class=\"" + className + "Bean\"/>\n" +
				                 "\t\t</jaxws:serviceBean>\n" +
				                 "\t</jaxws:server>\n" +
				                 "</beans>";

				folderEntry
						.createFile("src/main/webapp/WEB-INF/cxf-servlet.xml", servlet.getBytes(),
						            "text/xml");

				pomStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
						"jaxws_pom_template.xml");
				reader = new BufferedReader(new InputStreamReader(pomStream));
				model = MavenUtils.readModel(reader);

				VirtualFileEntry pomFile = folderEntry.getChild("pom.xml");
				InputStream input = ((FileEntry) pomFile).getInputStream();
				Model modelpom = MavenUtils.readModel(input);

				model.setModelVersion(modelpom.getModelVersion());
				model.setGroupId(modelpom.getGroupId());
				model.setArtifactId(modelpom.getArtifactId());
				model.setVersion(modelpom.getVersion());
				model.setName(options.get(AppServerExtConstants.GENERATOR_PROJECT_NAME));

				MavenUtils.writeModel(model, pomFile.getVirtualFile());

			} else if (options.get(AppServerExtConstants.GENERATOR_PROJECT_TYPE)
			                  .equals(AppServerExtConstants
					                          .WSO2_JAX_RS_PROJECT_ID)) {

				folderEntry.createFolder("src/main/resources/");
				folderEntry.createFolder("src/main/webapp/");
				folderEntry.createFolder("src/main/webapp/WEB-INF");
				folderEntry.createFolder("src/main/webapp/META-INF");

				String packageName = (options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME))
						.replace(".", "/");
				String className = options.get(AppServerExtConstants.GENERATOR_CLASS_NAME);
				String classContent =
						"package " + options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME) +
						";\n" +
						"import javax.ws.rs.*;\n\n" +
						"@Path(\"/\")\n" +
						"public class " + className + "{\n\n" +
						"}";

				folderEntry.createFolder("src/main/java/" + packageName);
				folderEntry.createFile("src/main/java/" + packageName + "/" + className + ".java",
				                       classContent.getBytes(), "MimeType.APPLICATION_JAVA");

				String classloading =
						"<Classloading xmlns=\"http://wso2.org/projects/as/classloading\">\n" +
						"\t<!-- Parent-first or child-first. Default behaviour is child-first.-->\n" +
						"\t<ParentFirst>false</ParentFirst>\n" +
						"\t<!-- Default environments that contains provides to all the webapps. This can be overridden by individual webapps by specifing required environments \n" +
						"\tTomcat environment is the default and every webapps gets it even if they didn't specify it.-->\n" +
						"\t<Environments>CXF</Environments>\n" +
						"</Classloading>";

				folderEntry.createFile("src/main/webapp/META-INF/webapp-classloading.xml",
				                       classloading.getBytes(),
				                       "text/xml");

				String web = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				             "<web-app version=\"2.5\" xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
				             "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
				             "\txsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee \n" +
				             "\thttp://java.sun.com/xml/ns/javaee/web-app_2_5.xsd\">\n" +
				             "<servlet>\n" +
				             "\t<servlet-name>cxf</servlet-name>\n" +
				             "\t<display-name>cxf</display-name>\n" +
				             "\t<description>Apache CXF Endpoint</description>\n" +
				             "\t<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>" +
				             "\t<load-on-startup>1</load-on-startup>\n" +
				             "</servlet>\n" +
				             "<servlet-mapping>" +
				             "\t<servlet-name>cxf</servlet-name>\n" +
				             "\t<url-pattern>/services/*</url-pattern>\n" +
				             "</servlet-mapping>" +
				             "<session-config>\n" +
				             "\t<session-timeout>60</session-timeout>\n" +
				             "</session-config>\n" +
				             "</web-app>\n";

				folderEntry.createFile("src/main/webapp/WEB-INF/web.xml", web.getBytes(),
				                       "text/xml");

				String serveletAddressElement =
						className.replaceAll("([A-Z])", "_$1"); // split CamelCase
				serveletAddressElement = serveletAddressElement.replaceAll("^/_", "/");
				serveletAddressElement = serveletAddressElement.toLowerCase();

				String servlet = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				                 "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:jaxrs=\"http://cxf.apache.org/jaxrs\" xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd http://cxf.apache.org/bindings/soap http://cxf.apache.org/schemas/configuration/soap.xsd http://cxf.apache.org/jaxrs http://cxf.apache.org/schemas/jaxrs.xsd\">\n" +
				                 "\t<bean  id=\"" + className + "Bean\" class=\"" +
				                 options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME) + "." +
				                 className + "\"/>\n" +
				                 "\t<jaxrs:server id=\"" + className + "\" address=\"/" +
				                 serveletAddressElement + "\">\n" +
				                 "\t\t<jaxrs:serviceBean>\n" +
				                 "\t\t\t<bean  class=\"" + className + "Bean\"/>\n" +
				                 "\t\t</jaxrs:serviceBean>\n" +
				                 "\t</jaxrs:server>\n" +
				                 "</beans>";

				folderEntry
						.createFile("src/main/webapp/WEB-INF/cxf-servlet.xml", servlet.getBytes(),
						            "text/xml");

				pomStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
						"jaxrs_pom_template.xml");
				reader = new BufferedReader(new InputStreamReader(pomStream));
				model = MavenUtils.readModel(reader);

				VirtualFileEntry pomFile = folderEntry.getChild("pom.xml");
				InputStream input = ((FileEntry) pomFile).getInputStream();
				Model modelpom = MavenUtils.readModel(input);

				model.setModelVersion(modelpom.getModelVersion());
				model.setGroupId(modelpom.getGroupId());
				model.setArtifactId(modelpom.getArtifactId());
				model.setVersion(modelpom.getVersion());
				model.setName(options.get(AppServerExtConstants.GENERATOR_PROJECT_NAME));

				MavenUtils.writeModel(model, pomFile.getVirtualFile());

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