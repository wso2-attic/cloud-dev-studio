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
var DRAGGABLE_CLASS = "ui-draggable"; //declaring constants
var DRAGGABLE_ICON = "draggableIcon";
var DRAGGED = "dragged";
var DRAGGED_ELEM = "draggedElem";
var DRAGGABLE = "draggable";
var MEDIATOR_STYLE = "wso2Mediator_style";
var CONNECTOR_LINE_COLOR = "#3366FF";
var CONNECTOR_STYLE = "Flowchart";
var ID = 'id';
var HEIGHT = "height";
var WIDTH = "width";
var RIGHT = "Right";
var LEFT = "Left";
var TOP = 'top';
var ICON_LEFT = 'left';
var PX = "px"; // used when assigning pixel values for elements
var TOLERANCE_POINTER = "pointer";
var MEDIATOR_SWITCH = "switch"; //currently only done for a switch mediator needs to pass the constants to a separate file when all mediators are being done

var CURVINESS_CONSTANT = 100;
var CONNECTOR_STYLE_VAL =  { lineWidth: 1, strokeStyle: CONNECTOR_LINE_COLOR }
var HOVER_PAINT_STYLE =  { strokeStyle: CONNECTOR_LINE_COLOR, lineWidth: 1 };
var PAINT_STYLE =  { strokeStyle: CONNECTOR_LINE_COLOR, lineWidth: 1 };
var Z_INDEX_CONSTANT = 1000; //for drag drop z index, constant is generally taken as 1000

//selecting the elements, currently used only for enabling deleting on select
 function selectDeleteFunction() {
    if (curElement != null) {
        curElement.removeClass('selected'); //deselect old
    }
    curElement = $(this);
    id = $(this).attr('id');
    curElement.addClass('selected'); //select new
 }

//deleting elements on select and delete key press
function designViewDeleteKeyDown(e) {
    connectionList = jsPlumb.getAllConnections();
    if (curElement != null) {
        for (var connection in connectionList) {
            if (connectionList.hasOwnProperty(connection)) {
                if (connectionList[connection].sourceId == curElement.attr('id')) {
                    curSourceElem = connectionList[connection].targetId
                }
                if (connectionList[connection].targetId == curElement.attr('id')) {
                    curTargetElem = connectionList[connection].sourceId
                }
            }
        }

        if (curElement.attr('id') == lastItem.attr('id')) {
            lastItem = $("#" + curTargetElem);
        }
        jsPlumb.detachAllConnections(id);
        curElement.remove();

        if (curTargetElem != null && curSourceElem != null) { // if the deleted item was a middle item in the sequence, connect its source and target
            connectDivs(curTargetElem, curSourceElem);
            curSourceElem = null;
            curTargetElem = null;
        }
        curElement = null; //clear, that element doesn't exist anymore
    }
}

//creating a div on drop of a mediator to the droppable area
function createDiv(objName, image, type) {
    var xLoc = curXLoc - LEFT_OFFSET;
    var element = $("<div></div>");
    element.click(selectDeleteFunction);
    element.dblclick(openPopupDialog);
    setData(element, type);
    element.attr(ID, objName);
    element.append(image);
    $("#jsPlumbContainer").append(element);
    jsPlumb.draggable(objName, {
        containment: $("#jsPlumbContainer")
    });
    element.css({'top': TOP_LOC, 'left': xLoc});
    element.addClass("wso2Mediator_style");
}

//add div function for source to design view change, only done for log mediator yet
function addDiv(logMediatorObj) {
var jsPlumbContainerCanvas= $("#jsPlumbContainer")
    jsonObj = {
        log : logMediatorObj
    };
    editorItemCounter++;
    var newElemCreated = DRAGGED_ELEM + editorItemCounter;
    var element = $("<div></div>");
    element.css({'top': 250, 'left': X_OFFSET + xSpace});
    //element.css({TOP: 200, ICON_LEFT: X_OFFSET + xSpace}); //dynamically positioning the elements,
    element.attr(ID, newElemCreated);
    element.addClass(DRAGGABLE);
    /*loading element image resource on creating diagram from source, done only for log mediator, work in progress,
      needs to be changed when generalizing*/
    element.prepend('<img src="icons/log-mediator.gif" />')
    element.click(selectDeleteFunction);
    element.dblclick(openPopupDialog);
    element.data('jsonConfig', jsonObj);
    element.addClass(MEDIATOR_STYLE);
    jsPlumbContainerCanvas.append(element);
    xSpace += X_SPACE_BUFFER;
    return newElemCreated;
}
//connect function with jsplumb, this is a jsplumb function for connecting to elements.
function connectDivs(source, target) {
    jsPlumb.connect({
        source: source,
        target: target,
        anchors: [RIGHT, LEFT ],
        paintStyle: PAINT_STYLE,
        connector: [CONNECTOR_STYLE, { curviness: CURVINESS_CONSTANT}],
        connectorStyle: [CONNECTOR_STYLE_VAL],
        hoverPaintStyle: HOVER_PAINT_STYLE
    });
}

function jsplumbHandleDraggable() {

    $(".draggableIcon").draggable({
        helper: 'clone',
        containment: 'jsPlumbContainer',
        cursor: 'move',
        zIndex: Z_INDEX_CONSTANT,
        //When first dragged
        stop: function (ev, ui) { }
    });
}

function jsplumbHandleDroppable() {
    var newDraggedElem = null;
    var newDroppedElem = null;
    var newDroppedItem = null;

    $("#jsPlumbContainer").droppable({
        drop: function (ev, ui) {//to locate the element
            var xLoc = curXLoc - LEFT_OFFSET; //to get the current location in the div
            if(lastItem != null){
            var connectionList = jsPlumb.getAllConnections();
                for (var connection in connectionList) {//getting a map of the existing elements in the canvas
                    if (connectionList.hasOwnProperty(connection)) {
                        elemSourceId[connection] = connectionList[connection].sourceId;
                        elemSource = document.getElementById(elemSourceId[connection]);
                        elemSourceLocList[connection] = elemSource.offsetLeft;

                        elemTargetId[connection] = connectionList[connection].targetId;
                        elemTarget = document.getElementById(elemTargetId[connection]);
                        elemTargetLocList[connection] = elemTarget.offsetLeft;
                    }
                }
            }
            if ($(ui.draggable).attr(ID).search(/dragged/) == -1) { // to check whether an elements is being dragged
                editorItemCounter++;
                var newDraggedElem = $(ui.draggable).clone();
                newDraggedElem.removeClass(DRAGGABLE_ICON);
                newDraggedElem.removeClass(DRAGGABLE_CLASS);
                var type = newDraggedElem.attr(ID);

                newDroppedElem = DRAGGED + type + editorItemCounter;
                createDiv(newDroppedElem, newDraggedElem, type);
                if(elemSourceLocList != null){
                    for (var elemInList in elemSourceLocList) {
                        if (elemSourceLocList.hasOwnProperty(elemInList)) {
                            if (xLoc > elemSourceLocList[elemInList] && xLoc < elemTargetLocList[elemInList]) {
                                jsPlumb.detach(connectionList[elemInList]);
                                elemIsMiddle = true;
                                connectDivs(elemSourceId[elemInList], $("#" + newDroppedElem));
                                connectDivs($("#" + newDroppedElem), elemTargetId[elemInList]);
                            }
                        }
                    }
                }
                newDroppedItem = $("#" + newDroppedElem);
                if (!elemIsMiddle) {
                   if(lastItem != null){
                    connectDivs(lastItem, newDroppedItem);
                    }
                    lastItem = newDroppedItem;
                }
                elemIsMiddle = false;
            }
        },
        tolerance: TOLERANCE_POINTER
    });

}

