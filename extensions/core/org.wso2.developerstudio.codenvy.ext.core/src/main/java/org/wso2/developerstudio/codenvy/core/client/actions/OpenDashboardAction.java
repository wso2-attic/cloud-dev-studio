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
package org.wso2.developerstudio.codenvy.core.client.actions;

import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.workspace.PartStackType;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.page.DashboardPagePresenter;
import org.wso2.developerstudio.codenvy.core.shared.CoreExtConstants;

public class OpenDashboardAction extends Action {

	@Inject
	private DashboardPagePresenter dashboardPagePresenter;

	@Inject
	private WorkspaceAgent workspaceAgent;

	public OpenDashboardAction() {
		super(CoreExtConstants.WSO2_OPEN_DASHBOARD_ACTION_NAME);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		workspaceAgent.openPart(dashboardPagePresenter, PartStackType.EDITING);
	}
}
