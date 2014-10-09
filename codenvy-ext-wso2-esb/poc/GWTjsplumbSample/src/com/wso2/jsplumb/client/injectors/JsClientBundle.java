/* Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package com.wso2.jsplumb.client.injectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public  interface JsClientBundle extends ClientBundle {
	
    JsClientBundle INSTANCE = GWT.create(JsClientBundle.class);
    //Generate resources accessible to GWT from external resources
    @Source("com/wso2/jsplumb/client/scripts/jquery.min.js")
    TextResource jquerysource();

    @Source("com/wso2/jsplumb/client/scripts/jquery.ui.min.js")
    TextResource jqueryuimin();

    @Source("com/wso2/jsplumb/client/scripts/jquery.jsPlumb-1.6.2-min.js")
    TextResource jsplumbsource();

    @Source("com/wso2/jsplumb/client/scripts/gwtjsplumbdemo.js")
    TextResource gwtresource();

    @Source("icons/Call.gif")
    ImageResource CallImage();

    @Source("icons/CallTemplate.gif")
    ImageResource CalleTempImage();

    @Source("icons/Log.gif")
    ImageResource LogImage();

    @Source("icons/Drop.gif")
    ImageResource DropImage();

    @Source("icons/Clone.gif")
    ImageResource CloneImage();

    @Source("icons/Respond.gif")
    ImageResource RespondImage();

    @Source("icons/Property.gif")
    ImageResource PropertyImage();

    @Source("icons/PayloadFactory.gif")
    ImageResource PayloadFactoryImage();

    @Source("icons/Throttle.gif")
    ImageResource ThrottleImage();

    @Source("icons/Send.gif")
    ImageResource SendImage();

    @Source("icons/Store.gif")
    ImageResource StoreImage();

}
