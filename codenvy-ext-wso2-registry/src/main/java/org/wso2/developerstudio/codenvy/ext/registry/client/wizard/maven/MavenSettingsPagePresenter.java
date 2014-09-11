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
package org.wso2.developerstudio.codenvy.ext.registry.client.wizard.maven;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.api.ui.wizard.ProjectWizard;
import com.codenvy.ide.dto.DtoFactory;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.registry.client.i18n.LocalizationConstants;
import org.wso2.developerstudio.codenvy.ext.registry.shared.Constants;

import javax.annotation.Nullable;

@Singleton
public class MavenSettingsPagePresenter extends AbstractWizardPage implements MavenSettingsPageView.ActionDelegate {

    private final MavenSettingsPageView         view;
    private final ProjectServiceClient projectServiceClient;
    private final ResourceProvider     resourceProvider;
    private final DtoFactory           factory;
    private final LocalizationConstants localizedConstants;

    /**
     * Create wizard page with given caption and image.
     *
     */
    @Inject
    public MavenSettingsPagePresenter(MavenSettingsPageView view,
                                      ProjectServiceClient projectServiceClient,
                                      ResourceProvider resourceProvider,
                                      DtoFactory factory, LocalizationConstants localizedConstants) {
        super(localizedConstants.mavenWizardPageTitle(), null);

        this.view = view;
        this.localizedConstants = localizedConstants;
        // Important step, set the action delegate of view
        this.view.setDelegate(this);
        this.projectServiceClient = projectServiceClient;
        this.resourceProvider = resourceProvider;
        this.factory = factory;

    }

    @Nullable
    @Override
    public String getNotice() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return !view.getArtifactId().equals("") && !view.getGroupId().equals("") && !view.getVersion().equals("");
    }

    @Override
    public void focusComponent() {

    }

    @Override
    public void storeOptions() {
        wizardContext.putData(Constants.WKEY_MAVEN_ARTIFACT_ID, view.getArtifactId());
        wizardContext.putData(Constants.WKEY_MAVEN_GROUP_ID, view.getGroupId());
        wizardContext.putData(Constants.WKEY_MAVEN_VERSION, view.getVersion());
    }

    @Override
    public void removeOptions() {
        wizardContext.removeData(Constants.WKEY_MAVEN_ARTIFACT_ID);
        wizardContext.removeData(Constants.WKEY_MAVEN_VERSION);
        wizardContext.removeData(Constants.WKEY_MAVEN_GROUP_ID);
    }

    @Override
    public void go(AcceptsOneWidget container) {
        // Important - Set view as the widget
        container.setWidget(view);
        Project project = wizardContext.getData(ProjectWizard.PROJECT);
        if (project != null) {
            view.setArtifactId(project.getAttributeValue(Constants.MAVEN_ARTIFACT_ID));
            view.setGroupId(project.getAttributeValue(Constants.MAVEN_GROUP_ID));
            view.setVersion(project.getAttributeValue(Constants.MAVEN_VERSION));
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    onTextChange();
                }
            });
        }
    }

    @Override
    public boolean canSkip() {
        return false;
    }

    @Override
    public void onTextChange() {
        delegate.updateControls();
    }
}
