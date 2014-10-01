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

import com.codenvy.api.project.server.ProjectTypeDescriptionExtension;
import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.shared.AttributeDescription;
import com.codenvy.api.project.shared.ProjectType;
import com.codenvy.ide.extension.maven.shared.MavenAttributes;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.AppServerExtConstants;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class JAXRSProjectTypeDescriptionExtension implements ProjectTypeDescriptionExtension {

    @Inject
    public JAXRSProjectTypeDescriptionExtension(ProjectTypeDescriptionRegistry registry) {
        registry.registerDescription(this);
    }

    @Override
    public List<ProjectType> getProjectTypes() {
        final List<ProjectType> list = new ArrayList<ProjectType>(1);
        list.add(new ProjectType(AppServerExtConstants.WSO2_JAX_RS_PROJECT_ID,
                AppServerExtConstants.WSO2_JAX_RS_PROJECT_NAME,
                AppServerExtConstants.WSO2_APP_SERVER_PROJECT_CATEGORY_ID));
        return list;
    }

    @Override
    public List<AttributeDescription> getAttributeDescriptions() {
        final List<AttributeDescription> list = new ArrayList<AttributeDescription>();
        list.add(new AttributeDescription(AppServerExtConstants.LANGUAGE));
        list.add(new AttributeDescription(AppServerExtConstants.BUILDER_NAME));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_GROUP_ID));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_ARTIFACT_ID));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_VERSION));
        list.add(new AttributeDescription(AppServerExtConstants.LANGUAGE_VERSION));
        list.add(new AttributeDescription(AppServerExtConstants.FRAMEWORK));
        list.add(new AttributeDescription(AppServerExtConstants.BUILDER_MAVEN_SOURCE_FOLDERS));
        list.add(new AttributeDescription(AppServerExtConstants.RUNNER_NAME));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_PACKAGING));
        return list;
    }
}
