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

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.workspaceselector.launcher.ConfigurationContext;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.io.File.separator;
import static org.wso2.developerstudio.server.launcher.ServerConstants.*;

public class ServerBootstrap {
    private static final Logger log = LoggerFactory.getLogger(ServerBootstrap.class);
    public static final String CONTEXT = "/";
	/**
	 * Entry point of embedded Tomcat server.
	 * @param args console arguments
	 */
	public static void main(String args[]) throws IOException, ServletException, LifecycleException {
		/**
		 * default value of port is set to "-1" so that on user operation other depending applications will exit
		 * if port = -1
		 */
        int port = -1;
        String rootDir = System.getenv(STUDIO_ROOT_ENV_VAR_NAME);
        try {
			// wait till a port is available
			while (true) {
				Path portFile = Paths.get(rootDir + PORT_FILE_REL_URL);
				if (Files.exists(portFile)) {
					String portStr = new String(Files.readAllBytes(portFile));
					System.setProperty(SERVER_PORT_SYS_PROPERTY, portStr);
					port = Integer.parseInt(portStr);
					Files.deleteIfExists(portFile);
					break;
				} else {
					Thread.sleep(DEFAULT_SLEEP_TIME);
				}
			}
        } catch (IOException | InterruptedException e) {
            log.error("Error querying available local ports for tomcat.", e);
            System.exit(1);
        }
		/**
		 * default value of pid is set to "-1" so that on user operation other depending applications will exit
		 * if pid = -1
		 */
        int pid = -1;
        if (port > 0) {
            pid = getProcessPid(); //System.getProperty(PID_SYS_PROPERTY);
	        writePIDFile(rootDir, pid);
	        startTomcatServer(rootDir, port);
        } else {
	        writePIDFile(rootDir, pid);
	        log.info("Exiting due to user operation ..");
        }
	}

    private static void writePIDFile(String rootDir, int pid) {
        try {
            Files.write(Paths.get(rootDir + PID_FILE_REL_PATH), Integer.toString(pid).getBytes());
        } catch (IOException e) {
            log.error("Could not write PID to the file", e);
        }
    }

    private static void startTomcatServer(String rootDir, int port)
		    throws IOException, ServletException, LifecycleException {
        log.info("Developer Studio root dir is {}.", rootDir);
        log.info("Starting WSO2 Developer Studio");

        String webRoot = rootDir + RELATIVE_WEB_ROOT;
        log.info("Tomcat web app root is set to : {}", webRoot);

        // Initialize embedded tomcat
        Tomcat tomcatServer = new EmbeddedServer();
        tomcatServer.setPort(port);
        log.info("Tomcat port is set to: {}", port);

        final String ideURL = LOCAL_HOST + port + IDE_CONTEXT_PATH;
        log.info("IDE URL is set to: {}", ideURL);

        try {
            ConfigurationContext.setServerSystemProperties(Integer.toString(port));
            ConfigurationContext.setIDEUrl(ideURL);
            addWebAppsToTomcat(tomcatServer, webRoot);
            log.info("Starting embedded tomcat server.");
            tomcatServer.start();
            tomcatServer.getServer().await();

        } catch (IOException  e ) {
            log.error("Server startup failed : {}", e);
	        throw new IOException();
        } catch ( ServletException e){
	        log.error("Server startup failed : {}", e);
	        throw new ServletException();
	    } catch (LifecycleException e ){
	        log.error("Server startup failed : {}", e);
	        throw new LifecycleException();
        }
    }

    private static int getProcessPid() {
        int processPid = -1;
        String pidStr = ManagementFactory.getRuntimeMXBean().getName();
        if (pidStr.contains("@")) {
            processPid = Integer.parseInt(pidStr.split("@")[0]);
        }
        return processPid;
    }

	/**
	 * Add all web apps available in web root to embedded
	 * tomcat instance for deploying WebApps
	 */
	private static void addWebAppsToTomcat(Tomcat tomcatServer, String webRoot) throws ServletException {
		List<String> webApps = getWebAppList(webRoot);

		if (webApps.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("No web apps are found in web root {}.", webRoot);
			}
			return;
		}
		for (String app : webApps) {
			String path = new File(webRoot + separator + app).getAbsolutePath();
			tomcatServer.addWebapp(CONTEXT + app, path);
			log.info("Adding web app at: {}", path);
		}
	}

	/**
	 * Get a List of web apps in web app root.
	 * @return web app list
	 */
	private static List<String> getWebAppList(String webRoot) {
		File webRootFolder = new File(webRoot);
		String[] webApps = webRootFolder.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		return Arrays.asList(webApps);
	}
}
