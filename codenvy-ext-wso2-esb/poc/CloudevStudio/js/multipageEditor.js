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

var editorItemCounter = 0;
var currentId = null;
var count = 0;
var counter = 0;
var lastItem = null;
var dataString = null;
var curElement = null;
var id = null;
var over = "false";
var curSourceElem = null;
var curTargetElem = null;
var topLocation = 170; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var curXLoc = null;
var divWidth = 200; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var newElemXLoc = 60; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var topLoc = 120; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var curvinessConstant = 100; //for jsplumb arrow curviness 100 is the generally used value
var zIndexConstant = 1000; //for drag drop z index, constant is generally taken as 1000
var connectionList = null;
var elemSourceLocList = [];
var elemTargetLocList = [];
var elemSourceId = [];
var elemTargetId = [];
var elemSourceLocListIn = [];
var elemTargetLocListIn = [];
var elemSource = null;
var elemTarget = null;
var xSpace = 0; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var popupCount = 0;
var currentPopup = null;
var x2js = null;
var elemIsMiddle = false;
var popupHeight = 400; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var popupWidth =600; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var bufferConstant = 80; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var leftOffset = 400; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var xSpaceBuffer = 200; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet
var xOffset = 250; //in pixels.. all the number values given here needs to be tested and altered for all browsers and scenarios.. hence not finalised yet


$(document).ready(function () {
    x2js = new X2JS();
    registerTabChangeEvent();
    registerMouseAndKeyEvents();
    registerJsPlumbBind();
    jsplumbHandleDraggable();
    jsplumbHandleDroppable();
    connectionList = jsPlumb.getAllConnections();

});

//initiate jsplumb
function initJsPlumb(container) {
    jsPlumb.setContainer(container);
}

// data updating for mediator data
function setUpdatedDataCallBack(obj) {
    var strID = curElement.attr('id');
    var divMediator = document.getElementById(strID);
    $(divMediator).data('jsonConfig', obj);
    currentPopup.dialog("close");
}

//opening mediator data configuration dialog
function openMediatorConfigDialog(path, title) {
    if (popupCount == 0) {
        $(document.body).append('<div id="popupForMediatorData"></div>');
        $("#popupForMediatorData").attr('id', "popupForMediatorData");
        $("#popupForMediatorData").load(path);
        $("#popupForMediatorData").dialog({ autoOpen: false,
            bgiframe: true,
            height: popupHeight, //pop up widow height and width definitions
            width: popupWidth,
            modal: false,
            draggable: true,
            resizable: true,
            position: 'center' });
        $("#popupForMediatorData").dialog('option', 'title', title);
        currentPopup = $("#popupForMediatorData");
        ++popupCount;
    }
    currentPopup.dialog("open");
}

function registerMouseAndKeyEvents() {
    $(document).mousemove(function (e) {// to get the cursor point to drop an icon
        curXLoc = e.pageX;
    });

    $(document).keydown(function (e) {
        designViewKeyDown(e);
    });
}

//sourve view and design view tab changes
function registerTabChangeEvent() {
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        console.log('tabChagne');
        var tabName = $(e.target).html();
        if (tabName == 'Source') {
            activateSourceView();
        } else {
            activateDesignView();
        }
    });
}

//jsplumb binding to enable jsplumb in javascript
function registerJsPlumbBind() {
    jsPlumb.bind("ready", function () {
        initJsPlumb($("#jsPlumbContainer"));
    });
}

//activating source view
function activateSourceView() {
    var prevElement = null;
    var nextElement = null;
    var connectionList = jsPlumb.getAllConnections();
    var jObj = null;
    var xmlElement = null;
    var currentText = null;
    var sourceEditorTextBox = $('#sourceEditorTextBox');
    sourceEditorTextBox.val('<sequence name="sample_sequence">');

    for (var connection in connectionList) {
        if (connectionList.hasOwnProperty(connection)) {
            if (connectionList[connection].sourceId != null) {
                prevElement = document.getElementById(connectionList[connection].sourceId);
            }
            if (connectionList[connection].targetId != null) {
                nextElement = document.getElementById(connectionList[connection].targetId);
            }
        }

        jObj = $(prevElement).data('jsonConfig');
        //TODO remove console logs once finalized
        console.log(prevElement);
        console.log('serializing ' + jObj);
        console.log(jObj);
        xmlElement = '\n' + x2js.json2xml_str(jObj);
        currentText = sourceEditorTextBox.val();
        sourceEditorTextBox.val(currentText + xmlElement);
    }

    jObj = $(nextElement).data('jsonConfig');
    console.log('serializing ' + jObj);
    console.log(jObj);
    xmlElement = '\n' + x2js.json2xml_str(jObj);
    currentText = sourceEditorTBox.val();
    sourceEditorTBox.val(currentText + xmlElement + '\n</sequence>');
}

//activating design view
function activateDesignView() {
    var sourceEditorTextBox = $('#sourceEditorTextBox');
    var jsPlumbCont = $("#jsPlumbContainer");

    console.log('activateDesignView');
    var sequenceObj = x2js.xml_str2json(sourceEditorTextBox.val());
    var sequence = sequenceObj.sequence;
    var logArray = sequence.log;
    console.log(logArray);

    jsPlumbCont.empty();
    var prevDivElement = null;
    var logArrayElem;
    for (logArrayElem in logArray) {
        if (logArray.hasOwnProperty(logArrayElem)) {
            console.log(logArrayElem);
            var currentDiv = AddDiv(logArrayElem);
            if (prevDivElement != null) {
                connectDivs(prevDivElement, currentDiv);
                lastItem = currentDiv;
            }
            prevDivElement = currentDiv;
            }
    }

}