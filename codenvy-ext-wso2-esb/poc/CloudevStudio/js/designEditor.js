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
var SWITCH_MEDIATOR_TYPE = "SwitchMediator";

 function selectDeleteFunction() {
    if (CurElement != null) {
        CurElement.removeClass('selected'); //deselect old
    }

    CurElement = $(this);
    id = $(this).attr('id');
    CurElement.addClass('selected'); //select new

function createDiv(objName, image, type, topLoc) {
    var xLoc = CurXLoc - leftOffset;
    var element = $("<div></div>");

    element.click(selectDeleteFunction);
    element.dblclick(openPopupDialog);
    setData(element, type);
    if (type == SWITCH_MEDIATOR_TYPE) {
        addSwitchMediator(element, objName, xLoc, image);
    } else {
        element.attr(ID, objName);
        element.append(image);
        $("#jsPlumbContainer").append(element);
        jsPlumb.draggable(objName, {
            containment: $("#jsPlumbContainer")
        });
        element.css({'top': topLoc, 'left': xLoc});
        element.addClass("wso2Mediator_style");
    }
}

//add div function
function AddDiv(logMediatorObj) {
    jsonStr = '{"log":' + JSON.stringify(logMediatorObj) + ' }'
    jsonObj1 = $.parseJSON(jsonStr);
    editorItemCounter++;
    var newElemCreated = DRAGGED_ELEM + editorItemCounter;
    var element = $("<div></div>");
    element.css({TOP: x, ICON_LEFT: xOffset + xSpace}); //dynamically positioning the elements,
    element.attr(ID, newElemCreated);
    element.addClass(DRAGGABLE);
    //loading element image resource on creating diagram from source, done only for log mediator, work in progress, needs to be changed when generalizing
    element.prepend('<img src="icons/log-mediator.gif" />')
    element.click(selectDeleteFunction);
    element.dblclick(openPopupDialog);
    element.data('jsonConfig', jsonObj1);
    element.addClass(MEDIATOR_STYLE);
    $("#jsPlumbContainer").append(element);
    lastItem = $("#" + newElemCreated);
    xSpace += xSpaceBuffer;
    return newElemCreated;
}

function addSwitchMediator(element, objName, leftLoc, image) {
    var backgroundContainer = $("#jsPlumbContainer");
    /*  designing the background area for delements with droppable area. for the switch mediator. This will be removed when introducing a
      common method for all mediators with droppable palletes inside - work in progress*/
    backgroundContainer.append('<div id=' + objName + ' style="height: 165px; width: ' + divWidth + 'px; background: #fff0f0;"></div>')
    $("#" + objName).append('<div id="jsPlumbContainer1" style=" height:100%; width:100%;"></div>')
    $("#jsPlumbContainer1").append('<table id="switchtableID" width="100%" height="100%"><table/>');
    $("#switchtableID").append('<tr><td  id="draggedSwitchMediatorin" rowspan="2" style="switchTableLeftTDStyle">Switch Mediator</td><td style="switchTableTDStyle"><div id="jsPlumbContainerWrapperTest" class="well-lg"  style="height:100%; width:100%; background: #ffffff;">Case</div></td></tr>');
    $("#switchtableID").append('<tr><td style="switchTableTDStyle"><div id="jsPlumbContainerWrapper12" class="well-lg"  style="height:100%; width:100%; background: #ffffff;">Default</div></td></tr>');
    $("#draggedSwitchMediatorin").append(image);
    element.attr('id', objName + "inside");
    $("#" + objName).addClass("wso2Mediator_style");
    $("#" + objName).draggable()
    $("#draggedSwitchMediatorin").append(element);
    $("#" + objName + "inside").position({
        my: "left center",
        at: "left center",
        of: "#draggedSwitchMediatorin"
    });

    //$("#jsPlumbContainerWrapper12").append(element);
    $("#" + objName).css({TOP: topLoc, ICON_LEFT: leftLoc});
}

//connect function with jsplumb, this is a jsplumb function for connecting to elements.
function connectDivs(source, target) {
    jsPlumb.connect({
        source: source,
        target: target,
        anchors: [RIGHT, LEFT ],
        paintStyle: { strokeStyle: CONNECTOR_LINE_COLOR, lineWidth: 1 },
        connector: [CONNECTOR_STYLE, { curviness: curvinessConstant}],
        connectorStyle: [
            { lineWidth: 1, strokeStyle: CONNECTOR_LINE_COLOR }
        ],
        hoverPaintStyle: { strokeStyle: CONNECTOR_LINE_COLOR, lineWidth: 1 }
    });
}

function jsplumbHandleDraggable() {
    $(".draggableIcon").draggable({
        helper: 'clone',
        containment: 'jsPlumbContainer',
        cursor: 'move',
        zIndex: zIndexConstant,
        //When first dragged
        stop: function (ev, ui) {

        }
    });
}

function jsplumbHandleDropable() {
    var newDraggedElem = null;
    var newDroppedElem = null;
    var newDroppedItem = null;

    $("#jsPlumbContainer").droppable({
        drop: function (ev, ui) {//to locate the element
            var yLoc = CurXLoc - leftOffset; //to get the current location in the div
            var currentConnectionList = jsPlumb.getAllConnections();
            for (var connection in currentConnectionList) {//getting a map of the existing elements in the canvas
                if (currentConnectionList.hasOwnProperty(connection)) {
                    elemSourceId[connection] = currentConnectionList[connection].sourceId;
                    elemSource = document.getElementById(elemSourceId[connection]);
                    elemSourceLocList[connection] = elemSource.offsetLeft;

                    elemTargetId[connection] = currentConnectionList[connection].targetId;
                    elemTarget = document.getElementById(elemTargetId[connection]);
                    elemTargetLocList[connection] = elemTarget.offsetLeft;
                }
            }
            if ($(ui.draggable).attr(ID).search(/dragged/) == -1) { // to check whether an elements is being dragged
                editorItemCounter++;
                var newDraggedElem = $(ui.draggable).clone();
                newDraggedElem.removeClass(DRAGGABLE_ICON);
                newDraggedElem.removeClass(DRAGGABLE_CLASS);
                var type = newDraggedElem.attr(ID);
                if (over == false) {
                    //getting the switch mediator background stuff created
                    newDroppedElem = DRAGGED + type + editorItemCounter;
                    createDiv(newDroppedElem, newDraggedElem, type, topLocation);

                    for (var elemInList in elemSourceLocList) {
                        if (elemSourceLocList.hasOwnProperty(elemInList)) {
                            if (yLoc > elemSourceLocList[elemInList] && yLoc < elemTargetLocList[elemInList]) {
                                jsPlumb.detach(currentConnectionList[elemInList]);
                                elemIsMiddle = true;
                                connectDivs(elemSourceId[elemInList], $("#" + newDroppedElem));
                                connectDivs($("#" + newDroppedElem), elemTargetId[elemInList]);
                            }
                        }
                    }
                    newDroppedItem = $("#" + newDroppedElem);
                    if (elemIsMiddle != true) {
                        if (lastItem == null) {
                            if (type == switchMedType) {
                                lastItem = newDroppedItem;
                            } else {
                                lastItem = newDroppedItem;
                            }
                        } else {
                            connectDivs(lastItem, newDroppedItem);
                        }
                        if (type != switchMedType) {
                            lastItem = newDroppedItem;
                        } else {
                            lastItem = newDroppedItem;
                        }
                    }

                } else {//to locate the element, this is done only for a single switch mediator, code will be removed in introducing for all mediators with droppable area inside
                    newElemXLoc += bufferConstant; // incrementing the dropped location dynamically.. the integer value needs to be tested for all browsers hence not yet finalized

                    $("#draggedSwitchMediator1").css(WIDTH, divWidth + newElemXLoc + PX); // naming is done dynamically, since we are working with only one switch mediator it is named as this
                    $("#jsPlumbContainer1").css(WIDTH, divWidth + newElemXLoc + PX);
                    $("#draggedSwitchMediator1").css(HEIGHT, "300px"); // switch mediator size is set in pixels, need to change after testing on all browsers and then moved to css
                    $("#draggedSwitchMediator1").css(WIDTH, "80px"); // switch mediator size is set in pixels, need to change after testing on all browsers and then moved to css
                    //getting the switch mediator background area created
                    if (type == switchMedType) {
                        $('jsPlumbContainerWrapper1').show();
                    }
                    newDroppedElem = DRAGGED + type + MEDIATOR_SWITCH + editorItemCounter;

                    createDiv(newDroppedElem, newDraggedElem, type, topLocation);
                    //trying to get from the map
                    for (var eleminList in elemSourceLocList) {

                        if (elemSourceLocList.hasOwnProperty(eleminList)) {

                            if (yLoc > elemSourceLocListIn[eleminList] && yLoc < elemTargetLocListIn[eleminList]) {
                                jsPlumb.detach(currentConnectionList1[eleminList]);
                                elemIsMiddle = true;
                                connectDivs(elemSourceId1[eleminList], $("#" + newDroppedElem));
                                connectDivs($("#" + newDroppedElem), elemTargetId1[eleminList]);
                            }
                        }
                    }
                    newDroppedItem = $("#" + newDroppedElem)
                    if (elemIsMiddle != true) {
                        if (lastItemInSwitch == null) {
                            lastItemInSwitch = newDroppedItem;
                        } else {
                            connectDivs(lastItemInSwitch, newDroppedItem);
                        }
                        lastItemInSwitch = newDroppedItem;
                    }
                }
            }
        },
        tolerance: TOLERANCE_POINTER
    });

}

