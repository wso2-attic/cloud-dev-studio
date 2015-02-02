/**
 *
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.developerstudio.che.ext.esb.client.editor.multipage;

import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.jseditor.client.texteditor.ConfigurableTextEditor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class ESBMultiPageEditorViewImpl extends Composite implements ESBMultiPageEditorView {

    interface ESBMultiPageEditorViewImplUiBinder
            extends UiBinder<Widget, ESBMultiPageEditorViewImpl> {
    }

    private static ESBMultiPageEditorViewImplUiBinder ourUiBinder = GWT.create(ESBMultiPageEditorViewImplUiBinder.class);
    @UiField
    Button          showTextEditor;
    @UiField
    Button          showGraphicalEditor;
    @UiField
    SimplePanel     textEditor;
    @UiField
    SimplePanel     graphicalEditor;
    @UiField
    DockLayoutPanel pages;

    private ActionDelegate delegate;

    @Inject
    public ESBMultiPageEditorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void showTextEditor() {
        pages.setWidgetHidden(graphicalEditor, true);

        pages.setWidgetHidden(textEditor, false);
        pages.setWidgetSize(textEditor, 100);
    }

    @Override
    public void showGraphicalEditor() {
        pages.setWidgetHidden(graphicalEditor, false);

        pages.setWidgetHidden(textEditor, true);
        pages.setWidgetSize(textEditor, 0);
    }

    @Override
    public void addTextEditor(ConfigurableTextEditor editor) {
        editor.go(textEditor);
    }

    @Override
    public void addGraphicalEditor(EditorPartPresenter editor) {
        editor.go(graphicalEditor);
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @UiHandler("showTextEditor")
    public void onTextEditorChosen(ClickEvent event) {
        delegate.onTextEditorChosen();
    }

    @UiHandler("showGraphicalEditor")
    public void onGraphicalEditorChosen(ClickEvent event) {
        delegate.onGraphicalEditorChosen();
    }

}