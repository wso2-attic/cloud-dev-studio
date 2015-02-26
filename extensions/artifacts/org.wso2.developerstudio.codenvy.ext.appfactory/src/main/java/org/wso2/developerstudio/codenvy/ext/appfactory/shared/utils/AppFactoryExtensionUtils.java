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
package org.wso2.developerstudio.codenvy.ext.appfactory.shared.utils;

import org.apache.commons.validator.routines.UrlValidator;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.AppFactoryExtensionConstants;

/**
 * Class for keeping static utility methods used in App Factory extension module
 */
public class AppFactoryExtensionUtils {

    /**
     * Method checks given URL is a valid representation for a host URL
     *
     * @param url host URL to check the validity
     * @return <code>true</code> if valid, else <code>false</code>
     */
    public static boolean isValidHostUrl(String url) {
        if (url.contains(AppFactoryExtensionConstants.COLON_SEPARATOR) && !url.startsWith(AppFactoryExtensionConstants.COLON_SEPARATOR)) {
            if (url.startsWith(AppFactoryExtensionConstants.URL_PREFIX_HTTP) ||
                    url.startsWith(AppFactoryExtensionConstants.URL_PREFIX_HTTPS) ||
                    url.startsWith(AppFactoryExtensionConstants.URL_PREFIX_FTP)) {
                UrlValidator urlValidator = new UrlValidator(8L);
                return urlValidator.isValid(url);
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

}
