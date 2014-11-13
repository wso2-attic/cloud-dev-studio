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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.AppServerExtConstants;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Project generator for wso2 app server web apps
 */
@Singleton
public class WSO2AppServerProjectGenerator implements ProjectGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(WSO2AppServerProjectGenerator.class);
	public static final String MANIFEST_CONTENT = "Manifest-Version: 1.0\n" + "Class-Path: \n";
	private final String WEB_INF_FOLDER = "WEB-INF";
	private final String META_INF_FOLDER = "META-INF";
	private final String MAIN_SRC_FOLDER = "src" + File.separator + "main";
	private final String RESOURCE_FOLDER = "resources";
	private final String WEB_APP_FOLDER = "webapp";
	private final String JAVA_FOLDER = "java";
	private final String JAVA_FILE_EXTENSION = ".java";

	private final String WEB_APP_WEB_TEMPLATE = "webapp_web_template.xml";
	private final String WEB_APP_POM_TEMPLATE = "webapp_pom_template.xml";
	private final String JAXWS_JAVA_CLASS_TEMPLATE = "jaxws_java_template.xml";
	private final String JAXRS_JAVA_CLASS_TEMPLATE = "jaxrs_java_template.xml";
	private final String WEBAPP_CLASS_LOADING_TEMPLATE = "webapp_class_loading_template.xml";
	private final String JAXWS_JAXRS_WEB_TEMPLATE = "jaxrs_jaxws_web_template.xml";
	private final String JAXWS_CXF_SERVLET_TEMPLATE = "jaxws_cxf_servlet_template.xml";
	private final String JAXWS_POM_TEMPLATE = "jaxws_pom_template.xml";
	private final String JAXRS_CXF_SERVLET_TEMPLATE = "jaxrs_cxf_servlet_template.xml";
	private final String JAXRS_POM_TEMPLATE = "jaxrs_pom_template.xml";

	private final String WEB_XML = "web.xml";
	private final String MANIFEST_FILE = "manifest.xml";
	private final String POM_XML = "pom.xml";
	private final String WEBAPP_CLASSLOADING_XML = "webapp-classloading.xml";
	private final String CXF_SERVLET_XML = "cxf-servlet.xml";

	private final String MAVEN_WAR_PLUGIN_NODE = "maven-war-plugin";
	private final String CONFIGURATION_NODE = "configuration";
	private final String WEB_APP_DIRECTORY_NODE = "webappDirectory";
	private final String WEB_XML_NODE = "webXml";

	private final String KEYWORD_PACKAGE_NAME = "packageName";
	private final String KEYWORD_WEB_SERVICE_NAME = "webserviceName";
	private final String KEYWORD_CLASS_NAME = "className";
	private final String SERVER_ID = "ServerID";
	private final String SERVER_ADDRESS = "serveraddress";
	private final String SERVICE_CLASS = "serviceclass";
	private final String BEAN_ID = "beanID";
	private final String BEAN_CLASS = "beanclass";

	private InputStream webAppTemplateStream;
	private BufferedReader bufferedReader;
	private Model newPomModel;

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

		//read the constants from new constants file
		if (options.get(AppServerExtConstants.GENERATOR_PROJECT_TYPE).equals(AppServerExtConstants.WSO2_WEB_APP_PROJECT_ID)) {
			webAppProjectCreation(folderEntry, options);

		} else if (options.get(AppServerExtConstants.GENERATOR_PROJECT_TYPE).equals(AppServerExtConstants.WSO2_JAX_WS_PROJECT_ID)) {
			jaxWSProjectCreation(folderEntry, options);

		} else if (options.get(AppServerExtConstants.GENERATOR_PROJECT_TYPE).equals(AppServerExtConstants.WSO2_JAX_RS_PROJECT_ID)) {
			jaxRSProjectCreation(folderEntry, options);
		} else {
			LOG.error("Error generating project " + folderEntry.getName() + ".\nError : Project type is not " + "supported by " + getId() + ".");
		}

	}

	public void webAppProjectCreation(FolderEntry folderEntry, Map<String, String> options) {

		String webContentFolder = options.get(AppServerExtConstants.GENERATOR_WEB_CONTENT_FOLDER);
		try {
			folderEntry.createFolder(webContentFolder);
			folderEntry.createFolder(webContentFolder + File.separator + WEB_INF_FOLDER);
		    webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(WEB_APP_WEB_TEMPLATE);
			String webINFContent;
			InputStream input = null;
			try {
				if (webAppTemplateStream != null) {
					webINFContent = IOUtils.toString(webAppTemplateStream);
					folderEntry.createFile(webContentFolder + File.separator + WEB_INF_FOLDER + WEB_XML, webINFContent.getBytes(), MimeType.TEXT_XML);
					folderEntry.createFolder(webContentFolder + File.separator + META_INF_FOLDER);
					String metaINFContent = MANIFEST_CONTENT;
					folderEntry.createFile(
							webContentFolder + File.separator + META_INF_FOLDER + File.separator +
							MANIFEST_FILE, metaINFContent.getBytes(), MimeType.TEXT_XML
					);
					webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(WEB_APP_POM_TEMPLATE);
					if (webAppTemplateStream != null) {
						bufferedReader = new BufferedReader(new InputStreamReader(webAppTemplateStream));
						newPomModel = MavenUtils.readModel(bufferedReader);
						VirtualFileEntry pomFile = folderEntry.getChild(POM_XML);
						input = ((FileEntry) pomFile).getInputStream();
						Model defaultCreatedPom =  MavenUtils.readModel(input);
						if (defaultCreatedPom != null) {
							Model createdModel = pomCreationWebAppProject(defaultCreatedPom, newPomModel, webContentFolder);
							MavenUtils.writeModel(createdModel, pomFile.getVirtualFile());
						}
					}
				}
			} catch (IOException e) {
				LOG.error("Error reading web/xml template", e);

			} finally {
				try {
					if (webAppTemplateStream != null) {
						webAppTemplateStream.close();
					}
					if (bufferedReader != null) {
						bufferedReader.close();
					}
					if (input != null) {
						input.close();
					}
				} catch (IOException e) {
					LOG.error("Error in closing the Stream", e);
				}
			}

		} catch (ConflictException e) {
			LOG.error("Conflict exception in file entry for app server project creation", e);
		} catch (ServerException e) {
			LOG.error("Server exception in file entry for app server project creation", e);
		} catch (ForbiddenException e) {
			LOG.error("Forbidden exception in file entry for app server project creation", e);
		}
	}

	public Model pomCreationWebAppProject(Model modelpom, Model model, String webContentFolderLoc) {
		model.setModelVersion(modelpom.getModelVersion());
		model.setGroupId(modelpom.getGroupId());
		model.setArtifactId(modelpom.getArtifactId());
		model.setVersion(modelpom.getVersion());
		model.setName(options.get(AppServerExtConstants.GENERATOR_PROJECT_NAME));

		List<Plugin> pluginList = model.getBuild().getPlugins();
		for (Plugin element : pluginList) {
			if (element.getArtifactId().equals(MAVEN_WAR_PLUGIN_NODE)) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
				Xpp3Dom configurationDom = null;

				try {
					builder = factory.newDocumentBuilder();
					Document document = builder.parse(new InputSource(new StringReader(element.getConfiguration().toString())));
					NodeList nodeList = document.getFirstChild().getChildNodes();
					configurationDom = new Xpp3Dom(CONFIGURATION_NODE);

					for (Node nodeListItem: nodeList ) {
						if (nodeListItem.getNodeType() != Node.TEXT_NODE) {
							Xpp3Dom nodeDom = new Xpp3Dom(nodeList.item(j).getNodeName());
							if (nodeListItem.getNodeName().equals(WEB_APP_DIRECTORY_NODE)) {
								nodeDom.setValue(webContentFolderLoc);
							} else if (nodeListItem.getNodeName().equals(WEB_XML_NODE)) {
								nodeDom.setValue(
										webContentFolderLoc + File.separator +
										WEB_INF_FOLDER + File.separator +
										WEB_XML
								);
							} else {
								nodeDom.setValue(nodeListItem.getTextContent());
							}
							configurationDom.addChild(nodeDom);
						}
					}
				} catch (Exception e) {
					LOG.error("Exception was thrown in building the pom for Web Application project creatio", e);
				}
				element.setConfiguration(configurationDom);
			}
		}
		return model;

	}

	public void jaxWSProjectCreation(FolderEntry folderEntry, Map<String, String> options) {
		String javaClassContent;
		String classLoadingContent;
		String webXmlContent;
		String servletAddressElement;
		String cxfServletContent;
		InputStream input;
		Model modelPom;		try {
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + RESOURCE_FOLDER);
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER);
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator + WEB_INF_FOLDER);
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator + META_INF_FOLDER);
			String packageName = (options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME)).replace(".", File.separator);
			String className = options.get(AppServerExtConstants.GENERATOR_CLASS_NAME);
			webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXWS_JAVA_CLASS_TEMPLATE);
			if (webAppTemplateStream != null) {
				try {
					javaClassContent = IOUtils.toString(webAppTemplateStream);
					folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + JAVA_FOLDER + packageName);
					javaClassContent = javaClassContent.replace(KEYWORD_PACKAGE_NAME, options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME));
					javaClassContent = javaClassContent.replace(KEYWORD_WEB_SERVICE_NAME, className);
					javaClassContent = javaClassContent.replace(KEYWORD_CLASS_NAME, className);
					folderEntry.createFile(
							MAIN_SRC_FOLDER + File.separator + JAVA_FOLDER + packageName +
							File.separator + className + JAVA_FILE_EXTENSION,
							javaClassContent.getBytes(),
							MimeType.APPLICATION_JAVA
					);
					webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(WEBAPP_CLASS_LOADING_TEMPLATE);
					if (webAppTemplateStream != null) {
						classLoadingContent = IOUtils.toString(webAppTemplateStream);
						folderEntry.createFile(
								MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator +
								META_INF_FOLDER + File.separator + WEBAPP_CLASSLOADING_XML,
								classLoadingContent.getBytes(),
								MimeType.TEXT_XML
						);
						webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXWS_JAXRS_WEB_TEMPLATE);
						if (webAppTemplateStream != null) {
							webXmlContent = IOUtils.toString(webAppTemplateStream);
							folderEntry.createFile(
									MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER +
									File.separator + WEB_INF_FOLDER + File.separator + WEB_XML,
									webXmlContent.getBytes(),
									MimeType.TEXT_XML
							);
							if (className != null) {
								webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXWS_CXF_SERVLET_TEMPLATE);
								if (webAppTemplateStream != null) {
									cxfServletContent = IOUtils.toString(webAppTemplateStream);
									cxfServletContent = cxfServletContent.replace(SERVER_ID, className);
									cxfServletContent = cxfServletContent.replace(SERVER_ADDRESS, creatingServletAddressElem(className));
									cxfServletContent = cxfServletContent.replace(SERVICE_CLASS, className);
									folderEntry.createFile(MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator +
									                       WEB_INF_FOLDER + File.separator + CXF_SERVLET_XML,
									                       cxfServletContent.getBytes(), MimeType.TEXT_XML
									);
									webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXWS_POM_TEMPLATE);
									if (webAppTemplateStream != null) {
										bufferedReader = new BufferedReader(new InputStreamReader(
												webAppTemplateStream));
										newPomModel = MavenUtils.readModel(bufferedReader);
										VirtualFileEntry pomFile = folderEntry.getChild(POM_XML);
										input = ((FileEntry) pomFile).getInputStream();
										modelPom = MavenUtils.readModel(input);
										if (modelPom != null) {
											newPomModel.setModelVersion(modelPom.getModelVersion());
											newPomModel.setGroupId(modelPom.getGroupId());
											newPomModel.setArtifactId(modelPom.getArtifactId());
											newPomModel.setVersion(modelPom.getVersion());
											newPomModel.setName(options.get(
													AppServerExtConstants.GENERATOR_PROJECT_NAME));
											MavenUtils.writeModel(newPomModel, pomFile.getVirtualFile());
										}
									}
								}
							}
						}
					}
				} catch (IOException e) {
					LOG.error("Error in reading in JaxWS Java class Template", e);
				}
			}
		} catch (ConflictException e) {
			LOG.error("Conflict exception in file entry for app server project creation", e);
		} catch (ServerException e) {
			LOG.error("Server exception in file entry for app server project creation", e);
		} catch (ForbiddenException e) {
			LOG.error("Forbidden exception in file entry for app server project creation", e);
		}
	}

	public void jaxRSProjectCreation(FolderEntry folderEntry, Map<String, String> options) {
		String javaClassContent;
		String classLoadingContent;
		String webXmlContent;
		String cxfServletContent ;
		InputStream inputStream;
		Model modelPom;
		try {
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + RESOURCE_FOLDER);
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER);
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator + WEB_INF_FOLDER);
			folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator + META_INF_FOLDER);
			String packageName = (options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME)).replace(".", File.separator);
			String className = options.get(AppServerExtConstants.GENERATOR_CLASS_NAME);
			webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXRS_JAVA_CLASS_TEMPLATE);

			if (webAppTemplateStream != null) {
				try {
					javaClassContent = IOUtils.toString(webAppTemplateStream);
					javaClassContent = javaClassContent.replace(KEYWORD_PACKAGE_NAME, options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME));
					javaClassContent = javaClassContent.replace(KEYWORD_CLASS_NAME, className);
					folderEntry.createFolder(MAIN_SRC_FOLDER + File.separator + JAVA_FOLDER + File.separator + packageName);
					folderEntry.createFile(
							MAIN_SRC_FOLDER + File.separator + JAVA_FOLDER + File.separator + packageName + File.separator + className + JAVA_FILE_EXTENSION,
							javaClassContent.getBytes(), MimeType.APPLICATION_JAVA
					);
					webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(WEBAPP_CLASS_LOADING_TEMPLATE);
					if (webAppTemplateStream != null) {
						classLoadingContent = IOUtils.toString(webAppTemplateStream);
						folderEntry.createFile(
								MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator + META_INF_FOLDER + File.separator + WEBAPP_CLASSLOADING_XML,
								classLoadingContent.getBytes(), MimeType.TEXT_XML
						);
						webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXWS_JAXRS_WEB_TEMPLATE);
						if (webAppTemplateStream != null) {
							webXmlContent = IOUtils.toString(webAppTemplateStream);
							folderEntry.createFile(
									MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator + WEB_INF_FOLDER + File.separator + WEB_XML,
									webXmlContent.getBytes(), MimeType.TEXT_XML
							);

							webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXRS_CXF_SERVLET_TEMPLATE);

							if (webAppTemplateStream != null && className != null) {
								cxfServletContent = IOUtils.toString(webAppTemplateStream);
								cxfServletContent = cxfServletContent.replace(BEAN_ID, className + "BEAN");
								cxfServletContent = cxfServletContent.replace(BEAN_CLASS, options.get(AppServerExtConstants.GENERATOR_PACKAGE_NAME) + "." + className);
								cxfServletContent = cxfServletContent.replace(SERVER_ID, className);
								cxfServletContent = cxfServletContent.replace(SERVER_ADDRESS, creatingServletAddressElem(className));
								cxfServletContent = cxfServletContent.replace(SERVICE_CLASS, className + "BEAN");
								folderEntry.createFile(
										MAIN_SRC_FOLDER + File.separator + WEB_APP_FOLDER + File.separator + WEB_INF_FOLDER + File.separator + CXF_SERVLET_XML,
										cxfServletContent.getBytes(), MimeType.TEXT_XML
								);
								webAppTemplateStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(JAXRS_POM_TEMPLATE);
								if (webAppTemplateStream != null) {
									bufferedReader = new BufferedReader(new InputStreamReader(webAppTemplateStream));
									newPomModel = MavenUtils.readModel(bufferedReader);
									VirtualFileEntry pomFile = folderEntry.getChild(POM_XML);
									inputStream = ((FileEntry) pomFile).getInputStream();
									modelPom = MavenUtils.readModel(inputStream);
									if (modelPom != null) {
										newPomModel.setModelVersion(modelPom.getModelVersion());
										newPomModel.setGroupId(modelPom.getGroupId());
										newPomModel.setArtifactId(modelPom.getArtifactId());
										newPomModel.setVersion(modelPom.getVersion());
										newPomModel.setName(options.get(AppServerExtConstants.GENERATOR_PROJECT_NAME));
										MavenUtils.writeModel(newPomModel, pomFile.getVirtualFile());
									}
								}
							}
						}
					}
				} catch (IOException e) {
					LOG.error("Error in class-loading.xml", e);
				}
			}
		} catch (ConflictException e) {
			LOG.error("Conflict exception in file entry for app server project creation", e);
		} catch (ServerException e) {
			LOG.error("Server exception in file entry for app server project creation", e);
		} catch (ForbiddenException e) {
			LOG.error("Forbidden exception in file entry for app server project creation", e);
		}
	}

	public String creatingServletAddressElem( String className){
		String servletAddressElement;
		if (className != null) {
			servletAddressElement = className.replaceAll("([A-Z])", "_$1"); // split CamelCase
			servletAddressElement = servletAddressElement.replaceAll("^/_", "/");
			return  servletAddressElement.toLowerCase();
		}

	}


}