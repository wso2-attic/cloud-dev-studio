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
package org.wso2.developerstudio.server.launcher;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.io.File.separator;
import static org.apache.catalina.startup.Constants.ApplicationContextXml;
import static org.wso2.developerstudio.server.launcher.ServerConstants.*;

/**
 * Extended tomcat server for developer studio
 */
public class EmbeddedServer extends Tomcat {

	private static final Logger log = LoggerFactory.getLogger(EmbeddedServer.class);

	public static final String JAR_FILE_PREFIX = "jar:file:";
	public static final String JAR_ROOT_PREFIX = "!/";
	public static final String LIB_DIRECTORY = "WEB-INF" + separator + "lib";
	public static final String FILE_PROTOCOL = "file:";

	@Override
	public Context addWebapp(Host host, String contextPath, String name, String webAppDirectory) {
		JarFile webAppJarFile = null;
		JarEntry contextXMLFileEntry;
		Context ctx = null;

		boolean removeContext = false;
		try {
			ctx = new StandardContext();
			ctx.setName(name);
			ctx.setPath(contextPath);
			ctx.setDocBase(webAppDirectory);
			ctx.addLifecycleListener(new DefaultWebXmlListener());

			ContextConfig ctxCfg = new ContextConfig();
			ctx.addLifecycleListener(ctxCfg);

			URLClassLoader urlClassLoader = new URLClassLoader(
					new URL[] { new URL(FILE_PROTOCOL + webAppDirectory + LIB_DIRECTORY) },
					ClassLoader.getSystemClassLoader());
			ctx.setParentClassLoader(urlClassLoader);

			String pathToGlobalWebXml = System.getenv(STUDIO_ROOT_ENV_VAR_NAME) +
			                            separator + WEB_APPS_DIR + separator + WEB_XML_FILE;
			if (new File(pathToGlobalWebXml).exists()) {
				ctxCfg.setDefaultWebXml(pathToGlobalWebXml);
			} else {
				ctxCfg.setDefaultWebXml(noDefaultWebXmlPath());
			}

			File webAppDir = new File(webAppDirectory);
			if (webAppDir.isDirectory()) {
				File contextXMLFile =
						new File(webAppDirectory + separator + ApplicationContextXml);
				if (contextXMLFile.exists()) {
					ctx.setConfigFile(contextXMLFile.toURI().toURL());
				}
			} else {
				// Check for embedded contextXml file in this web-app
				webAppJarFile = new JarFile(webAppDirectory);
				contextXMLFileEntry = webAppJarFile.getJarEntry(ApplicationContextXml);
				if (contextXMLFileEntry != null) {
					ctx.setConfigFile(new URL(JAR_FILE_PREFIX + webAppDirectory + JAR_ROOT_PREFIX +
					                          ApplicationContextXml));
				}
			}

			StandardContext standardCtx = (StandardContext) ctx;
			standardCtx.setClearReferencesStopTimerThreads(true);

			if (host != null) {
				host.addChild(ctx);
			} else {
				getHost().addChild(ctx);
			}
		} catch (IOException e) {
			removeContext = false;
			log.error(" Web-app {} deploying failed. Error : {}", name, e.getMessage(), e);
		} finally {
			if (removeContext && ctx != null && host != null) {
				ctx.setRealm(null);
				try {
					if (!ctx.getState().equals(LifecycleState.STOPPED)) {
						ctx.stop();
					}
				} catch (LifecycleException e) {
					log.error("Context cannot be stopped.", e);
				}
				host.removeChild(ctx);
				log.error("Web-app {} deploying failed.", ctx);
			}
			if (webAppJarFile != null) {
				try {
					webAppJarFile.close();
				} catch (Throwable t) {
					ExceptionUtils.handleThrowable(t);
				}
			}
		}
		return ctx;
	}
}



