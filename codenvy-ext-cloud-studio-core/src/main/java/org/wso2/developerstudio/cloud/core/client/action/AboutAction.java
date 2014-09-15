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
package org.wso2.developerstudio.cloud.core.client.action;

/**
 * As usual, importing resources, related to Action API.
 * The 3rd import is required to call a default alert box.
 */
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import org.wso2.developerstudio.cloud.core.client.ui.About;
import com.google.inject.Inject;
import org.wso2.developerstudio.cloud.core.shared.WSO2StudioUIConstants;


public class AboutAction extends Action
{
    /**
     * Define a constructor and pass over text to be displayed in the dialogue box
     */

    @Inject
    public AboutAction() {
        super(WSO2StudioUIConstants.WSO2_ABOUT_ACTION_NAME);
    }

    /**
     * Define the action required when calling this method. In our case it'll open a dialogue box with 'Hello world'
     */

    @Override
    public void actionPerformed(ActionEvent arg0) {
        
        new About().show();
              
    }
   
}
