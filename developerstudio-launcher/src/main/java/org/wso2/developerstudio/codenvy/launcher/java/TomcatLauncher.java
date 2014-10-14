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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatLauncher {

    private static final Logger logger = LoggerFactory.getLogger(TomcatLauncher.class);

    protected Tomcat tomcatInstance;

    public TomcatLauncher(Tomcat tomcatInstance) {
        this.tomcatInstance = tomcatInstance;
    }

    public void launch() throws LifecycleException {

        tomcatInstance.start();
        tomcatInstance.getServer().await();

    }

    public Tomcat getTomcatInstance() {
        return tomcatInstance;
    }

}
