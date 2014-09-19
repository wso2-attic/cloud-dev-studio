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
package org.wso2.developerstudio.cloud.core.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.action.IdeActions;
import com.codenvy.ide.api.ui.workspace.PartStackType;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.cloud.core.client.actions.AboutAction;
import org.wso2.developerstudio.cloud.core.client.actions.OpenDashboardAction;
import org.wso2.developerstudio.cloud.core.client.ui.dashboard.page.DashboardPagePresenter;
import org.wso2.developerstudio.cloud.core.shared.CoreConstants;

@Singleton
@Extension(title = CoreConstants.EXT_NAME_PREFIX + CoreConstants.CORE_EXTENSION_NAME,
        version = CoreConstants.EXT_CORE_VERSION)
public class CoreExtension {

    @Inject
    public CoreExtension(ActionManager actionManager, AboutAction aboutAction, OpenDashboardAction openDashboardAction,
                         ConsolePart console, WorkspaceAgent workspaceAgent, DashboardPagePresenter dashboardPagePresenter) {

        DefaultActionGroup wso2CloudStudioActionGroup = new DefaultActionGroup(CoreConstants.WSO2_ACTION_GROUP_NAME, true, actionManager);
        actionManager.registerAction(CoreConstants.WSO2_ACTION_GROUP_ID, wso2CloudStudioActionGroup);
        DefaultActionGroup mainMenu = (DefaultActionGroup) actionManager.getAction(IdeActions.GROUP_MAIN_MENU);
        mainMenu.add(wso2CloudStudioActionGroup);

        actionManager.registerAction(CoreConstants.WSO2_ABOUT_ACTION_ID, aboutAction);
        wso2CloudStudioActionGroup.add(aboutAction);

        actionManager.registerAction(CoreConstants.WSO2_OPEN_DASHBOARD_ACTION_ID, openDashboardAction);
        wso2CloudStudioActionGroup.add(openDashboardAction);

        workspaceAgent.openPart(dashboardPagePresenter, PartStackType.EDITING);

    }
}
