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
package com.codenvy.ide.client.inject;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
* Extend the {@link ClientBundle} to provide Javascript
* resource linkage.
*
*/
public interface JSBundle extends ClientBundle {

@Source("esb-extension.js")
TextResource esbExtensionJS();

@Source("jquery.min.js")
TextResource jqueryLib();

@Source("jquery.ui.min.js")
TextResource jqueryUILib();

@Source("jquery.jsPlumb-1.6.2-min.js")
TextResource jsPlumbLib();

}
