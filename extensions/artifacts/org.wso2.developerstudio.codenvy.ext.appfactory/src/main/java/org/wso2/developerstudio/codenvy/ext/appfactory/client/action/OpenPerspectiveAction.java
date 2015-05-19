/*
* Copyright (c) 2014-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.login.LoginPresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.factory.AppFactoryPartsFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.appdetails.AppDetailsPresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.applist.AppListPresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.console.ConsolePresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;

import static org.eclipse.che.ide.api.parts.PartStackType.TOOLING;
import static org.eclipse.che.ide.api.parts.PartStackType.INFORMATION;

/**
 * This is an Action class to open App Factory perspective
 */
public class OpenPerspectiveAction extends Action {

    @Inject
    private AppFactoryPartsFactory appFactoryPartsFactory;
    @Inject
    private WorkspaceAgent workspaceAgent;
    @Inject
    private LoginPresenter loginPresenter;
    private AppListPresenter appListPresenter;
    private AppDetailsPresenter appDetailsPresenter;
    private ConsolePresenter consolePresenter;
    private boolean presentersInitialized;

    public OpenPerspectiveAction() {
        super(AppFactoryExtensionConstants.OPEN_AF_PERSPECTIVE_ACTION_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!presentersInitialized) {
            appListPresenter = appFactoryPartsFactory.createAppListPart(AppFactoryExtensionConstants
                    .WSO2_APP_FAC_VIEW_APP_LIST);
            appDetailsPresenter = appFactoryPartsFactory.createAppDetailsPart(AppFactoryExtensionConstants
                    .WSO2_APP_FAC_VIEW_APP_DETAILS);
            consolePresenter = appFactoryPartsFactory.createConsolePart(AppFactoryExtensionConstants
                    .WSO2_APP_FAC_VIEW_CONSOLE);
            presentersInitialized = true;
        } else {
            workspaceAgent.removePart(appListPresenter);
            workspaceAgent.removePart(appDetailsPresenter);
            workspaceAgent.removePart(consolePresenter);
        }
        workspaceAgent.openPart(appListPresenter, TOOLING);
        workspaceAgent.openPart(appDetailsPresenter, TOOLING);
        workspaceAgent.openPart(consolePresenter, INFORMATION);
        workspaceAgent.setActivePart(appListPresenter);
        workspaceAgent.setActivePart(consolePresenter);
        loginPresenter.showDialog();
    }
}
