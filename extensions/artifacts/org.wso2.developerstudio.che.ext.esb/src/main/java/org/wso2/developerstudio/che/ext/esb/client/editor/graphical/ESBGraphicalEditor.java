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
package org.wso2.developerstudio.che.ext.esb.client.editor.graphical;

import elemental.html.IFrameElement;

import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.api.editor.EditorInput;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Frame;

import org.vectomatic.dom.svg.ui.SVGResource;
import org.wso2.developerstudio.che.ext.esb.client.editor.WSO2IFrameEditor;
import org.wso2.developerstudio.che.ext.esb.client.messages.GetContentMessage;
import org.wso2.developerstudio.che.ext.esb.client.messages.SendContentMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ESBGraphicalEditor extends AbstractEditorPresenter implements WSO2IFrameEditor {

    private Frame editorFrame;
    private AsyncCallback<String> callback;
    private boolean isDirty = true;

    public ESBGraphicalEditor() {
        editorFrame = new Frame("/jsplumb/index.html");
        editorFrame.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent event) {
                frameLoaded();
            }
        });
        editorFrame.setSize("100%", "100%");
    }

    @Override
    protected void initializeEditor() {

    }

    private void frameLoaded() {
        input.getFile().getContent(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Log.error(getClass(), caught);
            }

            @Override
            public void onSuccess(String result) {
                sendContentToIFrame(result);
                firePropertyChange(PROP_INPUT);
            }
        });
    }

    private void sendContentToIFrame(String result) {
        SendContentMessage message = SendContentMessage.make();
        message.setContent(result);
        message.setFilePath(input.getFile().getPath());
        IFrameElement frameElement = (IFrameElement)editorFrame.getElement();
        frameElement.getContentWindow().postMessage(message, "*");
    }

    @Override
    public void doSave() {
        doSave(new AsyncCallback<EditorInput>() {
            @Override
            public void onSuccess(final EditorInput result) {
                // do nothing
            }

            @Override
            public void onFailure(final Throwable caught) {
                // do nothing
            }
        });
    }

    @Override
    public void doSave(AsyncCallback<EditorInput> callback) {
        sendGetContentMessage();
    }

    private void sendGetContentMessage() {
        GetContentMessage getContentMessage = GetContentMessage.make();
        getContentMessage.setFilePath(input.getFile().getPath());
        IFrameElement frameElement = (IFrameElement)editorFrame.getElement();
        frameElement.getContentWindow().postMessage(getContentMessage,"*");
    }

    @Override
    public void onClose(@Nonnull AsyncCallback<Void> callback) {
        handleClose();
        callback.onSuccess(null);
    }

    @Override
    public void doSaveAs() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void activate() {

    }

    @Override
    public void close(boolean save) {

    }

    @Nullable
    @Override
    public SVGResource getTitleSVGImage() {
        return input.getSVGResource();
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
        container.setWidget(editorFrame);
    }

    public void saveContent(String content) {
        if(isDirty) {
            input.getFile().updateContent(content, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    Log.error(getClass(), caught);
                }

                @Override
                public void onSuccess(Void result) {
                    updateDirtyState(false);
                }
            });
        } else{
            isDirty = true;
            if(callback != null){
                callback.onSuccess(content);
            }
        }
    }

    public void setDirtyState(boolean dirty) {
        updateDirtyState(dirty);
    }

    public void serialize(String content){
        sendContentToIFrame(content);
    }

    public void deserialize(AsyncCallback<String> callback){
        this.callback = callback;
        this.isDirty = false;
        sendGetContentMessage();
    }
}
