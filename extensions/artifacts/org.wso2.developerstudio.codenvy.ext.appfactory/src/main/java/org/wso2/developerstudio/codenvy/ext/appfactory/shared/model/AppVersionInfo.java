/*
* Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.codenvy.ext.appfactory.shared.model;

import java.io.Serializable;

/**
 * A bean class keeps Application version related information
 */
public interface AppVersionInfo extends Serializable {

    public String getIsAutoDeploy();

    public void setIsAutoDeploy(String isAutoDeploy);

    public String getRepoURL();

    public void setRepoURL(String repoURL);

    public String getVersion();

    public void setVersion(String version);

    public String getStage();

    public void setStage(String stage);

    public String getIsAutoBuild();

    public void setIsAutoBuild(String isAutoBuild);

    public String getLastBuildResult();

    public void setLastBuildResult(String lastBuildResult);

    public String getAppName();

    public void setAppName(String appName);

    public String getLocalRepo();

    public void setLocalRepo(String localRepo);

    public boolean isCheckedOut();

    public void setCheckedOut(boolean isCheckedOut);

    public boolean isBuildRequestCancel();

    public void setBuildRequestCancel(boolean isBuildRequestCancel);

    public boolean isAForkedRepo();

    public void setAForkedRepo(boolean isAForkedRepo);

    public AppVersionGroup getVersionGroup();

    public void setVersionGroup(AppVersionGroup versionGroup);
}
