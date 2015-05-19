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

import com.google.gwt.core.client.GWT;
import com.google.inject.name.Named;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.parts.base.BasePresenter;
import com.google.gwt.resources.client.ImageResource;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.rest.AsyncRequestCallback;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.DtoUnmarshallerFactory;
import org.eclipse.che.ide.rest.Unmarshallable;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.factory.AppFactoryAutoBeanFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.resources.AppFactoryExtensionResources;
import org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.login.LoginPresenter;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.constants.AppFactoryAPIConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryHTTPResponse;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.HTTPRequestInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppInfoList;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a view handler to the App list part which is shown with App Factory perspective
 */
public class AppListPresenter extends BasePresenter implements AppListView.ActionDelegate {

    private AppListView appListView;
    private String title;
    private LoginPresenter loginPresenter;
    private AppFactoryExtensionResources extensionResources;
    private final DtoFactory dtoFactory;
    private final DtoUnmarshallerFactory unmarshallerFactory;
    private final AsyncRequestFactory requestFactory;
    private final String restContext;
    private NotificationManager notificationManager;

    /**
     * Creates an App list part presenter with GWT injected App list view, login presenter, and extension resources
     */
    @Inject
    public AppListPresenter(@Named("restContext") String restContext, AppListView appListView, @Assisted String title,
                            LoginPresenter loginPresenter, AppFactoryExtensionResources extensionResources, DtoFactory dtoFactory, DtoUnmarshallerFactory unmarshallerFactory, AsyncRequestFactory requestFactory, NotificationManager notificationManager) {
        this.appListView = appListView;
        this.appListView.setDelegate(this);
        this.appListView.setTitle(title);
        this.title = title;
        this.loginPresenter = loginPresenter;
        this.extensionResources = extensionResources;
        this.dtoFactory = dtoFactory;
        this.unmarshallerFactory = unmarshallerFactory;
        this.requestFactory = requestFactory;
        this.restContext = restContext;
        this.notificationManager = notificationManager;
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
        //Disabling the refresh button till process completes
        appListView.enableRefreshButton(false);
        appListView.setMessage("Loading Applications...");
        appListView.removeData();

        final HTTPRequestInfo appListRequest = dtoFactory.createDto(HTTPRequestInfo.class);
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put(AppFactoryAPIConstants.ACTION_PARAM, AppFactoryAPIConstants.GET_APP_LIST_ACTION);
        appListRequest.setRequestParams(requestParams);

        Unmarshallable<AppFactoryHTTPResponse> unmarshaller = unmarshallerFactory.newUnmarshaller(AppFactoryHTTPResponse.class);

        requestFactory.createPostRequest(restContext + "/" + AppFactoryExtensionConstants.AF_REST_SERVICE_PATH
                + "/" + AppFactoryExtensionConstants.AF_REST_APP_LIST_PATH, appListRequest)
                .send(new AsyncRequestCallback<AppFactoryHTTPResponse>(unmarshaller) {

                    @Override
                    protected void onSuccess(AppFactoryHTTPResponse appListResponse) {
                        if (appListResponse.isRequestSuccess()) {
                            // Instantiate the factory
                            AppFactoryAutoBeanFactory autoBeanFactory = GWT.create(AppFactoryAutoBeanFactory.class);
                            AutoBean<AppInfoList> bean = AutoBeanCodex.decode(autoBeanFactory, AppInfoList.class, "{\"appInfoList\": " + appListResponse.getResponse() + "}");
                            AppInfoList appInfoList = bean.as();
                            appListView.setAppData(appInfoList);
                            appListView.setMessage(AppFactoryExtensionConstants.EMPTY_STRING);
                        } else {
                            appListView.setMessage("Loading Applications Failed...");
                            notificationManager.showError(appListResponse.getErrorType().toString() + "...");
                        }
                        //Enabling refresh button once its done
                        appListView.enableRefreshButton(true);
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        appListView.setMessage("Loading Applications Failed...");
                        notificationManager.showError("An Unexpected Error Occurred");
                        //Enabling refresh button once its done
                        appListView.enableRefreshButton(true);
                    }
                });
    }
}
