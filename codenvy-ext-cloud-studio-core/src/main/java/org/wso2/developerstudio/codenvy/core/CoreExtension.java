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
package org.wso2.developerstudio.codenvy.core;

/**
 * Codenvy API imports. In this extension we'll need
 * to talk to Parts and Action API. Gin and Singleton
 * imports are obligatory as well for any extension
 */
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.api.ui.action.ActionManager;
import com.codenvy.ide.api.ui.action.Constraints;
import com.codenvy.ide.api.ui.action.DefaultActionGroup;
import com.codenvy.ide.api.ui.action.IdeActions;
import  org.wso2.developerstudio.codenvy.core.action.AboutAction;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @Singleton is required in case the instance is triggered several times this extension will be initialized several times as well.
 * @Extension lets us know this is an extension and code injected in it will be executed when launched
 */
@Singleton
@Extension(title = "Developer Studio Core", version = "1.0.0")
public class CoreExtension
{
    @Inject
    public CoreExtension(ActionManager actionManager, AboutAction action, ConsolePart console) {
        
        DefaultActionGroup demoGroup = new DefaultActionGroup("Developer Studio", true, actionManager);
        actionManager.registerAction("demoGroup", demoGroup);
        DefaultActionGroup mainMenu = (DefaultActionGroup)actionManager.getAction(IdeActions.GROUP_MAIN_MENU);
        mainMenu.add(demoGroup);
          
        //add demoAction to demoGroup
        actionManager.registerAction("about", action);
        demoGroup.add(action);

    }
}
