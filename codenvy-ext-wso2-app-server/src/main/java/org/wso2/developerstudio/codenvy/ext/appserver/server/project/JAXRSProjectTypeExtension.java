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

import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.server.ProjectTypeExtension;
import com.codenvy.api.project.shared.Attribute;
import com.codenvy.api.project.shared.ProjectTemplateDescription;
import com.codenvy.api.project.shared.ProjectType;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.AppServerExtConstants;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class JAXRSProjectTypeExtension implements ProjectTypeExtension {

    private final ProjectType projectType;

    @Inject
    public JAXRSProjectTypeExtension(ProjectTypeDescriptionRegistry registry) {
        this.projectType = new ProjectType(AppServerExtConstants.WSO2_JAX_RS_PROJECT_ID,
                AppServerExtConstants.WSO2_JAX_RS_PROJECT_NAME,
                AppServerExtConstants.WSO2_APP_SERVER_PROJECT_CATEGORY_ID);
        registry.registerProjectType(this);
    }

    @Override
    public ProjectType getProjectType() {
        return projectType;
    }

    @Override
    public List<Attribute> getPredefinedAttributes() {
        final List<Attribute> list = new ArrayList<Attribute>(2);
        list.add(new Attribute(AppServerExtConstants.LANGUAGE, AppServerExtConstants.JAVA_LANGUAGE));
        list.add(new Attribute(AppServerExtConstants.BUILDER_NAME, AppServerExtConstants.MAVEN_BUILDER));
        list.add(new Attribute(AppServerExtConstants.RUNNER_NAME, AppServerExtConstants.WSO2_APP_SERVER_RUNNER_NAME));
        return list;
    }

    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        final List<ProjectTemplateDescription> list = new ArrayList<ProjectTemplateDescription>();
        return list;
    }
}
