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


import java.util.HashMap;

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class ESBEditorPresenter extends AbstractEditorPresenter implements ESBEditorView.ActionDelegate {

        private ESBEditorView view;

    @Inject
    public ESBEditorPresenter(ESBEditorView view) {
        this.view = view;
        this.view.setDelegate(this);
    }
    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initializeEditor() {
        // TODO Auto-generated method stub
    }

    @Override
    public void minimize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void activate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doSave() {
        // TODO Auto-generated method stub
    }

    @Override
    public void doSaveAs() {
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

    @Override
    public String getTitleToolTip() {
        // TODO Auto-generated method stub
        return null;
    }
}
