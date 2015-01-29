/**
 * <!--
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
 * -->
 */
package org.wso2.developerstudio.che.ext.esb.client.editor.multipage;

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.api.editor.EditorInitException;
import com.codenvy.ide.api.editor.EditorInput;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.PartPresenter;
import com.codenvy.ide.api.parts.PropertyListener;
import com.codenvy.ide.jseditor.client.defaulteditor.DefaultEditorProvider;
import com.codenvy.ide.jseditor.client.document.EmbeddedDocument;
import com.codenvy.ide.jseditor.client.texteditor.ConfigurableTextEditor;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import org.vectomatic.dom.svg.ui.SVGResource;
import org.wso2.developerstudio.che.ext.esb.client.editor.WSO2IFrameEditor;
import org.wso2.developerstudio.che.ext.esb.client.editor.graphical.ESBGraphicalEditor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ESBMultiPageEditor extends AbstractEditorPresenter
        implements ESBMultiPageEditorView.ActionDelegate, PropertyListener, WSO2IFrameEditor {

    private final ESBMultiPageEditorView view;
    private final ESBGraphicalEditor graphicalEditor;
    private final ConfigurableTextEditor textEditor;

    @Inject
    public ESBMultiPageEditor(ESBMultiPageEditorView view,
                              ESBGraphicalEditor graphicalEditor,
                              DefaultEditorProvider editorProvider,
                              NotificationManager notificationManager) {
        this.view = view;
        view.setDelegate(this);

        this.graphicalEditor = graphicalEditor;
        graphicalEditor.addPropertyListener(this);
        view.addGraphicalEditor(graphicalEditor);

        textEditor = editorProvider.getEditor();
        textEditor.addPropertyListener(this);
        view.addTextEditor(textEditor);
    }

    @Override
    public void init(@Nonnull EditorInput input) throws EditorInitException {
        super.init(input);
        textEditor.init(input);
        graphicalEditor.init(input);
    }

    @Override
    protected void initializeEditor() {

    }

    @Override
    public void onTextEditorChosen() {
        view.showTextEditor();

        graphicalEditor.deserialize(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Log.error(ESBMultiPageEditor.class, caught);
            }

            @Override
            public void onSuccess(String result) {
                EmbeddedDocument document = textEditor.getDocument();
                document.replace(0, document.getContents().length(), result);
            }
        });
    }

    @Override
    public boolean isDirty() {
        return textEditor.isDirty() || graphicalEditor.isDirty();
    }

    @Override
    public void onGraphicalEditorChosen() {
        view.showGraphicalEditor();
        graphicalEditor.serialize(textEditor.getDocument().getContents());
    }

    @Override
    public void doSave() {
        textEditor.doSave();
    }

    @Override
    public void doSave(AsyncCallback<EditorInput> callback) {
        textEditor.doSave(callback);
    }

    @Override
    public void doSaveAs() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void close(boolean save) {

    }

    @Override
    public void onClose(@Nonnull AsyncCallback<Void> callback) {
        handleClose();
        textEditor.onClose(callback);
    }

    @Nonnull
    @Override
    public String getTitle() {
        if (isDirty()) {
            return "*" + input.getName();
        } else {
            return input.getName();
        }

    }

    @Nullable
    @Override
    public ImageResource getTitleImage() {
        return input.getImageResource();
    }

    @Nullable
    @Override
    public String getTitleToolTip() {
        return null;
    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Nullable
    @Override
    public SVGResource getTitleSVGImage() {
        return input.getSVGResource();
    }

    @Override
    public void propertyChanged(PartPresenter source, int propId) {
        if(propId == PROP_INPUT && source == textEditor){
            onGraphicalEditorChosen();
        }
        firePropertyChange(propId);
    }

    @Override
    public void saveContent(String content) {
        graphicalEditor.saveContent(content);
    }

    @Override
    public void setDirtyState(boolean dirty) {
        graphicalEditor.setDirtyState(dirty);
    }
}
