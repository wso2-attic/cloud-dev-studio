/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.client.parts.login.action;

import com.codenvy.api.user.gwt.client.UserServiceClient;
import com.codenvy.api.user.shared.dto.User;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.client.commons.WSO2AsyncRequestCallback;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;


import static com.codenvy.ide.api.notification.Notification.Type.INFO;


/**
 * The action for OAuth authorization on WSO2 AppFactory.
 *
 * @author Sohani
 */
public class LoginAction extends Action {
    private final String WSO2_VENDOR_NAME = "wso2";

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub
        
    }

}
