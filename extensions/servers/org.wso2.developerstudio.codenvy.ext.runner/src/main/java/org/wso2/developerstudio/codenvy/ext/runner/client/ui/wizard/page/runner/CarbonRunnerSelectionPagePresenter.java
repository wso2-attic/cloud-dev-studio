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
package org.wso2.developerstudio.codenvy.ext.runner.client.ui.wizard.page.runner;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.api.project.server.Project;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.api.runner.dto.RunnerDescriptor;
import com.codenvy.api.runner.dto.RunnerEnvironment;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizard;
import com.codenvy.ide.api.wizard.AbstractWizardPage;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.deploy.model.ResourceProvider;
import org.wso2.developerstudio.codenvy.ext.runner.client.i18n.LocalizationConstants;
import org.wso2.developerstudio.codenvy.ext.runner.client.i18n.LocalizationMessages;
import org.wso2.developerstudio.codenvy.ext.runner.shared.CarbonRunnerExtConstants;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Singleton
public class CarbonRunnerSelectionPagePresenter extends AbstractWizardPage
        implements CarbonRunnerSelectionPageView.ActionDelegate {

    private final CarbonRunnerSelectionPageView view;
    private final ProjectServiceClient projectServiceClient;
    private final ResourceProvider resourceProvider;
    private final DtoFactory factory;
    private final LocalizationConstants localizedConstants;
    private final LocalizationMessages localizationMessages;
    private final NotificationManager notificationManager;

    private RunnerDescriptor selectedRunner;
    private RunnerEnvironment selectedEnvironment;

    /**
     * Create wizard page with given caption and image.
     */
    @Inject
    public CarbonRunnerSelectionPagePresenter(CarbonRunnerSelectionPageView view,
                                              ProjectServiceClient projectServiceClient,
                                              ResourceProvider resourceProvider,
                                              DtoFactory factory, LocalizationConstants localizedConstants,
                                              LocalizationMessages localizationMessages,
                                              NotificationManager notificationManager) {

        super(localizedConstants.carbonRunnerSelectionWizardTitle(), null);
        this.view = view;
        this.view.setDelegate(this);

        this.localizedConstants = localizedConstants;
        this.localizationMessages = localizationMessages;
        this.notificationManager = notificationManager;
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
        return true;
    }

    @Override
    public void focusComponent() {

    }

    @Override
    public void removeOptions() {
    }

    @Override
    public void go(AcceptsOneWidget container) {

        container.setWidget(view);
        Project project = wizardContext.getData(ProjectWizard.PROJECT);
        if (project != null) {

        }
    }

    @Override
    public boolean canSkip() {
        return false;
    }

    @Override
    public void commit(@NotNull final CommitCallback callback) {

        if (wizardContext.getData(ProjectWizard.PROJECT) == null) {
            callback.onFailure(new IllegalStateException(localizationMessages.errMsgCannotFindAProject()));
            return;
        }

        if (selectedRunner == null) {
            callback.onSuccess();
            return;
        }
        Project project = wizardContext.getData(ProjectWizard.PROJECT);

        final ProjectDescriptor projectDescriptor = factory.createDto(ProjectDescriptor.class);

        Map<String, List<String>> attributes = project.getAttributes();
        attributes.put(CarbonRunnerExtConstants.RUNNER_NAME, Arrays.asList(selectedRunner.getName()));
        if (selectedEnvironment != null) {
            attributes.put(CarbonRunnerExtConstants.RUNNER_ENV_ID, Arrays.asList(selectedEnvironment.getId()));
        } else {
            attributes.remove(CarbonRunnerExtConstants.RUNNER_ENV_ID);
        }

        projectDescriptor.setAttributes(attributes);
        projectDescriptor.setVisibility(project.getVisibility());
        projectDescriptor.setProjectTypeId(project.getDescription().getProjectTypeId());

        updateProject(project, projectDescriptor, callback);
    }

    private void updateProject(final Project project, ProjectDescriptor projectDescriptor, final CommitCallback callback) {
        projectServiceClient.updateProject(project.getPath(), projectDescriptor, new AsyncRequestCallback<ProjectDescriptor>() {
            @Override
            protected void onSuccess(ProjectDescriptor result) {
                resourceProvider.getProject(project.getName(), new AsyncCallback<Project>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(Project result) {
                        wizardContext.putData(ProjectWizard.PROJECT, result);
                        callback.onSuccess();
                    }
                });
            }

            @Override
            protected void onFailure(Throwable exception) {
                callback.onFailure(exception);
            }
        });
    }

    @Override
    public void onRunnerChanged() {

    }
}
