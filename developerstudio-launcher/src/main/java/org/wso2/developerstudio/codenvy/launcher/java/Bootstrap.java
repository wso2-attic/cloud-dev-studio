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
package org.wso2.developerstudio.codenvy.launcher.java;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.embedded.browser.SampleBrowserSWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private static final String DEFAULT_WEB_ROOT = "./../tomcat/webroot";

    protected static Map<String, String> mapContextToWebApp;
    protected static String webAppRoot;

    static {
        mapContextToWebApp = new HashMap<>();
        mapContextToWebApp.put("/api", "api");
        mapContextToWebApp.put("/datasource", "datasource");
        mapContextToWebApp.put("/java-ca", "java-ca");
        mapContextToWebApp.put("/ide", "ide");
    }

    public static void main(String args[]) {

        logger.info("Starting WSO2 Developer Studio 4.0.0");
        logger.info("Initializing embedded tomcat server.");

        webAppRoot = args[0];

        if (webAppRoot.equals("")) {
            webAppRoot = DEFAULT_WEB_ROOT;
        }

        logger.info("Tomcat web app root is set to : " + webAppRoot);

        try {
            Tomcat tomcat = new Tomcat();
            Integer port = getAvailablePort();
            tomcat.setPort(port);
            logger.info("Tomcat port is set to: " + port);

            addWebApps(tomcat);

            TomcatLauncher launcher = new TomcatLauncher(tomcat);
            launcher.launch();

            SampleBrowserSWT.load();
        } catch (IOException e) {
            logger.error("Error querying available local ports for tomcat", e);
        } catch (ServletException e) {
            logger.error("Error adding Web apps", e);
        } catch (LifecycleException e) {
            logger.error("Error Starting tomcat", e);
        }
    }

    private static void addWebApps(Tomcat tomcat) throws ServletException {

        Iterator it = mapContextToWebApp.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry webAppEntry = (Map.Entry) it.next();
            tomcat.addWebapp(webAppEntry.getKey().toString(),
                    new File(webAppRoot + File.separator + webAppEntry.getValue().toString()).getAbsolutePath());

            logger.info("Adding web app : " + new File(webAppRoot + File.separator + webAppEntry.getValue().toString()).getAbsolutePath());
        }
    }

    private static Integer getAvailablePort() throws IOException {

        ServerSocket socket = new ServerSocket(0);
        Integer port = socket.getLocalPort();
        socket.close();
        return port;

    }
}
