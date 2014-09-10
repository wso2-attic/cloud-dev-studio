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
package com.codenvy.ide.client.parts;

import com.codenvy.ide.api.parts.base.BasePresenter;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * The presenter that represent general part for showing part API.
 *
 * @author Sohani
 */
public class AppFacPartPresenterConsole extends BasePresenter implements AppFacPartView.ActionDelegate {
    private AppFacPartView view;
    private String     title;

    @Inject
    public AppFacPartPresenterConsole(AppFacPartView view, @Assisted String title) {
        this.view = view;
        this.view.setDelegate(this);
        this.view.setTitle(title);
        this.title = title;
    }

    /** {@inheritDoc} */
    @Override
    public void onButtonClicked() {
       
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    @Override
    public ImageResource getTitleImage() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getTitleToolTip() {
        return "Tooltip";
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

}
