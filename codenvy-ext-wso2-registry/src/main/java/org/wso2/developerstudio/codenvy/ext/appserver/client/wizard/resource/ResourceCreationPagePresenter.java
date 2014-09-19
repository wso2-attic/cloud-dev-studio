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
package org.wso2.developerstudio.codenvy.ext.appserver.client.wizard.resource;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.api.ui.wizard.ProjectWizard;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.appserver.client.WSO2RegistryExtensionResources;
import org.wso2.developerstudio.codenvy.ext.appserver.client.i18n.LocalizationConstants;
import org.wso2.developerstudio.codenvy.ext.appserver.client.i18n.LocalizationMessages;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.Constants;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ResourceCreationPagePresenter extends AbstractWizardPage implements ResourceCreationPageView.ActionDelegate {

    private final ResourceCreationPageView view;
    private final ProjectServiceClient projectServiceClient;
    private final ResourceProvider resourceProvider;
    private final DtoFactory factory;
    private final NotificationManager notificationManager;
    private Notification projectCreationStatusNotification;
    private final LocalizationConstants localizedConstants;
    private final LocalizationMessages localizedMessages;
    private final WSO2RegistryExtensionResources resources;

    /**
     * Create wizard page with given caption and image.
     */
    @Inject
    public ResourceCreationPagePresenter(ResourceCreationPageView view,
                                         ProjectServiceClient projectServiceClient,
                                         ResourceProvider resourceProvider,
                                         DtoFactory factory, NotificationManager notificationManager,
                                         LocalizationConstants localizedConstants,
                                         LocalizationMessages localizedMessages,
                                         WSO2RegistryExtensionResources resources) {
        super(localizedConstants.createResourceWizardPageTitle(), null);
        this.view = view;
        this.notificationManager = notificationManager;
        this.localizedConstants = localizedConstants;
        this.localizedMessages = localizedMessages;
        this.resources = resources;
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
    public void removeOptions() {

    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void onTextChange() {
        delegate.updateControls();
    }

    @Override
    public void commit(@NotNull final CommitCallback callback) {

        Map<String, List<String>> options = new HashMap<String, List<String>>();
        options.put(Constants.MAVEN_ARTIFACT_ID, Arrays.asList(wizardContext.getData(Constants.WKEY_MAVEN_ARTIFACT_ID)));
        options.put(Constants.MAVEN_GROUP_ID, Arrays.asList(wizardContext.getData(Constants.WKEY_MAVEN_GROUP_ID)));
        options.put(Constants.MAVEN_VERSION, Arrays.asList(wizardContext.getData(Constants.WKEY_MAVEN_VERSION)));

        final ProjectDescriptor projectDescriptor = factory.createDto(ProjectDescriptor.class);
        projectDescriptor.withProjectTypeId(wizardContext.getData(ProjectWizard.PROJECT_TYPE).getProjectTypeId());
        projectDescriptor.setAttributes(options);

        boolean visibility = wizardContext.getData(ProjectWizard.PROJECT_VISIBILITY);
        projectDescriptor.setVisibility(visibility ? "public" : "private");

        final String name = wizardContext.getData(ProjectWizard.PROJECT_NAME);
        final Project project = wizardContext.getData(ProjectWizard.PROJECT);
        if (project != null) {
            if (project.getName().equals(name)) {
                updateProject(project, projectDescriptor, callback);
            } else {
                projectServiceClient.rename(project.getPath(), name, null, new AsyncRequestCallback<Void>() {
                    @Override
                    protected void onSuccess(Void result) {
                        project.setName(name);

                        updateProject(project, projectDescriptor, callback);
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        callback.onFailure(exception);
                    }
                });
            }

        } else {
            createProject(callback, projectDescriptor, name);
        }


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

    private void createProject(final CommitCallback callback, ProjectDescriptor projectDescriptor, final String name) {
        projectServiceClient
                .createProject(name, projectDescriptor,
                        new AsyncRequestCallback<ProjectDescriptor>() {


                            @Override
                            protected void onSuccess(ProjectDescriptor result) {
                                resourceProvider.getProject(name, new AsyncCallback<Project>() {
                                    @Override
                                    public void onSuccess(Project project) {
                                        callback.onSuccess();
                                        createFiles(project);// Continue after successfully creating the project

                                    }

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        callback.onFailure(caught);
                                    }
                                });
                            }

                            @Override
                            protected void onFailure(Throwable exception) {
                                callback.onFailure(exception);
                            }
                        }
                );
    }

    private void createFiles(final Project project) {

        projectCreationStatusNotification = new Notification(localizedMessages.fileCreationStartedMsg(project.getName()), Notification.Status.PROGRESS);
        notificationManager.showNotification(projectCreationStatusNotification);

        projectServiceClient.createFolder(project.getName() + "/src", new EmptyCallback());
        projectServiceClient.createFolder(project.getName() + "/resources", new EmptyCallback());
        projectServiceClient.createFile(project.getName(), "pom.xml", "testContent", "text/xml", new AsyncRequestCallback<Void>() {
            @Override
            protected void onSuccess(Void result) {
                projectCreationStatusNotification.setMessage(localizedMessages.fileCreationFinishedMsg(project.getName()));
                projectCreationStatusNotification.setStatus(Notification.Status.FINISHED);

                resourceProvider.getActiveProject().refreshChildren(new AsyncCallback<Project>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(Project result) {

                    }
                });
            }

            @Override
            protected void onFailure(Throwable exception) {

            }
        });


    }

    static class EmptyCallback extends AsyncRequestCallback<Void> {
        @Override
        protected void onSuccess(Void result) {

        }

        @Override
        protected void onFailure(Throwable exception) {

        }
    }
}
