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

package org.wso2.jsplumb.client.controllers;

import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wso2.jsplumb.client.GWTjsplumbSample;
import com.wso2.jsplumb.client.Mediator;
import com.wso2.jsplumb.client.MediatorCreator;

public class CustomImageElementDropControler extends SimpleDropController {

	private static final int xCoordinateIncrement = 100;
	private static final String DROPPABLE_PANEL = "droppablePanel";
	private static final String DRAGGABLE_PANEL = "draggablePanel";
	private static final String PAYLFAC_MEDIATOR = "paylfacMediator";
	private static final String PROPERTY_MEDIATOR = "propertyMediator";
	private static final String RESPOND_MEDIATOR = "respondMediator";
	private static final String THROTTLE_MEDIATOR = "throttleMediator";
	private static final String CLONE_MEDIATOR = "cloneMediator";
	private static final String SEND_MEDIATOR = "sendMediator";
	private static final String STORE_MEDIATOR = "storeMediator";
	private static final String DROP_MEDIATOR = "dropMediator";
	private static final String LOG_MEDIATOR = "logMediator";
	private static final String CALL_TEMPLATE_MEDIATOR = "callTemplateMediator";
	private static final String CALL_MEDIATOR = "callMediator";
	private static final String BACKGROUND = "background";
	private static final String DRAGGED = "dragged";

	private static final String DROPPABLE_IMAGE_STYLE = "gwt-Image dragdrop-draggable dragdrop-handle";
	private static final String DROPPING_IMAGE_STYLE = "gwt-Image dragdrop-draggable dragdrop-handle dragdrop-dragging";	

	private int elementCount = 0; // count of dropped elements in the droppable panel
	private int droppedElemxCoord = 100; //coordinates for the dropped element
	private int droppedElemyCoord = 50;

	private Image newDroppedElem;	
	private static Widget selectedWidget;
	private Map<Integer, String> widgetMap;	

	public CustomImageElementDropControler(Widget dropTarget, EntryPoint newEntrypoint) {
		super(dropTarget);
		widgetMap = new HashMap<Integer, String>();
	}

	@Override
	public void onDrop(DragContext context) { // on widget dropping 		
		for (Widget widget : context.selectedWidgets) {
			if (widget != null) {
				String droppedElemId = widget.getElement().getId();
				String newDroppedElemId = DRAGGED + droppedElemId + elementCount;
				// removing the GWT dropping widget styles and add Droppable styles and move it to droppable panel					
				widget.getElement().removeClassName(DROPPING_IMAGE_STYLE);
				widget.getElement().addClassName(DROPPABLE_IMAGE_STYLE);
				
				if (RootPanel.get(DRAGGABLE_PANEL) != null
						&& RootPanel.get(DROPPABLE_PANEL) != null) {//(mediator.toString().toLowerCase() + MEDIATOR);
					
					RootPanel.get(DRAGGABLE_PANEL).add(widget); // add a clone to the draggable panel
					RootPanel.get(DROPPABLE_PANEL).remove(widget); // remove the default dropped element to the droppable panel

					if (droppedElemId.equalsIgnoreCase(CALL_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.CALL,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(CALL_TEMPLATE_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.CALLTEMPLATE,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(LOG_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.LOG,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(DROP_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.DROP,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(STORE_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.STORE,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(SEND_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.SEND,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(CLONE_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.CLONE,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(THROTTLE_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.THROTTLE,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(RESPOND_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.RESPOND,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(PROPERTY_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.PROPERTY,
								clickHandler);
					}
					else if (droppedElemId.equalsIgnoreCase(PAYLFAC_MEDIATOR)) {
						newDroppedElem = MediatorCreator.getMediatorByName(Mediator.PAYLOADFACTORY,
								clickHandler);						
					}
					newDroppedElem.getElement().setId(newDroppedElemId);
					RootPanel.get(DROPPABLE_PANEL).add(newDroppedElem);					
					RootPanel.get(DROPPABLE_PANEL).setWidgetPosition(newDroppedElem,
							droppedElemxCoord, droppedElemyCoord);
					//generate a map of the dropped elements
					widgetMap.put(elementCount, newDroppedElem.getElement().getId());
					newDroppedElem = null; 
					RootPanel.get(BACKGROUND).getAbsoluteLeft();
				}
			}
		}
		//to connect the widgets on drop with the previously dropped widget
		int PrevCount = elementCount - 1;
		String prevElem = widgetMap.get(PrevCount);
		String currElem = widgetMap.get(elementCount);
		GWTjsplumbSample.gwtjsPlumbDemo(prevElem, currElem, elementCount);
		elementCount++;

		droppedElemyCoord += xCoordinateIncrement; // increment the dropping coordinates of the dropped elements by 100px
		super.onDrop(context);
	}

	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
	}

	@Override
	public void onLeave(DragContext context) {
		super.onLeave(context);
	}

	public static final ClickHandler clickHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			event.preventDefault();
			selectedWidget = (Widget) event.getSource();
			GWTjsplumbSample.echo(selectedWidget.getElement().getId());
		}
	};
}
