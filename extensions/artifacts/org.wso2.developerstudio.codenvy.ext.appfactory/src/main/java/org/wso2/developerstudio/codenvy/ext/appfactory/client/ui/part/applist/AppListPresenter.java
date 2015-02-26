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

import com.codenvy.ide.api.parts.base.BasePresenter;
import com.google.gwt.resources.client.ImageResource;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.resources.AppFactoryExtensionResources;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.login.LoginPresenter;

import javax.annotation.Nonnull;

/**
 * Provides a view handler to the App list part which is shown with App Factory perspective
 */
public class AppListPresenter extends BasePresenter implements AppListView.ActionDelegate {

    private AppListView appListView;
    private String title;
    private LoginPresenter loginPresenter;
    private AppFactoryExtensionResources extensionResources;

    /**
     * Creates an App list part presenter with GWT injected App list view, login presenter, and extension resources
     */
    @Inject
    public AppListPresenter(AppListView appListView, @Assisted String title,
                            LoginPresenter loginPresenter, AppFactoryExtensionResources extensionResources) {
        this.appListView = appListView;
        this.appListView.setDelegate(this);
        this.appListView.setTitle(title);
        this.title = title;
        this.loginPresenter = loginPresenter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageResource getTitleImage() {
        return extensionResources.getLoginIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitleToolTip() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(appListView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoginButtonClicked() {
        loginPresenter.showDialog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRefreshButtonClicked() {
        //TODO - Need to implement the logic with App Factory server side implementation
    }
}
