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
public class MavenSettingsPageViewImpl implements MavenSettingsPageView{

    private static MavenSettingsPageViewImplUiBinder uiBinder = GWT.create(MavenSettingsPageViewImplUiBinder.class);

    private final DockLayoutPanel rootElement;

    private ActionDelegate delegate;

    @UiField
    TextBox versionField;
    @UiField
    TextBox groupId;
    @UiField
    TextBox artifactId;
    @UiField
    ListBox packagingField;

    interface MavenSettingsPageViewImplUiBinder extends UiBinder<DockLayoutPanel, MavenSettingsPageViewImpl>{}

    public MavenSettingsPageViewImpl() {
        rootElement = uiBinder.createAndBindUi(this);
        packagingField.setSelectedIndex(0);
        packagingField.setEnabled(false);
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * This will be called by project wizard window.
     *
     * @return
     */
    @Override
    public Widget asWidget() {
        return rootElement;
    }

    @Override
    public void setArtifactId(String artifact) {
        this.artifactId.setText(artifact);
    }

    @Override
    public void setGroupId(String group) {
        this.groupId.setText(group);
    }

    @Override
    public void setVersion(String value) {
        this.versionField.setText(value);
    }

    @Override
    public String getPackaging() {
        return packagingField.getItemText(packagingField.getSelectedIndex());
    }

    @Override
    public String getGroupId() {
        return groupId.getText();
    }

    @Override
    public String getArtifactId() {
        return artifactId.getText();
    }

    @Override
    public String getVersion() {
        return versionField.getText();
    }

    /**
     * ActionListner for keyup event of text fields.
     *
     * @param event
     */
    @UiHandler({"versionField", "groupId", "artifactId"})
    void onKeyUp(KeyUpEvent event) {
        delegate.onTextChange();
    }

}
