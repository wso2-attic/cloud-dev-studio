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
package org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.applist;

import org.eclipse.che.ide.api.parts.PartStackUIResources;
import org.eclipse.che.ide.api.parts.base.BaseView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.resources.AppFactoryExtensionResources;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppInfoList;

/**
 * Class that constructs App list view for App list part using GWT components
 */
public class AppListViewImpl extends BaseView<AppListView.ActionDelegate> implements AppListView {

    /**
     * Interface defines the UI binder for <code>AppListViewImpl</code> class with <code>AppListViewImpl.ui.xml</code> file
     */
    interface AppListViewImplUiBinder extends UiBinder<Widget, AppListViewImpl> {
        //No body, since it is only for binding purpose
    }

    @UiField
    HorizontalPanel buttonPanel;
    @UiField
    SimplePanel loginButtonPanel;
    @UiField
    SimplePanel refreshButtonPanel;
    @UiField
    VerticalPanel appListPanel;
    @UiField
    Label messageLabel;
    @UiField
    FlowPanel infoPanel;
    @UiField
    FlowPanel buildPanel;
    @UiField
    FlowPanel teamPanel;
    @UiField
    FlowPanel dataSourcesPanel;

    private PushButton refreshButton;
    private VerticalPanel appsPanel;

    /**
     * Creates a App list view implementation with GWT injected UI binder and resources
     */
    @Inject
    public AppListViewImpl(AppListViewImplUiBinder appListViewImplUiBinder, PartStackUIResources resources,
                           AppFactoryExtensionResources extensionResources) {
        super(resources);
        setContentWidget(appListViewImplUiBinder.createAndBindUi(this));

        buttonPanel.setSpacing(6);

        ImageResource loginIcon = extensionResources.getLoginIcon();
        PushButton loginButton = new PushButton(new Image(loginIcon));
        loginButton.setPixelSize(16, 16);
        loginButton.setTitle(AppFactoryExtensionConstants.LOGIN_ACTION);
        loginButtonPanel.add(loginButton);

        //Action handler for login button in App list part
        loginButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delegate.onLoginButtonClicked();
            }
        });

        ImageResource refreshIcon = extensionResources.getRefreshIcon();
        refreshButton = new PushButton(new Image(refreshIcon));
        refreshButton.setPixelSize(16, 16);
        refreshButton.setTitle(AppFactoryExtensionConstants.REFRESH_ACTION);
        refreshButtonPanel.add(refreshButton);

        //Action handler for refresh button in App list part
        refreshButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delegate.onRefreshButtonClicked();
            }
        });
    }

    @Override
    public void setMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    @Override
    public void enableRefreshButton(boolean enable) {
        refreshButton.setEnabled(enable);
    }

    @Override
    public void setAppData(AppInfoList appInfoList) {
        appListPanel.setSpacing(5);
        appsPanel = new VerticalPanel();
        appsPanel.setSpacing(5);
        for (AppInfo appInfo : appInfoList.getAppInfoList()) {
            appsPanel.add(new Label(appInfo.getName()));
        }
        appListPanel.add(appsPanel);
    }

    @Override
    public void removeData() {
        if (appsPanel != null) {
            appListPanel.remove(appsPanel);
        }
    }

}
