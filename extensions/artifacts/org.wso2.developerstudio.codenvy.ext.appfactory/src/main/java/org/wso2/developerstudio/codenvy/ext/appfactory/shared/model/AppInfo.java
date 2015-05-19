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
import java.util.List;

/**
 * A bean class keeps Application information
 */
public interface AppInfo extends Serializable {

    public String getKey();

    public void setKey(String key);

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public String getRepositoryType();

    public void setRepositoryType(String repositoryType);

    public String getLocalRepoLocation();

    public void setLocalRepoLocation(String localRepoLocation);

    public String getLocalForkRepoLocation();

    public void setLocalForkRepoLocation(String localForkRepoLocation);

    public String getType();

    public void setType(String type);

    public int getLabelState();

    public void setLabelState(int labelState);

    public long getRevision();

    public void setRevision(long revision);

    public String getApplicationOwner();

    public void setApplicationOwner(String applicationOwner);

    public List<AppUserInfo> getApplicationDevelopers();

    public void setApplicationDevelopers(List<AppUserInfo> applicationDevelopers);

    public List<DataSource> getDataSources();

    public void setDataSources(List<DataSource> dataSources);

    public List<AppDBInfo> getDatabases();

    public void setDatabases(List<AppDBInfo> databases);

    public List<String> getApis();

    public void setApis(List<String> apis);

    public List<String> getProperties() ;

    public void setProperties(List<String> properties);

    public List<AppVersionInfo> getVersion();

    public void setVersion(List<AppVersionInfo> version);

    public List<AppVersionInfo> getForkedVersions();

    public void setForkedVersions(List<AppVersionInfo> forkedVersions);

    public boolean isLoaded();

    public void setLoaded(boolean loaded);
}
