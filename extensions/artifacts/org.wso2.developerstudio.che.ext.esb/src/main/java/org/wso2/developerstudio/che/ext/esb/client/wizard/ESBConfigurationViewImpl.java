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
package org.wso2.developerstudio.che.ext.esb.client.wizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class ESBConfigurationViewImpl extends Composite implements ESBConfigurationView {


    interface ESBConfigurationViewImplUiBinder
            extends UiBinder<Widget, ESBConfigurationViewImpl> {

    }

    private static ESBConfigurationViewImplUiBinder ourUiBinder = GWT.create(ESBConfigurationViewImplUiBinder.class);

    private ActionDelegate delegate;
    @UiField
    TextBox groupId;
    @UiField
    TextBox artifactId;
    @UiField
    TextBox version;


    @Inject
    public ESBConfigurationViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {

        this.delegate = delegate;
    }

    @Override
    public String getGroupId() {
        return groupId.getText();
    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId.setText(groupId);
    }


    @Override
    public String getArtifactId() {
        return artifactId.getText();
    }

    @Override
    public void setArtifactId(String artifactId) {
        this.artifactId.setText(artifactId);
    }


    @Override
    public String getVersion() {
        return version.getText();
    }

    @Override
    public void setVersion(String version) {
        this.version.setText(version);
    }


    @UiHandler("groupId")
    public void onGroupIdChanged(KeyUpEvent event) {
        delegate.onGroupIdChanged();
    }

    @UiHandler("artifactId")
    public void onArtifactIdChanged(KeyUpEvent event) {
        delegate.onArtifactIdChanged();
    }

    @UiHandler("version")
    public void onVersionChanged(KeyUpEvent event) {
        delegate.onVersionChanged();
    }

}