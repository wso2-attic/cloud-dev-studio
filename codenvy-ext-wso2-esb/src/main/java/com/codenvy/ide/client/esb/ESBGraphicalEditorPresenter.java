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

