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
package org.wso2.developerstudio.codenvy.ext.appserver.shared;

import com.codenvy.ide.api.ui.wizard.WizardContext;

public interface Constants {

    String WSO2_PROJECT_ID = "WSO2Project";
    String WSO2_REGISTRY_PROJECT = "WSO2 Registry Resources Project";
    String WSO2_REGISTRY_PROJECT_ID = "WSo2 Registry Project";

    String LANGUAGE = "language";
    String LANGUAGE_VERSION = "language.version";
    String FRAMEWORK = "framework";
    String BUILDER_NAME = "builder.name";
    String BUILDER_MAVEN_SOURCE_FOLDERS = "builder.maven.source_folders";
    String RUNNER_NAME                  = "runner.name";
    String MAVEN_ARTIFACT_ID = "-mvn-artifact-id";
    String MAVEN_GROUP_ID = "mvn-group-id";
    String MAVEN_VERSION = "mvn-version";
    String MAVEN_PACKAGING = "mvn-packaging";

    // Keys for wizard context
    WizardContext.Key<String> WKEY_MAVEN_ARTIFACT_ID = new WizardContext.Key<String>(Constants.MAVEN_ARTIFACT_ID);
    WizardContext.Key<String> WKEY_MAVEN_GROUP_ID = new WizardContext.Key<String>(Constants.MAVEN_GROUP_ID);
    WizardContext.Key<String> WKEY_MAVEN_VERSION= new WizardContext.Key<String>(Constants.MAVEN_VERSION);
}
