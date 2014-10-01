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

import com.codenvy.ide.api.mvp.View;
import com.google.inject.ImplementedBy;

@ImplementedBy(MavenConfigurationPageViewImpl.class)
public interface MavenConfigurationPageView extends View<MavenConfigurationPageView.ActionDelegate> {

    public interface ActionDelegate{
        void onTextChange();
    }

    void setArtifactId(String artifact);

    void setGroupId(String group);

    void setVersion(String value);

    void setPackaging(String value);

    String getGroupId();

    String getArtifactId();

    String getVersion();

    String getPackaging();

    void reset();
}
