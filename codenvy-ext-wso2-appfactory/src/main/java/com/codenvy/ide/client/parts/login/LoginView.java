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
package com.codenvy.ide.client.parts.login;

import javax.annotation.Nonnull;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.google.inject.ImplementedBy;

@ImplementedBy(LoginViewImpl.class)
public interface LoginView extends View<LoginView.ActionDelegate> {
    /** Required for delegating functions in view. */
    public interface ActionDelegate extends BaseActionDelegate {
        /** Performs some actions in response to a user's clicking on Button */
        void onOKButtonClicked();
        void onCancelButtonClicked();
        
        /** Performs some actions in response to a app cloud. */
        void onAppCloudChosen();

        /** Performs some actions in response to a app fac. */
        void onAppFacChosen();
        
          /** Performs any actions appropriate in response to the user having changed something. */
        void onValueChanged();
       
    }
    
    /** Show dialog. */
    void showDialog();
    
    /** @return <code>true</code> if app cloud is chosen, and <code>false</code> otherwise */
    boolean isAppCloud();

    /** @return <code>true</code> if a fac is chosen, and <code>false</code> otherwise */
    boolean isAppFac();

    /**
     * Select app cloud button.
     *
     * @param isAppCloud
     *         <code>true</code> to select app cloud, <code>false</code> not to select
     */
    void setAppCloud(boolean isAppCloud);

    /**
     * Select app fac button.
     *
     * @param isAppFac
     *         <code>true</code> to select app fac, <code>false</code> not to select
     */
    void setAppFac(boolean isAppFac);

     /** @return Wso2 App Cloud  */
    @Nonnull
    String getWso2AppCloud();

    /**
     * Set Wso2 Appcloud.
     *
     * @param App Cloud
     *         
     */
    void setWso2AppCloud(@Nonnull String AppCloud);
    
     /** @return Email */
    @Nonnull
    String getEmail();

    /**
     * Set Email.
     *
     * @param Email
     *         
     */
    void setEmail(@Nonnull String email);
     /** @return Wso2 App Cloud  */
    @Nonnull
    String getPassword();

    /**
     * Set Password .
     *
     * @param Password
     *         
     */
    void setPassword(@Nonnull String password);
    
    /** Close dialog. */
    void close();

    
}
