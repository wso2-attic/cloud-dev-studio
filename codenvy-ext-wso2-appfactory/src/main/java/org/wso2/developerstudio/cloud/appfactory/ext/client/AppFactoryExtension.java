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
package org.wso2.developerstudio.cloud.appfactory.ext.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.cloud.appfactory.ext.client.action.OpenPerspectiveAction;
import org.wso2.developerstudio.cloud.appfactory.ext.shared.Constants;
import org.wso2.developerstudio.cloud.core.shared.CoreConstants;

@Singleton
@Extension(title = CoreConstants.EXT_NAME_PREFIX + Constants.EXTENSION_NAME,
            version = CoreConstants.EXT_APP_FACTORY_VERSION)
public class AppFactoryExtension {

    @Inject
    public AppFactoryExtension(ActionManager actionManager, OpenPerspectiveAction openAFAction) {

        DefaultActionGroup wso2ActionGroup = (DefaultActionGroup) actionManager
                .getAction(CoreConstants.WSO2_ACTION_GROUP_ID);
        actionManager.registerAction(Constants.OPEN_AF_PERSPECTIVE_ACTION_ID, openAFAction);
        wso2ActionGroup.add(openAFAction);

//        AsyncRequest.build(RequestBuilder.GET, "/api/afclient/kavi").send(new AsyncRequestCallback<String>(new StringUnmarshaller()) {
//            protected void onSuccess(String answer) {
//
//                Window.alert(answer);
//            };
//            protected void onFailure(Throwable arg0) {};
//        });

    }
}



