/**
 *
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.developerstudio.che.ext.esb.server.project.type;

import com.codenvy.api.project.server.Attribute;
import com.codenvy.api.project.server.Builders;
import com.codenvy.api.project.server.ProjectTemplateDescription;
import com.codenvy.api.project.server.ProjectType;
import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.server.ProjectTypeExtension;
import com.codenvy.api.project.server.Runners;
import com.codenvy.api.project.shared.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.wso2.developerstudio.che.ext.esb.shared.ESBProjectConstants;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Singleton
public class ESBProjectTypeExtension implements ProjectTypeExtension {

    private final ProjectType projectType;

    @Inject
    public ESBProjectTypeExtension(ProjectTypeDescriptionRegistry registry) {
        projectType = new ProjectType(ESBProjectConstants.PROJECT_ID, ESBProjectConstants.PROJECT_NAME, ESBProjectConstants.CATEGORY);
        //register our project type
        registry.registerProjectType(this);
    }

    @Override
    public ProjectType getProjectType() {
        return projectType;
    }

    @Nonnull
    @Override
    public List<Attribute> getPredefinedAttributes() {
        return Arrays.asList(new Attribute(Constants.LANGUAGE, "java"));
    }

    @Override
    public Builders getBuilders() {
        return new Builders("maven");
    }

    @Override
    public Runners getRunners() {
        return null;
    }

    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        return null;
    }

    @Override
    public Map<String, String> getIconRegistry() {
        return null;
    }
}
