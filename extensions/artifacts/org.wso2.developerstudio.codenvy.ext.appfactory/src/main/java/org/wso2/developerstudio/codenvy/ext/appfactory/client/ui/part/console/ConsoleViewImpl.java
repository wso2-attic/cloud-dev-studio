/*
* Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.codenvy.ext.appfactory.client.ui.part.console;

import org.eclipse.che.ide.api.parts.PartStackUIResources;
import org.eclipse.che.ide.api.parts.base.BaseView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Class that constructs console view for console part using GWT components
 */
public class ConsoleViewImpl extends BaseView<ConsoleView.ActionDelegate> implements ConsoleView {

    /**
     * Interface defines the UI binder for <code>ConsoleViewImpl</code> class with <code>ConsoleViewImpl.ui.xml</code> file
     */
    interface ConsoleViewImplUiBinder extends UiBinder<Widget, ConsoleViewImpl> {
        //No body, since it is only for binding purpose
    }

    @UiField
    Button clearConsoleButton;

    /**
     * Creates a console view implementation with GWT injected UI binder and resources
     */
    @Inject
    public ConsoleViewImpl(ConsoleViewImplUiBinder ourUiBinder, PartStackUIResources resources) {
        super(resources);
        setContentWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("clearConsoleButton")
    public void onButtonClicked(ClickEvent event) {
        delegate.onClearButtonClicked();
    }
}
