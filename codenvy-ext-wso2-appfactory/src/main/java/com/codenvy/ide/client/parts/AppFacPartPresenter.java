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
package com.codenvy.ide.client.parts;

import com.codenvy.ide.api.parts.base.BasePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.codenvy.ide.client.parts.login.LoginPresenter;
import com.google.gwt.user.client.Element;

public class AppFacPartPresenter extends BasePresenter implements AppFacPartView.ActionDelegate {
    private AppFacPartView view;
    private String     title;
    //private Element id;
    private LoginPresenter loginPresenter;

    @Inject
    public AppFacPartPresenter(AppFacPartView view, @Assisted String title, LoginPresenter loginPresenter) {
        this.view = view;
        this.view.setDelegate(this);
        this.view.setTitle(title);
        this.title = title;
        this.loginPresenter =loginPresenter;
       // this.view.setID(id);
    }

    /** {@inheritDoc} */
    @Override
    public void onButtonClicked() {
       // Window.alert(title);
      //  loginPresenter.show();
        loginPresenter.showDialog();
    }
  
    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    @Override
    public ImageResource getTitleImage() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getTitleToolTip() {
        return "Tooltip";
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }
}
