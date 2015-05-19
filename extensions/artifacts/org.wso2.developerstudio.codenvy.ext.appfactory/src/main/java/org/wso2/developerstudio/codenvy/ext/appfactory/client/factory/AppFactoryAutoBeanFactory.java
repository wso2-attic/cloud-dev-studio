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
package org.wso2.developerstudio.codenvy.ext.appfactory.client.factory;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppDBInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppInfoList;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppUserInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppVersionGroup;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.AppVersionInfo;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.model.DataSource;

/**
 * A factory class for defining AutoBean JSON framework supported bean types
 */
public interface AppFactoryAutoBeanFactory extends AutoBeanFactory {
    AutoBean<AppInfoList> appInfoList();

    AutoBean<AppInfo> appInfo();

    AutoBean<AppDBInfo> appDBInfo();

    AutoBean<AppUserInfo> appUserInfo();

    AutoBean<AppVersionInfo> appVersionInfo();

    AutoBean<AppVersionGroup> appVersionGroupInfo();

    AutoBean<DataSource> dataSourceInfo();
}
