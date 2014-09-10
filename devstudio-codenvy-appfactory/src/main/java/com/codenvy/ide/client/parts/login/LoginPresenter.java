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
package com.codenvy.ide.client.parts.login;

import com.codenvy.ide.api.parts.base.BasePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.inject.Inject;
import com.google.gwt.user.client.Window;

/**
 * The presenter that represent general part for showing part API.
 *
 * @author Sohani
 */
public class LoginPresenter implements LoginView.ActionDelegate {
    private LoginView view;
    private String title;
    private String wso2AppCloud;
    private String email;
    private String password;
    
    @Inject
    public LoginPresenter(LoginView view) {
        this.view = view;
        this.view.setDelegate(this);
    }

 /** {@inheritDoc} */ 
   @Override
        public void onValueChanged() {
        wso2AppCloud = view.getWso2AppCloud();
        email = view.getEmail();
        password = view.getPassword();
      
        }
 
  /** Show dialog. */
    public void showDialog() {
        view.showDialog();
    }

    @Override
    public void minimize() {
        // TODO Auto-generated method stub
       
    }

    @Override
    public void onAppCloudChosen() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onAppFacChosen() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCancelButtonClicked() {
       Window.alert("Cancel Selected");
       view.close();
    }

    @Override
    public void onOKButtonClicked() {
       
    	}
        
    }



