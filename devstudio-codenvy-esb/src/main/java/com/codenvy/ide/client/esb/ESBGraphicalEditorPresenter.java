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
package com.codenvy.ide.client.esb;


import com.codenvy.ide.api.ui.workspace.AbstractPartPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.codenvy.ide.client.inject.JSBundle;
import com.codenvy.ide.client.inject.ESBJavaScriptInjector;

/**
 * The presenter that represent general part for showing part API.
 *
 * @author Sohani
 */
public class ESBGraphicalEditorPresenter extends AbstractPartPresenter implements ESBGraphicalEditorView.ActionDelegate {
    private ESBGraphicalEditorView view;
   
    @Inject
    public ESBGraphicalEditorPresenter(ESBGraphicalEditorView view) {
        this.view = view;
        this.view.setDelegate(this);
     }

  
    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }
    
    @Override
    public String getTitleToolTip() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void minimize() {
        // TODO Auto-generated method stub
        
    }


    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public ImageResource getTitleImage() {
        // TODO Auto-generated method stub
        return null;
    }

}

