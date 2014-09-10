/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.client.parts.login;

import javax.annotation.Nonnull;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.google.inject.ImplementedBy;

/**
 * The view of {@link LoginPresenter}.
 *
 * @author Sohani
 */
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
