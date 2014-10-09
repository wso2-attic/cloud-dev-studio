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

package com.codenvy.ide.client.Editor;

import java.util.HashMap;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.codenvy.ide.api.editor.AbstractEditorPresenter;
import com.codenvy.ide.client.inject.JSBundle;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import com.codenvy.ide.api.resources.model.File;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ESBEditor extends AbstractEditorPresenter 
{

    public static final String DRAGGABLE_PANEL = "draggablePanel";
    public static final String DROPPABLE_PANEL = "droppablePanel";
    public static final String WINDOW = "window";
    public static final String POSITION = "position";
    public static final String RELATIVE = "relative";
    public static final String CALL_MEDIATOR = "callMediator";
    public static final String DROP_MEDIATOR = "dropMediator";
    public static final String CALL_TEMPLATE_MEDIATOR = "callTemplateMediator";
    public static final String LOG_MEDIATOR = "logMediator";
    public static final String STORE_MEDIATOR = "storeMediator";
    public static final String SEND_MEDIATOR = "sendMediator";
    public static final String THROTTLE_MEDIATOR = "throttleMediator";
    public static final String PAYLFAC_MEDIATOR = "paylfacMediator";
    public static final String RESPOND_MEDIATOR = "respondMediator";
    public static final String CLONE_MEDIATOR = "cloneMediator";
    public static final String PROPERTY_MEDIATOR = "propertyMediator";
    public static final int DROPPABLE_PANEL_WIDTH = 1350; // numerical values needs to be alrtered to a dynamic value after testing on all browsers
    public static final int DROPPABLE_PANEL_HEIGHT = 1000; // numerical values needs to be alrtered to a dynamic value after testing after testing on all browsers

    private ImageResource CallImage = JSBundle.INSTANCE.CallImage();
    private ImageResource CallTempImage = JSBundle.INSTANCE
            .CalleTempImage();
    private ImageResource LogImage = JSBundle.INSTANCE.LogImage();
    private ImageResource DropImage = JSBundle.INSTANCE.DropImage();
    private ImageResource StoreImage = JSBundle.INSTANCE.StoreImage();
    private ImageResource ThrottleImage = JSBundle.INSTANCE
            .ThrottleImage();
    private ImageResource SendImage = JSBundle.INSTANCE.SendImage();
    private ImageResource PayloadFactoryImage = JSBundle.INSTANCE
            .PayloadFactoryImage();
    private ImageResource RespondImage = JSBundle.INSTANCE.RespondImage();
    private ImageResource CloneImage = JSBundle.INSTANCE.CloneImage();
    private ImageResource PropertyImage = JSBundle.INSTANCE
            .PropertyImage();
    
    static  VerticalPanel backgroundPanel;
    public static DockLayoutPanel dockPanel ;

    static VerticalPanel draggablePanel = new VerticalPanel();
    static HorizontalPanel droppablePanel = new HorizontalPanel();
    static RootPanel rootPanel;
    static Widget selectedWidget = new Widget();

    private Image callimage = new Image();
    private Image dropimage = new Image();
    private Image calltempimage = new Image();
    private Image logimage = new Image();
    private Image storeimage = new Image();
    private Image throttleimage = new Image();
    private Image sendimage = new Image();
    private Image payloadfactoryimage = new Image();
    private Image respondimage = new Image();
    private Image cloneimage = new Image();
    private Image propertyimage = new Image();
    
    public ESBEditor(){  
    
    }
    @Override
    public void activate() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doSave() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doSaveAs() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImageResource getTitleImage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitleToolTip() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void go(AcceptsOneWidget container) {

        dockPanel = new DockLayoutPanel(Style.Unit.PX);

        draggablePanel.getElement().setId(DRAGGABLE_PANEL);
        droppablePanel.getElement().setId(DROPPABLE_PANEL);

        draggablePanel.getElement().setClassName(WINDOW);
        droppablePanel.getElement().setClassName(WINDOW);
        droppablePanel.setPixelSize(DROPPABLE_PANEL_WIDTH, DROPPABLE_PANEL_HEIGHT);
        droppablePanel.getElement().getStyle().setProperty(POSITION, RELATIVE);
        draggablePanel.getElement().getStyle().setProperty(POSITION, RELATIVE);

        callimage.getElement().setId(CALL_MEDIATOR); // need to optimize using enum, still work on progress
        callimage.setResource(CallImage);
        callimage.addClickHandler(clickHandler);
        draggablePanel.add(callimage);

        dropimage.getElement().setId(DROP_MEDIATOR);
        dropimage.setResource(DropImage);
        dropimage.addClickHandler(clickHandler);
        draggablePanel.add(dropimage);

        calltempimage.getElement().setId(CALL_TEMPLATE_MEDIATOR);
        calltempimage.setResource(CallTempImage);
        calltempimage.addClickHandler(clickHandler);
        draggablePanel.add(calltempimage);

        logimage.getElement().setId(LOG_MEDIATOR);
        logimage.setResource(LogImage);
        logimage.addClickHandler(clickHandler);
        draggablePanel.add(logimage);

        storeimage.getElement().setId(STORE_MEDIATOR);
        storeimage.setResource(StoreImage);
        storeimage.addClickHandler(clickHandler);
        draggablePanel.add(storeimage);

        sendimage.getElement().setId(SEND_MEDIATOR);
        sendimage.setResource(SendImage);
        sendimage.addClickHandler(clickHandler);
        draggablePanel.add(sendimage);

        throttleimage.getElement().setId(THROTTLE_MEDIATOR);
        throttleimage.setResource(ThrottleImage);
        throttleimage.addClickHandler(clickHandler);
        draggablePanel.add(throttleimage);

        payloadfactoryimage.getElement().setId(PAYLFAC_MEDIATOR);
        payloadfactoryimage.setResource(PayloadFactoryImage);
        payloadfactoryimage.addClickHandler(clickHandler);
        draggablePanel.add(payloadfactoryimage);

        respondimage.getElement().setId(RESPOND_MEDIATOR);
        respondimage.setResource(RespondImage);
        respondimage.addClickHandler(clickHandler);
        draggablePanel.add(respondimage);

        cloneimage.getElement().setId(CLONE_MEDIATOR);
        cloneimage.setResource(CloneImage);
        cloneimage.addClickHandler(clickHandler);
        draggablePanel.add(cloneimage);

        propertyimage.getElement().setId(PROPERTY_MEDIATOR);
        propertyimage.setResource(PropertyImage);
        propertyimage.addClickHandler(clickHandler);
        draggablePanel.add(propertyimage);

        dockPanel.add(draggablePanel);
        dockPanel.add(droppablePanel);
        RootPanel.get().add(dockPanel);
        container.setWidget(dockPanel);
        
    }

    @Override
    protected void initializeEditor() {
      backgroundPanel = new VerticalPanel();

        //use or load content of the file
        if(input.getFile().getContent() == null){
            input.getFile().getProject().getContent(input.getFile(), new AsyncCallback<File>() {
                @Override
                public void onFailure(Throwable caught) {
                }
                @Override
                public void onSuccess(File result) {
                   backgroundPanel.add(rootPanel);
                }
            });
        }
        else{
        }
    }
    
    public static final ClickHandler clickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            event.preventDefault();
            selectedWidget = (Widget) event.getSource();
        }
    };

    public static native void gwtjsPlumbDemo(String prevElem, String currElem, //JSNI Jqeury codes written in block comments, practice
                                             int ElemCount) /*-{
        if (ElemCount > 1) {
            $wnd.gwtjsplumbdemo(prevElem, currElem);
        }
    }-*/;
}