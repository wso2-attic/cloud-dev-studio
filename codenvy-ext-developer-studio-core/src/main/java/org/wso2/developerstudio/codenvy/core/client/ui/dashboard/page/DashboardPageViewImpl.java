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
package org.wso2.developerstudio.codenvy.core.client.ui.dashboard.page;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.DashboardCategory;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.DashboardItem;

import java.util.List;
import java.util.Map;

@Singleton
public class DashboardPageViewImpl implements DashboardPageView {

    interface DashboardPageBinder extends UiBinder<Widget, DashboardPageViewImpl>{}

    private static DashboardPageBinder uiBinder = GWT.create(DashboardPageBinder.class);

    private final Widget rootElement;

    private ActionDelegate delegate;

    @UiField
    FlowPanel mainPanel;

    public DashboardPageViewImpl() {
        rootElement = uiBinder.createAndBindUi(this);
    }

    @Override
    public void setDelegate(ActionDelegate actionDelegate) {
        this.delegate = actionDelegate;
    }

    @Override
    public Widget asWidget() {
        return rootElement;
    }

    @Override
    public void addDashboardItem(DashboardItem item) {

    }

    @Override
    public void addDashboardCategory(DashboardCategory category) {

    }

    @Override
    public void setDashboardItemCategoryMap(Map<DashboardCategory, List<DashboardItem>> itemCatMap) {

    }

}
