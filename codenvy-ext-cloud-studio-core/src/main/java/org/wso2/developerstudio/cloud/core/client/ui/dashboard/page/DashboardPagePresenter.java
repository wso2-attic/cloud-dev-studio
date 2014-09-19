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
package org.wso2.developerstudio.cloud.core.client.ui.dashboard.page;

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.api.editor.EditorInput;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.cloud.core.client.ui.dashboard.DashboardItemRegistry;
import org.wso2.developerstudio.cloud.core.shared.CoreConstants;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Singleton
public class DashboardPagePresenter extends AbstractEditorPresenter {


    private final DashboardItemRegistry itemRegistry;
    private final DashboardPageView view;

    @Inject
    public DashboardPagePresenter(DashboardItemRegistry itemRegistry, DashboardPageView view) {
        this.itemRegistry = itemRegistry;
        this.view = view;
    }

    @Override
    protected void initializeEditor() {

    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public String getTitle() {
        return CoreConstants.DASHBOARD_TITLE;
    }

    @Nullable
    @Override
    public ImageResource getTitleImage() {
        return null;
    }

    @Nullable
    @Override
    public String getTitleToolTip() {
        return null;
    }

    @Override
    public void doSave() {

    }

    @Override
    public void doSave(@NotNull AsyncCallback<EditorInput> editorInputAsyncCallback) {

    }

    @Override
    public void doSaveAs() {

    }

    @Override
    public void activate() {

    }
}
