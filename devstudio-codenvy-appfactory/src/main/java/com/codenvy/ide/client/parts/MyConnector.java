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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;


public class MyConnector implements EntryPoint {

public void onModuleLoad() {

AbsolutePanel panel = new AbsolutePanel();
panel.setSize("200px", "120px");
panel.addStyleName("demo-panel");
Label label = new Label("Label");
label.setWidth("100px");
label.setStyleName("demo-label");
panel.add(label, 50, 50);
RootPanel.get("demo").add(panel);
    
}

}