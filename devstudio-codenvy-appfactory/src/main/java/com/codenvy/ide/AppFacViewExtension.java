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



/** Extension used to demonstrate the parts feature
 * 
 * @author Sohani

 *  */
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



