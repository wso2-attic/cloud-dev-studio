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
package org.wso2.developerstudio.codenvy.ext.appserver.client.ui.wizard.page.maven;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.api.ui.wizard.ProjectWizard;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.Jso;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.extension.maven.client.wizard.MavenPomReaderClient;
import com.codenvy.ide.extension.maven.shared.MavenAttributes;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.appserver.client.i18n.LocalizationConstants;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.AppServerExtConstants;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MavenConfigurationPagePresenter extends AbstractWizardPage implements MavenConfigurationPageView.ActionDelegate {

    private final MavenConfigurationPageView view;
    private final ProjectServiceClient projectServiceClient;
    private final ResourceProvider resourceProvider;
    private final DtoFactory factory;
    private final LocalizationConstants localizedConstants;
    private final PreferencesManager preferencesManager;
    private final MavenPomReaderClient pomReaderClient;
    private final StringMap<String> projectGeneratorOptions;

    @Inject
    public MavenConfigurationPagePresenter(MavenConfigurationPageView view,
                                           ProjectServiceClient projectServiceClient,
                                           ResourceProvider resourceProvider,
                                           DtoFactory factory, LocalizationConstants localizedConstants,
                                           PreferencesManager preferencesManager, MavenPomReaderClient pomReaderClient) {
        super(localizedConstants.mavenWizardPageTitle(), null);

        this.view = view;
        this.localizedConstants = localizedConstants;
        this.preferencesManager = preferencesManager;
        this.pomReaderClient = pomReaderClient;
        this.view.setDelegate(this);
        this.projectServiceClient = projectServiceClient;
        this.resourceProvider = resourceProvider;
        this.factory = factory;
        this.projectGeneratorOptions = Collections.createStringMap();
	    this.view.setGroupId("com.example");
	    this.view.setGroupId("sample");
	    this.view.setVersion("1.0.0-SNAPSHOT");
    }

    @Nullable
    @Override
    public String getNotice() {
        return null;
    }

	@Override
	public boolean isCompleted() {

		boolean isCompleted = !view.getArtifactId().equals("") && !view.getGroupId().equals("") &&
		                      !view.getVersion().equals("");
		String projectTypeID = wizardContext.getData(ProjectWizard.PROJECT_TYPE).getProjectTypeId();

		if (projectTypeID.equals(AppServerExtConstants.WSO2_JAX_RS_PROJECT_ID) ||
		    projectTypeID.equals(AppServerExtConstants.WSO2_JAX_WS_PROJECT_ID)) {

			isCompleted = isCompleted && !view.getPackageName().equals("") &&
			              !view.getClassName().equals("");
		}

		return isCompleted;
	}

    @Override
    public void focusComponent() {
	    String projectTypeID = wizardContext.getData(ProjectWizard.PROJECT_TYPE).getProjectTypeId();

	    if(projectTypeID.equals(AppServerExtConstants.WSO2_JAX_RS_PROJECT_ID) ||
	       projectTypeID.equals(AppServerExtConstants.WSO2_JAX_WS_PROJECT_ID) ) {
		    view.swapToJAXServiceWizard(true);
		    delegate.updateControls();
	    }else {
		    view.swapToJAXServiceWizard(false);
		    delegate.updateControls();
	    }

    }

    @Override
    public void removeOptions() {

    }

    @Override
    public void go(AcceptsOneWidget container) {

        container.setWidget(view);
        view.reset();
        Project project = wizardContext.getData(ProjectWizard.PROJECT);
	    String projectTypeID = wizardContext.getData(ProjectWizard.PROJECT_TYPE).getProjectTypeId();

	    if(projectTypeID.equals(AppServerExtConstants.WSO2_JAX_RS_PROJECT_ID) ||
	       projectTypeID.equals(AppServerExtConstants.WSO2_JAX_WS_PROJECT_ID) ) {
		    view.swapToJAXServiceWizard(true);
		    delegate.updateControls();
	    }else {
		    view.swapToJAXServiceWizard(false);
		    delegate.updateControls();
	    }

	    if (project != null) {

            if (project.hasAttribute(MavenAttributes.MAVEN_ARTIFACT_ID)) {
                view.setArtifactId(project.getAttributeValue(MavenAttributes.MAVEN_ARTIFACT_ID));
                view.setGroupId(project.getAttributeValue(MavenAttributes.MAVEN_GROUP_ID));
                view.setVersion(project.getAttributeValue(MavenAttributes.MAVEN_VERSION));
                view.setVersion(project.getAttributeValue(MavenAttributes.MAVEN_PACKAGING));
                scheduleTextChanges();
            } else {
                pomReaderClient.readPomAttributes(project.getPath(), new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                    @Override
                    protected void onSuccess(String result) {
                        Jso jso = Jso.deserialize(result);
                        view.setArtifactId(jso.getStringField(MavenAttributes.MAVEN_ARTIFACT_ID));
                        view.setGroupId(jso.getStringField(MavenAttributes.MAVEN_GROUP_ID));
                        view.setVersion(jso.getStringField(MavenAttributes.MAVEN_VERSION));
                        view.setPackaging(jso.getStringField(MavenAttributes.MAVEN_PACKAGING));
                        scheduleTextChanges();
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        Log.error(MavenConfigurationPagePresenter.class, exception);
                    }
                });
            }
        }
    }

    private void scheduleTextChanges() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                onTextChange();
            }
        });
    }

    @Override
    public void commit(@NotNull final CommitCallback callback) {
        Map<String, List<String>> options = new HashMap<>();
        options.put(MavenAttributes.MAVEN_ARTIFACT_ID, Arrays.asList(view.getArtifactId()));
        options.put(MavenAttributes.MAVEN_GROUP_ID, Arrays.asList(view.getGroupId()));
        options.put(MavenAttributes.MAVEN_VERSION, Arrays.asList(view.getVersion()));
        options.put(MavenAttributes.MAVEN_PACKAGING, Arrays.asList(view.getPackaging()));

        final ProjectDescriptor projectDescriptor = factory.createDto(ProjectDescriptor.class);
        final String projectTypeID = wizardContext.getData(ProjectWizard.PROJECT_TYPE).getProjectTypeId();
        projectDescriptor.withProjectTypeId(projectTypeID);
        projectDescriptor.setAttributes(options);
        boolean visibility = wizardContext.getData(ProjectWizard.PROJECT_VISIBILITY);
        projectDescriptor.setVisibility(visibility ? AppServerExtConstants.PUBLIC_VISIBILITY : AppServerExtConstants
                .PRIVATE_VISIBILITY);
        projectDescriptor.setDescription(wizardContext.getData(ProjectWizard.PROJECT_DESCRIPTION));

        final String name = wizardContext.getData(ProjectWizard.PROJECT_NAME);
        final Project project = wizardContext.getData(ProjectWizard.PROJECT);

        projectGeneratorOptions.put(AppServerExtConstants.GENERATOR_PROJECT_TYPE, projectTypeID);
	    projectGeneratorOptions.put(AppServerExtConstants.GENERATOR_PROJECT_NAME, name);

	    if(projectTypeID.equals(AppServerExtConstants.WSO2_JAX_RS_PROJECT_ID) ||
	       projectTypeID.equals(AppServerExtConstants.WSO2_JAX_WS_PROJECT_ID) ) {
		    projectGeneratorOptions.put(AppServerExtConstants.GENERATOR_PACKAGE_NAME, view.getPackageName());
		    projectGeneratorOptions.put(AppServerExtConstants.GENERATOR_CLASS_NAME, view.getClassName());
	    }else{
		    projectGeneratorOptions.put(AppServerExtConstants.GENERATOR_WEB_CONTEXT_ROOT, view.getPackageName());
		    projectGeneratorOptions.put(AppServerExtConstants.GENERATOR_WEB_CONTENT_FOLDER, view.getClassName());
	    }

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
        projectServiceClient.updateProject(project.getPath(), projectDescriptor,
                                                                new AsyncRequestCallback<ProjectDescriptor>() {
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

    private void createProject(final CommitCallback callback, ProjectDescriptor projectDescriptor, final String name) {
        // Create Initial Project
        projectServiceClient
                .createProject(name, projectDescriptor,
                        new AsyncRequestCallback<ProjectDescriptor>() {

                            @Override
                            protected void onSuccess(ProjectDescriptor result) {
                                // Refresh Initial Project
                                resourceProvider.getProject(name, new AsyncCallback<Project>() {
                                    @Override
                                    public void onSuccess(Project project) {
                                        wizardContext.putData(ProjectWizard.PROJECT, project);

                                        // generate project contents
                                        projectServiceClient.generateProject(project.getPath(),
                                                AppServerExtConstants.WSO2_APP_SERVER_PROJECT_GENERATOR_ID, projectGeneratorOptions,

                                                new AsyncRequestCallback<ProjectDescriptor>() {
                                                    @Override
                                                    protected void onSuccess(ProjectDescriptor projectDescriptor) {

                                                        // Refresh generated project
                                                        resourceProvider.getProject(name, new AsyncCallback<Project>() {
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
                                                    protected void onFailure(Throwable throwable) {
                                                        callback.onFailure(throwable);
                                                    }
                                                });
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

    @Override
    public boolean canSkip() {
        return false;
    }

    @Override
    public void onTextChange() {
        delegate.updateControls();
    }

}
