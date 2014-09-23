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
package org.wso2.developerstudio.codenvy.ext.esb.client;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.wso2.developerstudio.cloud.core.shared.CoreExtConstants;
import org.wso2.developerstudio.codenvy.ext.esb.client.esb.ESBGraphicalEditorPresenter;
import org.wso2.developerstudio.codenvy.ext.esb.client.inject.ESBJavaScriptInjector;
import org.wso2.developerstudio.codenvy.ext.esb.shared.ESBExtConstants;

@Singleton
@Extension(title =  CoreExtConstants.EXT_NAME_PREFIX + ESBExtConstants.EXT_NAME,
                    version = ESBExtConstants.EXT_VERSION)
public class ESBExtension {

    @Inject
    public ESBExtension(WorkspaceAgent workspaceAgent,
                        ESBGraphicalEditorPresenter graphicalEditor,
                        ESBExtensionResources bundle) {

        ESBJavaScriptInjector.inject(bundle.jqueryLib().getText());
        ESBJavaScriptInjector.inject(bundle.jqueryUILib().getText());
        ESBJavaScriptInjector.inject(bundle.jsPlumbLib().getText());
        ESBJavaScriptInjector.inject(bundle.esbExtensionJS().getText());
    }
}
