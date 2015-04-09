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
package org.wso2.developerstudio.codenvy.core.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.wso2.developerstudio.codenvy.core.client.actions.AboutDialogBoxAction;
import org.wso2.developerstudio.codenvy.core.client.actions.OpenDashboardAction;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.DashboardItemRegistry;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.page.DashboardPagePresenter;
import org.wso2.developerstudio.codenvy.core.shared.CoreExtConstants;

@Singleton
@Extension(title = CoreExtConstants.EXT_NAME_PREFIX + CoreExtConstants.EXT_NAME,
           version = CoreExtConstants.EXT_VERSION)
public class CoreExtension {

	@Inject
	public CoreExtension(ActionManager actionManager, AboutDialogBoxAction aboutAction,
	                     OpenDashboardAction openDashboardAction,
	                     WorkspaceAgent workspaceAgent,
	                     DashboardPagePresenter dashboardPagePresenter,
	                     DashboardItemRegistry dashboardItemRegistry) {

		DefaultActionGroup wso2CloudStudioActionGroup =
				new DefaultActionGroup(CoreExtConstants.WSO2_ACTION_GROUP_NAME, true, actionManager);
		actionManager
				.registerAction(CoreExtConstants.WSO2_ACTION_GROUP_ID, wso2CloudStudioActionGroup);
		DefaultActionGroup mainMenu =
				(DefaultActionGroup) actionManager.getAction(IdeActions.GROUP_MAIN_MENU);
		mainMenu.add(wso2CloudStudioActionGroup);

		actionManager.registerAction(CoreExtConstants.WSO2_ABOUT_ACTION_ID, aboutAction);
		wso2CloudStudioActionGroup.add(aboutAction);

		actionManager.registerAction(CoreExtConstants.WSO2_OPEN_DASHBOARD_ACTION_ID,
		                             openDashboardAction);
		wso2CloudStudioActionGroup.add(openDashboardAction);

	}
}