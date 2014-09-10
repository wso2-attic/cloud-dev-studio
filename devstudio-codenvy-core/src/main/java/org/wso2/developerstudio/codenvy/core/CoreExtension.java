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
