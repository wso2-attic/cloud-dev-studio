/**
 * <!--
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
 * -->
 */
package org.wso2.developerstudio.che.ext.esb.server.genarator;

import com.codenvy.api.core.ConflictException;
import com.codenvy.api.core.ForbiddenException;
import com.codenvy.api.core.ServerException;
import com.codenvy.api.project.server.FileEntry;
import com.codenvy.api.project.server.FolderEntry;
import com.codenvy.api.project.server.ProjectGenerator;
import com.codenvy.api.project.shared.dto.NewProject;
import com.codenvy.ide.maven.tools.Model;

import org.wso2.developerstudio.che.ext.esb.shared.ESBProjectConstants;


public class ESBProjectGenerator implements ProjectGenerator {

    public static final String TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    @Override
    public String getId() {
        return ESBProjectConstants.GENERATOR_ID;
    }

    @Override
    public String getProjectTypeId() {
        return ESBProjectConstants.PROJECT_ID;
    }

    @Override
    public void generateProject(FolderEntry baseFolder, NewProject newProjectDescriptor)
            throws ForbiddenException, ConflictException, ServerException {
        Model model = Model.createModel();
        model.setModelVersion("4.0.0");
        model.setGroupId(newProjectDescriptor.getAttributes().get(ESBProjectConstants.GROUP_ID).get(0));
        model.setArtifactId(newProjectDescriptor.getAttributes().get(ESBProjectConstants.ARTIFACT_ID).get(0));
        model.setVersion(newProjectDescriptor.getAttributes().get(ESBProjectConstants.VERSION).get(0));
        FileEntry pomFile = baseFolder.createFile("pom.xml", (byte[])null, "text/xml");

        model.writeTo(pomFile.getVirtualFile());


        FolderEntry dbsFolder = baseFolder.createFolder("SynapseConfigs");
        dbsFolder.createFile(baseFolder.getName() + ".esb.proxy", TAG.getBytes(), "text/xml+wso2-esb");
    }
}
