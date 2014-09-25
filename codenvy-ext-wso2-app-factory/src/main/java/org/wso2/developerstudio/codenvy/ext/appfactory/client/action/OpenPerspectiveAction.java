package org.wso2.developerstudio.codenvy.ext.appfactory.client.action;

import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.workspace.PartPresenter;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.dialog.login.LoginPresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.factory.AppFactoryPartsFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtConstants;

import static com.codenvy.ide.api.ui.workspace.PartStackType.INFORMATION;
import static com.codenvy.ide.api.ui.workspace.PartStackType.TOOLING;

/**
 * Action to open App Factory Perspective
 */
public class OpenPerspectiveAction extends Action {

    @Inject
    private AppFactoryPartsFactory appFactoryPartsFactory;
    @Inject
    private WorkspaceAgent workspaceAgent;
    @Inject
    private LoginPresenter loginPresenter;

    public OpenPerspectiveAction() {
        super(AppFactoryExtConstants.OPEN_AF_PERSPECTIVE_ACTION_NAME);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        loginPresenter.showDialog();

        PartPresenter appFacPartPresenter = appFactoryPartsFactory.createAppListPart(AppFactoryExtConstants.WSO2_APP_FAC_VIEW_APPLIST);
        workspaceAgent.openPart(appFacPartPresenter, TOOLING);
        workspaceAgent.setActivePart(appFacPartPresenter);

        workspaceAgent.openPart(appFactoryPartsFactory.createAppDetailsPart(AppFactoryExtConstants.WSO2_APP_FAC_VIEW_CONSOLE), INFORMATION);
        workspaceAgent.openPart(appFactoryPartsFactory.createAppDetailsPart(AppFactoryExtConstants.WSO2_APP_FAC_VIEW_APPDETAILS), INFORMATION);
    }
}
