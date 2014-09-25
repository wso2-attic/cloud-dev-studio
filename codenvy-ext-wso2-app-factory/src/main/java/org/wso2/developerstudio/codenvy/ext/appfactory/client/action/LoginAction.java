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
package org.wso2.developerstudio.codenvy.ext.appfactory.client.action;

import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.workspace.PartPresenter;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.factory.AppFactoryPartsFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtConstants;

import static com.codenvy.ide.api.ui.workspace.PartStackType.INFORMATION;
import static com.codenvy.ide.api.ui.workspace.PartStackType.TOOLING;

public class LoginAction extends Action {

    @Inject
    private AppFactoryPartsFactory appFactoryPartsFactory;
    @Inject
    private WorkspaceAgent workspaceAgent;

    public LoginAction() {
        super(AppFactoryExtConstants.ACTION_LOGIN_AF_NAME);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        PartPresenter appFacPartPresenter = appFactoryPartsFactory.createAppListPart(AppFactoryExtConstants.WSO2_APP_FAC_VIEW_APPLIST);
        workspaceAgent.openPart(appFacPartPresenter, TOOLING);
        workspaceAgent.setActivePart(appFacPartPresenter);

        workspaceAgent.openPart(appFactoryPartsFactory.createAppDetailsPart(AppFactoryExtConstants.WSO2_APP_FAC_VIEW_CONSOLE), INFORMATION);
        workspaceAgent.openPart(appFactoryPartsFactory.createAppDetailsPart(AppFactoryExtConstants.WSO2_APP_FAC_VIEW_APPDETAILS), INFORMATION);
    }

}
