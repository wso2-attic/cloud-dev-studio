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

import com.codenvy.ide.api.action.Action;
import com.codenvy.ide.api.action.ActionEvent;
import com.codenvy.ide.api.parts.PartPresenter;
import com.codenvy.ide.api.parts.WorkspaceAgent;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.login.LoginPresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.factory.AppFactoryPartsFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.appdetails.AppDetailsPresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.console.ConsolePresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;

import static com.codenvy.ide.api.parts.PartStackType.TOOLING;
import static com.codenvy.ide.api.parts.PartStackType.INFORMATION;

/**
 * This is an Action class to open App Factory perspective
 */
public class OpenPerspectiveAction extends Action {
    private static final Logger logger = LoggerFactory.getLogger(OpenPerspectiveAction.class);

    @Inject
    private AppFactoryPartsFactory appFactoryPartsFactory;
    @Inject
    private WorkspaceAgent workspaceAgent;
    @Inject
    private LoginPresenter loginPresenter;

    public OpenPerspectiveAction() {
        super(AppFactoryExtensionConstants.OPEN_AF_PERSPECTIVE_ACTION_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        loginPresenter.showDialog();

        PartPresenter appListPresenter = appFactoryPartsFactory.createAppListPart(
                AppFactoryExtensionConstants.WSO2_APP_FAC_VIEW_APP_LIST);
        workspaceAgent.openPart(appListPresenter, TOOLING);
        workspaceAgent.setActivePart(appListPresenter);

        AppDetailsPresenter appDetailsPresenter = appFactoryPartsFactory.createAppDetailsPart(
                AppFactoryExtensionConstants.WSO2_APP_FAC_VIEW_APP_DETAILS);
        workspaceAgent.openPart(appDetailsPresenter, TOOLING);

        ConsolePresenter consolePresenter = appFactoryPartsFactory.createConsolePart(
                AppFactoryExtensionConstants.WSO2_APP_FAC_VIEW_CONSOLE);
        workspaceAgent.openPart(consolePresenter, INFORMATION);
        workspaceAgent.setActivePart(consolePresenter);

        if (logger.isDebugEnabled()) {
            logger.debug("Opening the App Factory perspective is successful");
        }
    }
}
