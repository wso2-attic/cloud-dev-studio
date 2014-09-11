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
package com.codenvy.ide;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.workspace.PartPresenter;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.client.parts.AppFacPartFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.codenvy.ide.shared.Constants;
import static com.codenvy.ide.api.ui.workspace.PartStackType.INFORMATION;
import static com.codenvy.ide.api.ui.workspace.PartStackType.TOOLING;

@Singleton
@Extension(title = "App Factory Views", version = "1.0.0")
public class AppFacViewExtension {

    @Inject
    public AppFacViewExtension(WorkspaceAgent workspaceAgent,
                                  AppFacPartFactory appFacPartFactory
                                 
                                  ) {
       
        PartPresenter appFacPartPresenter = appFacPartFactory.create(Constants.WSO2_APP_FAC_VIEW_APPLIST);
        workspaceAgent.openPart(appFacPartPresenter, TOOLING);
        workspaceAgent.setActivePart(appFacPartPresenter);

        workspaceAgent.openPart(appFacPartFactory.createName(Constants.WSO2_APP_FAC_VIEW_CONSOLE), INFORMATION);
        workspaceAgent.openPart(appFacPartFactory.createName(Constants.WSO2_APP_FAC_VIEW_APPDETAILS), INFORMATION);
      
    }
}



