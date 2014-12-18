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
package org.wso2.developerstudio.server.launcher;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Constants;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DevsTomcatServer extends Tomcat {

	private static final Logger logger = LoggerFactory.getLogger(DevsTomcatServer.class);

	@Override
	public Context addWebapp(Host host, String contextPath, String name, String webappFilePath) {
		JarFile webappJarFile = null;
		JarEntry contextXmlFileEntry;
		Context ctx = null;
		boolean removeContext = false;
		try {
			ctx = new StandardContext();
			ctx.setName(name);
			ctx.setPath(contextPath);
			ctx.setDocBase(webappFilePath);
			ctx.addLifecycleListener(new DefaultWebXmlListener());
			ContextConfig ctxCfg = new ContextConfig();
			ctx.addLifecycleListener(ctxCfg);
			String pathToGlobalWebXml = System.getenv(
					org.wso2.developerstudio.server.launcher.Constants.STUDIO_ROOT_ENV_VAR_NAME) +
			                            File.separator +
			                            org.wso2.developerstudio.server.launcher.Constants.WEBAPPS_DIR
			                            + File.separator +
			                            org.wso2.developerstudio.server.launcher.Constants.WEB_XML;
			if (new File(pathToGlobalWebXml).exists()) {
				ctxCfg.setDefaultWebXml(pathToGlobalWebXml);
			} else {
				ctxCfg.setDefaultWebXml(noDefaultWebXmlPath());
			}
			File webappDir = new File(webappFilePath);
			if (webappDir.isDirectory()) {
				File contextxmlfile =
						new File(webappFilePath + File.separator + Constants.ApplicationContextXml);
				if (contextxmlfile.exists()) {
					ctx.setConfigFile(contextxmlfile.toURI().toURL());
				}
			} else {
				// Check for embedded contextXml file in this webapp
				webappJarFile = new JarFile(webappFilePath);
				contextXmlFileEntry = webappJarFile.getJarEntry(Constants.ApplicationContextXml);
				if (contextXmlFileEntry != null) {
					ctx.setConfigFile(new URL("jar:file:" + webappFilePath + "!/" +
					                          Constants.ApplicationContextXml));
				}
			}

			if (ctx instanceof StandardContext) {
				((StandardContext) ctx).setClearReferencesStopTimerThreads(true);
			}
			if (host != null) {
				host.addChild(ctx);
			} else {
				getHost().addChild(ctx);
			}
		} catch (IOException e) {
			removeContext = false;
			logger.error(" Webapp failed to deploy" + e.getMessage(), e);
		} finally {
			if (removeContext && ctx != null && host != null) {
				ctx.setRealm(null);
				try {
					if (!ctx.getState().equals(LifecycleState.STOPPED)) {
						ctx.stop();
					}
				} catch (LifecycleException e) {
					logger.error("Cannot stop context ", e);
				}
				host.removeChild(ctx);
				logger.error("Webapp " + ctx + " failed to deploy");
			}
			if (webappJarFile != null) {
				try {
					webappJarFile.close();
				} catch (Throwable t) {
					ExceptionUtils.handleThrowable(t);
				}
			}
		}
		return ctx;
	}
}



