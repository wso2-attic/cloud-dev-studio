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
package org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto;

public class AFLoginResponse {

    private boolean isLoggedIn;
    private boolean isErroneousRequest;
    private String errorMessage;

    public AFLoginResponse(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public AFLoginResponse(boolean isLoggedIn, boolean isErroneousRequest, String errorMessage) {
        this.isLoggedIn = isLoggedIn;
        this.isErroneousRequest = isErroneousRequest;
        this.errorMessage = errorMessage;
    }

    public boolean isErroneousRequest() {
        return isErroneousRequest;
    }

    public void setErroneousRequest(boolean isErronouesRequest) {
        this.isErroneousRequest = isErronouesRequest;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
