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

package org.wso2.jsplumb.client.injectors;

import com.google.gwt.core.client.ScriptInjector;

public class ScriptInjectorHelper {
	 //inject all external javascript files to the GWT project
	  public static void injectScript() {
	        ScriptInjector
	                .fromString(JsClientBundle.INSTANCE.jquerysource().getText())
	                .setWindow(ScriptInjector.TOP_WINDOW).inject();
	        ScriptInjector
	                .fromString(JsClientBundle.INSTANCE.jqueryuimin().getText())
	                .setWindow(ScriptInjector.TOP_WINDOW).inject();
	        ScriptInjector
	                .fromString(JsClientBundle.INSTANCE.jsplumbsource().getText())
	                .setWindow(ScriptInjector.TOP_WINDOW).inject();
	        ScriptInjector
	                .fromString(JsClientBundle.INSTANCE.gwtresource().getText())
	                .setWindow(ScriptInjector.TOP_WINDOW).inject();	       

	    }
}
