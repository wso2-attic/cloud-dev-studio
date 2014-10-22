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

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.api.editor.EditorInput;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.DashboardItem;
import org.wso2.developerstudio.codenvy.core.client.ui.dashboard.DashboardItemRegistry;
import org.wso2.developerstudio.codenvy.core.shared.CoreExtConstants;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DashboardPagePresenter extends AbstractEditorPresenter implements DashboardPageView.ActionDelegate{


    private final DashboardItemRegistry itemRegistry;
    private final DashboardPageView view;
    private Map<String, List<DashboardItem>> itemMap;
    private List<String> categoryList;
    private List<DashboardItem> itemsByCategory;

    @Inject
    public DashboardPagePresenter(DashboardItemRegistry itemRegistry, DashboardPageView view) {
        this.itemRegistry = itemRegistry;
        this.view = view;

        view.setDelegate(this);
    }

    @Override
    protected void initializeEditor() {

    }

    @Override
    public void go(AcceptsOneWidget container) {
        generateItemMap();
        view.generateDashboard(itemMap);
        container.setWidget(view);
    }

    @Override
    public String getTitle() {
        return CoreExtConstants.DASHBOARD_TITLE;
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

    public Map<String, List<DashboardItem>> generateItemMap() {

        itemMap =  new HashMap<String, List<DashboardItem>>();
        categoryList = new ArrayList<String>();
        List<DashboardItem> dashboardItems = itemRegistry.getDashboardItems();

        if (!dashboardItems.isEmpty()) {
            for (DashboardItem ditem : dashboardItems) {
                if(!categoryList.contains(ditem.getCategory().getCategoryName())) {
                    categoryList.add(ditem.getCategory().getCategoryName());
                }
            }

            for (String catName : categoryList) {
                itemsByCategory = new ArrayList<DashboardItem>();
                for (DashboardItem dashItem : dashboardItems) {
                    if (dashItem.getCategory().getCategoryName() == catName) {
                        itemsByCategory.add(dashItem);
                    }
                }
                itemMap.put(catName, itemsByCategory);
            }
        } else {
            // log the error

        }
        return itemMap;
    }
}