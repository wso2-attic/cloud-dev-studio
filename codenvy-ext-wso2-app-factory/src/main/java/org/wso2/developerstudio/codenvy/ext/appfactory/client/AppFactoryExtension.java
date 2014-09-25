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
package org.wso2.developerstudio.codenvy.ext.appfactory.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.action.OpenPerspectiveAction;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtConstants;
import org.wso2.developerstudio.codenvy.core.shared.CoreExtConstants;


@Singleton
@Extension(title = CoreExtConstants.EXT_NAME_PREFIX + AppFactoryExtConstants.EXT_NAME,
            version = AppFactoryExtConstants.EXT_VERSION)
public class AppFactoryExtension {

    @Inject
    public AppFactoryExtension(ActionManager actionManager, OpenPerspectiveAction openAFAction) {

        DefaultActionGroup wso2ActionGroup = (DefaultActionGroup) actionManager
                .getAction(CoreExtConstants.WSO2_ACTION_GROUP_ID);
        actionManager.registerAction(AppFactoryExtConstants.OPEN_AF_PERSPECTIVE_ACTION_ID, openAFAction);
        wso2ActionGroup.add(openAFAction);
    }
}



