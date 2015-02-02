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
package org.wso2.developerstudio.che.ext.esb.client.editor;

import elemental.client.Browser;
import elemental.events.Event;
import elemental.events.EventListener;
import elemental.events.MessageEvent;

import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.collections.StringMap;
import com.google.gwt.webworker.client.messages.MessageFilter;
import com.google.gwt.webworker.client.messages.MessageImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.wso2.developerstudio.che.ext.esb.client.messages.DirtyStateChangedMessage;
import org.wso2.developerstudio.che.ext.esb.client.messages.SaveContentMessage;

@Singleton
public class MessagesRouter {

    private final EditorAgent editorAgent;
    private final MessageFilter filter;

    @Inject
    public MessagesRouter(final EditorAgent editorAgent) {
        this.editorAgent = editorAgent;
        filter = new MessageFilter();
        filter.registerMessageRecipient(3, new MessageFilter.MessageRecipient<SaveContentMessage>() {
            @Override
            public void onMessageReceived(final SaveContentMessage message) {
                editorAgent.getOpenedEditors().iterate(new StringMap.IterationCallback<EditorPartPresenter>() {
                    @Override
                    public void onIteration(String key, EditorPartPresenter value) {
                        if(message.getFilePath().equals(key) && value instanceof WSO2IFrameEditor){
                            WSO2IFrameEditor editor = ((WSO2IFrameEditor)value);
                                    editor.saveContent(message.getContent());
                        }
                    }
                });
            }
        });

        filter.registerMessageRecipient(4, new MessageFilter.MessageRecipient<DirtyStateChangedMessage>() {
            @Override
            public void onMessageReceived(final DirtyStateChangedMessage message) {
                editorAgent.getOpenedEditors().iterate(new StringMap.IterationCallback<EditorPartPresenter>() {
                    @Override
                    public void onIteration(String key, EditorPartPresenter value) {
                        if(message.getFilePath().equals(key) && value instanceof WSO2IFrameEditor){
                            WSO2IFrameEditor editor = ((WSO2IFrameEditor)value);
                            editor.setDirtyState(message.isDirty());
                        }
                    }
                });
            }
        });

        Browser.getWindow().addEventListener("message", new EventListener() {
            @Override
            public void handleEvent(Event evt) {
                MessageEvent messageEvent = (MessageEvent)evt;
                MessageImpl message = (MessageImpl)messageEvent.getData();
                filter.dispatchMessage(message);
            }
        }, false);


    }
}
