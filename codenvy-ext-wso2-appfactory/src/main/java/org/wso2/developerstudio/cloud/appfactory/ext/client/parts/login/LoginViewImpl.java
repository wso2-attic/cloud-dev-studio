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
package org.wso2.developerstudio.cloud.appfactory.ext.client.parts.login;

import com.codenvy.ide.api.parts.PartStackUIResources;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import javax.annotation.Nonnull;

public class LoginViewImpl extends DialogBox implements LoginView {
    interface LoginViewImplUiBinder extends UiBinder<Widget, LoginViewImpl> {
    }

    private ActionDelegate delegate;
    @UiField
    RadioButton appCloud;
    @UiField
    RadioButton appFac;
    @UiField
    Button btnOK;
    @UiField
    Button btnCancel;
    @UiField
    TextBox wso2AppCloud;
    @UiField 
    TextBox email;
    @UiField
    PasswordTextBox password;
    

    @Inject
    public LoginViewImpl(LoginViewImplUiBinder ourUiBinder, PartStackUIResources resources) {
        Widget widget = ourUiBinder.createAndBindUi(this);
        this.setWidget(widget);
       
    }

    @UiHandler("btnOK")
    public void onOKButtonClicked(ClickEvent event) {
        delegate.onOKButtonClicked();
    }
    @UiHandler("btnCancel")
    public void onCacelButtonClicked(ClickEvent event) {
        delegate.onCancelButtonClicked();
    }

    @UiHandler("appCloud")
    public void onAppCloudClicked(ClickEvent event) {
        delegate.onAppCloudChosen();
    }
    
    @UiHandler("appFac")
    public void onAppFaClicked(ClickEvent event) {
        delegate.onAppFacChosen();
    }
    
    @UiHandler("wso2AppCloud")
    public void onWso2AppCloud(KeyUpEvent event) {
        delegate.onValueChanged();
    }
    
    @UiHandler("email")
    public void onEmail(KeyUpEvent event) {
        delegate.onValueChanged();
    }
    
    @UiHandler("password")
    public void onPassword(KeyUpEvent event) {
        delegate.onValueChanged();
    }
     
    @Override
    public void setDelegate(ActionDelegate actionDelegate) {
        this.delegate = actionDelegate;
    }

    @Override
    public Widget asWidget() {
        // TODO Auto-generated method stub
        return null;
    }
    
    /** {@inheritDoc} */
    @Override
    public void showDialog() {
        this.center();
        this.show();
    }
    /** {@inheritDoc} */
    @Override
    public void close() {
        this.hide();

    }


    @Override
    public boolean isAppCloud() {
        return appCloud.getValue();
    }

    @Override
    public boolean isAppFac() {
        return appFac.getValue();
    }

    @Override
    public void setAppCloud(boolean isAppCloud) {
        appCloud.setValue(isAppCloud);
        
        
    }

    @Override
    public void setAppFac(boolean isAppFac) {
        appFac.setValue(isAppFac);
    }

    /** {@inheritDoc} */
    @Override
    public String getWso2AppCloud() {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void setWso2AppCloud(@Nonnull String wso2AppCloud) {
        // TODO Auto-generated method stub
        
    }
    
    /** {@inheritDoc} */
    @Nonnull
    @Override
    public String getEmail() {
        return email.getText();
    }

    /** {@inheritDoc} */
    @Override
    public void setEmail(@Nonnull String email) {
        this.email.setText(email);
    }
    

    /** {@inheritDoc} */
    @Nonnull
    @Override
    public String getPassword() {
        return password.getText();
    }

    /** {@inheritDoc} */
    @Override
    public void setPassword(@Nonnull String password) {
        this.password.setText(password);
    }

    

}
