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

import com.codenvy.api.project.server.AttributeDescription;
import com.codenvy.api.project.server.ProjectType;
import com.codenvy.api.project.server.ProjectTypeDescriptionExtension;
import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.shared.Constants;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.wso2.developerstudio.che.ext.esb.shared.ESBProjectConstants;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;


@Singleton
public class ESBProjectTypeDescriptionExtension implements ProjectTypeDescriptionExtension {

    @Inject
    public ESBProjectTypeDescriptionExtension(ProjectTypeDescriptionRegistry registry) {
        registry.registerDescription(this);
    }

    @Nonnull
    @Override
    public List<ProjectType> getProjectTypes() {
        return Arrays.asList(new ProjectType(ESBProjectConstants.PROJECT_ID, ESBProjectConstants.PROJECT_NAME, ESBProjectConstants.CATEGORY));
    }

    @Nonnull
    @Override
    public List<AttributeDescription> getAttributeDescriptions() {
        return Arrays.asList(new AttributeDescription(Constants.LANGUAGE));
    }
}
