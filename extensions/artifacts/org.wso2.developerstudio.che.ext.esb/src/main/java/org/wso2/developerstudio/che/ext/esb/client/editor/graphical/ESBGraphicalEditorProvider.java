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

import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.editor.EditorProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.annotation.Nonnull;

@Singleton
public class ESBGraphicalEditorProvider implements EditorProvider {

    private final Provider<ESBGraphicalEditor> provider;

    @Inject
    public ESBGraphicalEditorProvider(Provider<ESBGraphicalEditor> provider) {
        this.provider = provider;
    }

    @Override
    public String getId() {
        return "wso2ESBEditor";
    }

    @Override
    public String getDescription() {
        return "Editor for wso2 esb proxy file";
    }

    @Nonnull
    @Override
    public EditorPartPresenter getEditor() {
        return provider.get();
    }
}
