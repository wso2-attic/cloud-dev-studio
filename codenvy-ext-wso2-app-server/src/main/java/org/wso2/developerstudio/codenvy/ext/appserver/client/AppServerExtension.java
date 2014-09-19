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
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.appserver.client.ui.wizard.page.maven.MavenSettingsPagePresenter;
import org.wso2.developerstudio.codenvy.ext.appserver.shared.Constants;

/** WSO2 Registry Extension. */
@Singleton
@Extension(title = "WSO2 App Server extension", version = "1.0.0")
public class AppServerExtension {

    @Inject
    public AppServerExtension(NotificationManager notificationManager,
                              AppServerExtensionResources resources,
                              ProjectTypeWizardRegistry wizardRegistry,
                              Provider<MavenSettingsPagePresenter> mavenSettingsPagePresenter) {

        ProjectWizard wizard = new ProjectWizard(notificationManager);
        wizard.addPage(mavenSettingsPagePresenter);
        wizardRegistry.addWizard(Constants.WSO2_JAXWS_PROJECT_ID, wizard);

    }
}