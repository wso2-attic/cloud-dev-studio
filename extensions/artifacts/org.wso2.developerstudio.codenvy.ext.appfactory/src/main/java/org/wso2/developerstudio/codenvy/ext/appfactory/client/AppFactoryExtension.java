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
package org.wso2.developerstudio.codenvy.ext.appfactory.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.action.ActionManager;
import com.codenvy.ide.api.action.DefaultActionGroup;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.core.shared.CoreExtConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.action.OpenPerspectiveAction;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;

@Singleton
@Extension(title = CoreExtConstants.EXT_NAME_PREFIX + AppFactoryExtensionConstants.EXT_NAME,
        version = AppFactoryExtensionConstants.EXT_VERSION)
public class AppFactoryExtension {

    @Inject
    public AppFactoryExtension(ActionManager actManager, OpenPerspectiveAction openAFAction,
                               NotificationManager notificationManager) {

        DefaultActionGroup wso2ActionGroup = (DefaultActionGroup) actManager
                .getAction(CoreExtConstants.WSO2_ACTION_GROUP_ID);
        actManager.registerAction(AppFactoryExtensionConstants.OPEN_AF_PERSPECTIVE_ACTION_ID,
                openAFAction);
        wso2ActionGroup.add(openAFAction);

    }
}



