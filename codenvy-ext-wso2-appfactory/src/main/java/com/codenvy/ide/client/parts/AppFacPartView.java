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
package com.codenvy.ide.client.parts;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.google.inject.ImplementedBy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Element;


@ImplementedBy(AppFacPartViewImpl.class)
public interface AppFacPartView extends View<AppFacPartView.ActionDelegate> {
    /** Required for delegating functions in view. */
    public interface ActionDelegate extends BaseActionDelegate {
        /** Performs some actions in response to a user's clicking on Button */
        void onButtonClicked();
        
        
    }

    /**
     * Set title of my part.
     *
     * @param title
     *         title that need to be set
     */
    void setTitle(String title);
   
  
}
