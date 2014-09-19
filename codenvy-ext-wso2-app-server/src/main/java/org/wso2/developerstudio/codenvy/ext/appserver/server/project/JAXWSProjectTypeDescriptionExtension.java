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
import org.wso2.developerstudio.codenvy.ext.appserver.shared.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class JAXWSProjectTypeDescriptionExtension implements ProjectTypeDescriptionExtension {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(JAXWSProjectTypeDescriptionExtension.class);

    @Inject
    public JAXWSProjectTypeDescriptionExtension(ProjectTypeDescriptionRegistry registry) {
        registry.registerDescription(this);
    }

    @Override
    public List<ProjectType> getProjectTypes() {
        final List<ProjectType> list = new ArrayList<ProjectType>(1);
        list.add(new ProjectType(Constants.WSO2_JAXWS_PROJECT_ID, Constants.WSO2_JAXWS_PROJECT_NAME, Constants.WSO2_PROJECT_CATEGORY_ID));
        return list;
    }

    @Override
    public List<AttributeDescription> getAttributeDescriptions() {
        final List<AttributeDescription> list = new ArrayList<AttributeDescription>();
        list.add(new AttributeDescription(Constants.LANGUAGE));
        list.add(new AttributeDescription(Constants.BUILDER_NAME));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_GROUP_ID));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_ARTIFACT_ID));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_VERSION));
        list.add(new AttributeDescription(Constants.LANGUAGE_VERSION));
        list.add(new AttributeDescription(Constants.FRAMEWORK));
        list.add(new AttributeDescription(Constants.BUILDER_MAVEN_SOURCE_FOLDERS));
        list.add(new AttributeDescription(Constants.RUNNER_NAME));
        list.add(new AttributeDescription(MavenAttributes.MAVEN_PACKAGING));
        return list;
    }
}
