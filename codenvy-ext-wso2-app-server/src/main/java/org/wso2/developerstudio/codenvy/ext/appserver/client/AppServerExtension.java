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
package org.wso2.developerstudio.codenvy.ext.appserver.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.ProjectTypeWizardRegistry;
import com.codenvy.ide.api.ui.wizard.ProjectWizard;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.wso2.developerstudio.cloud.core.shared.CoreExtConstants;
import org.wso2.developerstudio.codenvy.ext.appserver.client.ui.wizard.page.maven.MavenSettingsPagePresenter;
import org.wso2.developerstudio.codenvy.ext.appserver.client.ui.wizard.page.runner.RunnerSelectionPagePresenter;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.AppServerExtConstants;

/** WSO2 Registry Extension. */
@Singleton
@Extension(title = CoreExtConstants.EXT_NAME_PREFIX + AppServerExtConstants.EXT_NAME ,
        version = AppServerExtConstants.EXT_VERSION)
public class AppServerExtension {

    @Inject
    public AppServerExtension(NotificationManager notificationManager,
                              AppServerExtensionResources resources,
                              ProjectTypeWizardRegistry wizardRegistry,
                              Provider<MavenSettingsPagePresenter> mavenSettingsPagePresenter,
                              Provider<RunnerSelectionPagePresenter> runnerSelectionPagePresenterProvider) {

        ProjectWizard wizard = new ProjectWizard(notificationManager);
        wizard.addPage(mavenSettingsPagePresenter);
        wizard.addPage(runnerSelectionPagePresenterProvider);


        wizardRegistry.addWizard(AppServerExtConstants.WSO2_WEB_APP_PROJECT_ID, wizard);
        wizardRegistry.addWizard(AppServerExtConstants.WSO2_JAXWS_PROJECT_ID, wizard);


    }
}