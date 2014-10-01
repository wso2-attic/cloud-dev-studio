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
package org.wso2.developerstudio.codenvy.ext.appserver.client.ui.wizard.page.maven;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class MavenConfigurationPageViewImpl implements MavenConfigurationPageView {

    private static MavenSettingsPageViewImplUiBinder uiBinder = GWT.create(MavenSettingsPageViewImplUiBinder.class);

    private final DockLayoutPanel rootElement;
    @UiField
    TextBox versionField;
    @UiField
    TextBox groupId;
    @UiField
    TextBox artifactId;
    @UiField
    ListBox packagingField;

    private ActionDelegate delegate;

    public MavenConfigurationPageViewImpl() {
        rootElement = uiBinder.createAndBindUi(this);
        packagingField.setEnabled(false);
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public Widget asWidget() {
        return rootElement;
    }

    @Override
    public String getGroupId() {
        return groupId.getText();
    }

    @Override
    public void setGroupId(String group) {
        this.groupId.setText(group);
    }

    @Override
    public String getArtifactId() {
        return artifactId.getText();
    }

    @Override
    public void setArtifactId(String artifact) {
        this.artifactId.setText(artifact);
    }

    @Override
    public String getVersion() {
        return versionField.getText();
    }

    @Override
    public void setVersion(String value) {
        this.versionField.setText(value);
    }

    @Override
    public String getPackaging() {
        return packagingField.getValue(packagingField.getSelectedIndex());
    }

    @Override
    public void setPackaging(String value) {
        for (int i = 0; i < packagingField.getItemCount(); i++) {
            if (value.equals(packagingField.getValue(i))) {
                packagingField.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public void reset() {
        artifactId.setText("");
        groupId.setText("");
        versionField.setText("");
    }

    @UiHandler({"versionField", "groupId", "artifactId"})
    void onKeyUp(KeyUpEvent event) {
        delegate.onTextChange();
    }

    interface MavenSettingsPageViewImplUiBinder extends UiBinder<DockLayoutPanel, MavenConfigurationPageViewImpl> {
    }

}
