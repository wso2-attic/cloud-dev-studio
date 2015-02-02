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
package org.wso2.developerstudio.che.ext.esb.client.wizard;

import com.codenvy.api.project.shared.dto.GeneratorDescription;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizard;
import com.codenvy.ide.api.wizard.AbstractWizardPage;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ui.dialogs.DialogFactory;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import org.wso2.developerstudio.che.ext.esb.shared.ESBProjectConstants;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ESBConfigurationPresenter extends AbstractWizardPage implements ESBConfigurationView.ActionDelegate {

    private final ESBConfigurationView view;
    private final DtoFactory dtoFactory;
    private final DialogFactory factory;

    @Inject
    public ESBConfigurationPresenter(ESBConfigurationView view, DtoFactory dtoFactory, com.codenvy.ide.ui.dialogs.DialogFactory factory) {
        super(null, null);
        this.view = view;
        this.dtoFactory = dtoFactory;
        this.factory = factory;
        view.setDelegate(this);
    }

    @Nullable
    @Override
    public String getNotice() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return !(view.getGroupId().isEmpty() ||
                 view.getArtifactId().isEmpty() ||
                 view.getVersion().isEmpty());
    }

    @Override
    public void focusComponent() {

    }

    @Override
    public void storeOptions() {
        Map<String, List<String>> attributes = getAttributes();

        attributes.put(ESBProjectConstants.GROUP_ID, Arrays.asList(view.getGroupId()));
        attributes.put(ESBProjectConstants.ARTIFACT_ID, Arrays.asList(view.getArtifactId()));
        attributes.put(ESBProjectConstants.VERSION, Arrays.asList(view.getVersion()));

        GeneratorDescription generator =
                dtoFactory.createDto(GeneratorDescription.class).withName(ESBProjectConstants.GENERATOR_ID);

        wizardContext.putData(ProjectWizard.GENERATOR, generator);

    }

    private Map<String, List<String>> getAttributes() {
        ProjectDescriptor project = wizardContext.getData(ProjectWizard.PROJECT);

        if (project == null) {
            throw new IllegalStateException("Some problem happened");
        }

        return project.getAttributes();
    }

    @Override
    public void removeOptions() {
        Map<String, List<String>> attributes = getAttributes();
        attributes.remove(ESBProjectConstants.GROUP_ID);
        attributes.remove(ESBProjectConstants.ARTIFACT_ID);
        attributes.remove(ESBProjectConstants.VERSION);

        wizardContext.removeData(ProjectWizard.GENERATOR);
    }

    @Override
    public void go(AcceptsOneWidget container) {
        view.setGroupId("");
        view.setArtifactId("");
        view.setVersion("");

        container.setWidget(view);
    }

    @Override
    public void onGroupIdChanged() {
        delegate.updateControls();
    }

    @Override
    public void onArtifactIdChanged() {
        delegate.updateControls();
    }

    @Override
    public void onVersionChanged() {
        delegate.updateControls();
    }


}
