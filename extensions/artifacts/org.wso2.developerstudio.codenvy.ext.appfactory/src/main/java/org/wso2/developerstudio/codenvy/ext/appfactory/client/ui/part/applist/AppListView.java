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

import org.eclipse.che.ide.api.mvp.View;
import org.eclipse.che.ide.api.parts.base.BaseActionDelegate;
import com.google.inject.ImplementedBy;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppInfoList;

@ImplementedBy(AppListViewImpl.class)
public interface AppListView extends View<AppListView.ActionDelegate> {

    /**
     * Interface for App list view action delegation
     */
    public interface ActionDelegate extends BaseActionDelegate {

        /**
         * Action Handler for login button clicked
         */
        void onLoginButtonClicked();

        /**
         * Action Handler for refresh button clicked
         */
        void onRefreshButtonClicked();
    }

    /**
     * Set title of App list part.
     *
     * @param title part title
     */
    void setTitle(String title);

    /**
     * Set message for App list part.
     *
     * @param message
     */
    void setMessage(String message);

    /**
     * Disable refresh button once its clicked
     *
     * @param enable
     */
    void enableRefreshButton(boolean enable);

    /**
     * Set the Application Info data
     *
     * @param appInfoList
     */
    void setAppData(AppInfoList appInfoList);

    /**
     * Remove App list data
     */
    void removeData();

}
