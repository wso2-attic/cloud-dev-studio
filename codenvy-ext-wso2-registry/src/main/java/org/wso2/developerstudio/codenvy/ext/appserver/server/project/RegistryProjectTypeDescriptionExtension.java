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
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.Constants;

import java.util.Arrays;
import java.util.List;

@Singleton
public class RegistryProjectTypeDescriptionExtension implements ProjectTypeDescriptionExtension {

    @Inject
    public RegistryProjectTypeDescriptionExtension(ProjectTypeDescriptionRegistry registry) {
        registry.registerDescription(this);
    }

    @Override
    public List<ProjectType> getProjectTypes() {
        return Arrays.asList(new ProjectType(Constants.WSO2_REGISTRY_PROJECT_ID, Constants.WSO2_REGISTRY_PROJECT, Constants.WSO2_PROJECT_ID));
    }

    @Override
    public List<AttributeDescription> getAttributeDescriptions() {
        return Arrays.asList(new AttributeDescription(Constants.LANGUAGE),
                new AttributeDescription(Constants.FRAMEWORK));
    }
}
