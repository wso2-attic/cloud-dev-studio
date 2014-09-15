package org.wso2.developerstudio.cloud.appfactory.ext.client.action;

import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.workspace.PartPresenter;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;
import org.wso2.developerstudio.cloud.appfactory.ext.client.parts.AppFacPartFactory;
import org.wso2.developerstudio.cloud.appfactory.ext.shared.Constants;

import static com.codenvy.ide.api.ui.workspace.PartStackType.INFORMATION;
import static com.codenvy.ide.api.ui.workspace.PartStackType.TOOLING;

/**
 * Action to open App Factory Perspective
 */
public class OpenAppFactoryPerspectiveAction extends Action {

    @Inject
    private AppFacPartFactory appFacPartFactory;
    @Inject
    private WorkspaceAgent workspaceAgent;

    public OpenAppFactoryPerspectiveAction() {
        super(Constants.OPEN_AF_PERSPECTIVE_ACTION_NAME);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        PartPresenter appFacPartPresenter = appFacPartFactory.create(Constants.WSO2_APP_FAC_VIEW_APPLIST);
        workspaceAgent.openPart(appFacPartPresenter, TOOLING);
        workspaceAgent.setActivePart(appFacPartPresenter);

        workspaceAgent.openPart(appFacPartFactory.createName(Constants.WSO2_APP_FAC_VIEW_CONSOLE), INFORMATION);
        workspaceAgent.openPart(appFacPartFactory.createName(Constants.WSO2_APP_FAC_VIEW_APPDETAILS), INFORMATION);

    }
}
