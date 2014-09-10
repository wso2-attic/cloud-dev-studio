package org.wso2.developerstudio.codenvy.ext.registry.server.project;

import com.codenvy.api.project.server.ProjectTypeDescriptionExtension;
import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.shared.AttributeDescription;
import com.codenvy.api.project.shared.ProjectType;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.registry.shared.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kavith on 7/15/14.
 */
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
