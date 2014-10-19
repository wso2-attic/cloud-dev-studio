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

package org.wso2.ESBEditor.client.Controllers;

import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.codenvy.ide.client.inject.JSBundle;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import  com.codenvy.ide.client.Editor.ESBEditor;


import com.allen_sauer.gwt.dnd.client.DragContext;

import java.lang.String;

public class NoInsertAtEndIndexedDropController extends SimpleDropController {

    public static final String DRAGGABLE_PANEL = "draggablePanel";
    public static final String DROPPABLE_PANEL = "droppablePanel";
    public static final String CALL_MEDIATOR = "callMediator";
    public static final String CALL_TEMPLATE_MEDIATOR = "callTemplateMediator";
    public static final String LOG_MEDIATOR = "logMediator";
    public static final String DROP_MEDIATOR = "dropMediator";
    public static final String STORE_MEDIATOR = "storeMediator";
    public static final String SEND_MEDIATOR = "sendMediator";
    public static final String CLONE_MEDIATOR = "cloneMediator";
    public static final String THROTTLE_MEDIATOR = "throttleMediator";
    public static final String RESPOND_MEDIATOR = "respondMediator";
    public static final String PROPERTY_MEDIATOR = "propertyMediator";
    public static final String PAYLFAC_MEDIATOR = "paylfacMediator";
    public static final String DRAGGED = "dragged";
    public static final String DRAGGABLE = "draggable";
    public static final String DRAGGED_ELEM_STYLE = "gwt-Image dragdrop-draggable dragdrop-handle dragdrop-dragging";
    public static final String DRAGGABLE_ELEM_STYLE = "gwt-Image dragdrop-draggable dragdrop-handle";
    public static final int INCREMENT_CONSTANT = 100;
    static MouseMoveEvent mouseEvent;
    static int mouseX =0;
    static int mouseY=0;

    private ImageResource DropCallImage = JSBundle.INSTANCE.CallImage();
    private ImageResource DropCallTempImage = JSBundle.INSTANCE
            .CalleTempImage();
    private ImageResource DropLogImage = JSBundle.INSTANCE.LogImage();
    private ImageResource DropDropImage = JSBundle.INSTANCE.DropImage();
    private ImageResource DropStoreImage = JSBundle.INSTANCE.StoreImage();
    private ImageResource DropThrottleImage = JSBundle.INSTANCE
            .ThrottleImage();
    private ImageResource DropSendImage = JSBundle.INSTANCE.SendImage();
    private ImageResource DropPayloadFactoryImage = JSBundle.INSTANCE
            .PayloadFactoryImage();
    private ImageResource DropRespondImage = JSBundle.INSTANCE
            .RespondImage();
    private ImageResource DropCloneImage = JSBundle.INSTANCE.CloneImage();
    private ImageResource DropPropertyImage = JSBundle.INSTANCE
            .PropertyImage();

    int ElementCount = 0;
    int xLoc = 50; //positioning integer value needs to be tested on all browsers and reset
    int yLoc = 200; //ipositioning integer value needs to be tested on all browsers and reset

    public NoInsertAtEndIndexedDropController(Widget dropTarget) {
        super(dropTarget);
    }

    @Override
    public void onDrop(DragContext context) {
        ElementCount++;
        xLoc = xLoc + INCREMENT_CONSTANT; //incrementing the next drop integer value needs to be tested on all browsers and reset, is += more efficient?
        for (Widget widget : context.selectedWidgets) {
            Image newDroppedElem = new Image();
            newDroppedElem.getElement().setId(DRAGGED + ElementCount);
            newDroppedElem.getElement().setPropertyBoolean(DRAGGABLE, false);
            newDroppedElem.addClickHandler(ESBEditor.clickHandler);
            widget.removeStyleName(DRAGGED_ELEM_STYLE);
            widget.addStyleName(DRAGGABLE_ELEM_STYLE);
            RootPanel.get(DRAGGABLE_PANEL).add(widget);
            RootPanel.get(DROPPABLE_PANEL).remove(widget);

            String thisId = widget.getElement().getId();

            if (thisId.equalsIgnoreCase(CALL_MEDIATOR)) {
                newDroppedElem.setResource(DropCallImage);
            }
            if (thisId.equalsIgnoreCase(CALL_TEMPLATE_MEDIATOR)) {
                newDroppedElem.setResource(DropCallTempImage);
            }
            if (thisId.equalsIgnoreCase(LOG_MEDIATOR)) {
                newDroppedElem.setResource(DropLogImage);
            }
            if (thisId.equalsIgnoreCase(DROP_MEDIATOR)) {
                newDroppedElem.setResource(DropDropImage);
            }
            if (thisId.equalsIgnoreCase(STORE_MEDIATOR)) {
                newDroppedElem.setResource(DropStoreImage);
            }
            if (thisId.equalsIgnoreCase(SEND_MEDIATOR)) {
                newDroppedElem.setResource(DropSendImage);
            }
            if (thisId.equalsIgnoreCase(CLONE_MEDIATOR)) {
                newDroppedElem.setResource(DropCloneImage);
            }
            if (thisId.equalsIgnoreCase(THROTTLE_MEDIATOR)) {
                newDroppedElem.setResource(DropThrottleImage);
            }
            if (thisId.equalsIgnoreCase(RESPOND_MEDIATOR)) {
                newDroppedElem.setResource(DropRespondImage);
            }
            if (thisId.equalsIgnoreCase(PROPERTY_MEDIATOR)) {
                newDroppedElem.setResource(DropPropertyImage);
            }
            if (thisId.equalsIgnoreCase(PAYLFAC_MEDIATOR)) {
                newDroppedElem.setResource(DropPayloadFactoryImage);
            }

            RootPanel.get(DROPPABLE_PANEL).add(newDroppedElem);
            RootPanel.get(DROPPABLE_PANEL).setWidgetPosition(newDroppedElem, xLoc, yLoc);
            newDroppedElem = null;
            RootPanel.get(DROPPABLE_PANEL).getAbsoluteLeft();
        }
        super.onDrop(context);
    }

    @Override
    public void onEnter(DragContext context) {
        super.onEnter(context);
        for (Widget widget : context.selectedWidgets) {
        }
    }

    @Override
    public void onLeave(DragContext context) {
        for (Widget widget : context.selectedWidgets) {
        }
        super.onLeave(context);

    }

    public static void setMouseEvent(MouseMoveEvent e){
        mouseEvent = e;
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();

    }

}