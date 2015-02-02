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
package org.wso2.developerstudio.che.ext.esb.client;

import com.codenvy.ide.api.editor.EditorRegistry;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.filetypes.FileType;
import com.codenvy.ide.api.filetypes.FileTypeRegistry;
import com.codenvy.ide.api.projecttype.wizard.ProjectTypeWizardRegistry;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizard;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import org.wso2.developerstudio.che.ext.esb.client.editor.multipage.ESBMultiPageEditorProvider;
import org.wso2.developerstudio.che.ext.esb.shared.ESBProjectConstants;
import org.wso2.developerstudio.che.ext.esb.client.wizard.ESBConfigurationPresenter;

@Singleton
@Extension(title = "WSO2 ESB extension", description = "WSO2 ESB extension", version = "1.0.0")
public class ESBExtension {

    @Inject
    public ESBExtension(ProjectWizard projectWizard,
                        Provider<ESBConfigurationPresenter> wizardPage,
                        ProjectTypeWizardRegistry registry,
                        FileTypeRegistry fileTypeRegistry,
                        @Named("wso2ESBFile") FileType dssFileType,
                        ESBMultiPageEditorProvider editorProvider,
                        EditorRegistry editorRegistry) {

        projectWizard.addPage(wizardPage);

        registry.addWizard(ESBProjectConstants.PROJECT_ID, projectWizard);

        fileTypeRegistry.registerFileType(dssFileType);
        editorRegistry.registerDefaultEditor(dssFileType, editorProvider);
    }

}
