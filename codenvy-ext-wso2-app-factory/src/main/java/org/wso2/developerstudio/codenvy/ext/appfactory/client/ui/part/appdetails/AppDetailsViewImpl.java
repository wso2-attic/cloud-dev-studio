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
package org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.appdetails;

import com.codenvy.ide.api.parts.PartStackUIResources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AppDetailsViewImpl extends BaseView<AppDetailsView.ActionDelegate> implements AppDetailsView {

    interface AppFacPartViewImplUiBinder extends UiBinder<Widget, AppDetailsViewImpl> {
    }

    @UiField
    Button button;

    @Inject
    public AppDetailsViewImpl(AppFacPartViewImplUiBinder ourUiBinder, PartStackUIResources resources) {
        super(resources);
        container.add(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("button")
    public void onButtonClicked(ClickEvent event) {
        delegate.onButtonClicked();
    }

}
