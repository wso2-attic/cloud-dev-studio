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

package org.wso2.jsplumb.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.wso2.jsplumb.client.injectors.JsClientBundle;

public class MediatorCreator {

	private static final String DRAGGABLE = "draggable"; 
	private static final String MEDIATOR = "Mediator"; 

	public static ImageResource getImage(Mediator mediator) {
		
		ImageResource imageResource = null;
		//creating the image resource for the new widget 
		switch (mediator) {
		case LOG:
			imageResource = JsClientBundle.INSTANCE.LogImage();
			break;

		case CALL:
			imageResource = JsClientBundle.INSTANCE.CallImage();
			break;

		case PAYLOADFACTORY:
			imageResource = JsClientBundle.INSTANCE.PayloadFactoryImage();
			break;

		case SEND:
			imageResource = JsClientBundle.INSTANCE.SendImage();
			break;

		case RESPOND:
			imageResource = JsClientBundle.INSTANCE.RespondImage();
			break;

		case PROPERTY:
			imageResource = JsClientBundle.INSTANCE.PropertyImage();
			break;

		case CALLTEMPLATE:
			imageResource = JsClientBundle.INSTANCE.CalleTempImage();
			break;

		case THROTTLE:
			imageResource = JsClientBundle.INSTANCE.ThrottleImage();
			break;

		case STORE:
			imageResource = JsClientBundle.INSTANCE.StoreImage();
			break;

		case CLONE:
			imageResource = JsClientBundle.INSTANCE.CloneImage();
			break;

		case DROP:
			imageResource = JsClientBundle.INSTANCE.DropImage();
			break;

		default:
			imageResource = JsClientBundle.INSTANCE.LogImage();// insert a default image for this
			break;
		}

		return imageResource;
	}

	public static Image getMediatorByName(Mediator mediator, ClickHandler clickHandler) {
		Image cloneimage = getMediatorByName(mediator);
		cloneimage.addClickHandler(clickHandler);
		cloneimage.getElement().setPropertyBoolean(DRAGGABLE, false);
		return cloneimage;
	}

	public static Image getMediatorByName(Mediator mediator) {
		Image cloneimage = new Image();
		ImageResource image = getImage(mediator);
		cloneimage.getElement().setId(mediator.toString().toLowerCase() + MEDIATOR);
		cloneimage.setResource(image);
		return cloneimage;
	}

}
