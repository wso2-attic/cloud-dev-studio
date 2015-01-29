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
package org.wso2.developerstudio.che.ext.esb.server.inject;

import com.codenvy.api.project.server.ProjectGenerator;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.wso2.developerstudio.che.ext.esb.server.genarator.ESBProjectGenerator;
import org.wso2.developerstudio.che.ext.esb.server.project.type.ESBProjectTypeExtension;
import org.wso2.developerstudio.che.ext.esb.server.project.type.ESBProjectTypeDescriptionExtension;
import com.codenvy.inject.DynaModule;


@DynaModule
public class ESBGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ESBProjectTypeExtension.class);
        bind(ESBProjectTypeDescriptionExtension.class);
        Multibinder.newSetBinder(binder(), ProjectGenerator.class).addBinding().to(ESBProjectGenerator.class);
    }
}
