/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.client.parts;

import java.util.Timer;

import com.codenvy.ide.api.parts.PartStackUIResources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

/**
 * The implementation of {@link AppFacPartView}.
 *
 * @author Sohani
 */
public class AppFacPartViewImpl extends BaseView<AppFacPartView.ActionDelegate> implements AppFacPartView {
    interface AppFacPartViewImplUiBinder extends UiBinder<Widget, AppFacPartViewImpl> {
    }

    @UiField
    Button button;
   
    
    @Inject
    public AppFacPartViewImpl(AppFacPartViewImplUiBinder ourUiBinder, PartStackUIResources resources) {
        super(resources);
        container.add(ourUiBinder.createAndBindUi(this));
    
    }

    @UiHandler("button")
    public void onButtonClicked(ClickEvent event) {
        delegate.onButtonClicked();
    }
    
    
     
}
