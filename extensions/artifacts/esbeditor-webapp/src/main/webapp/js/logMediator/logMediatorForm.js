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


  $(document).ready(function() {
    var logMediatorElement =  window.parent.curElement.data('jsonConfig');
    var logMediator = logMediatorElement.log;
    $("#mediator_log_log_separator").val(logMediator.seperator);
    $('#mediator_log_category').prop("selectedIndex", logMediator._category);
    $('#mediator_log_log_level').prop("selectedIndex", logMediator._logLevel);
    $("#propertyName").val(logMediator.description);


    $("#mybutton").click(function() {

        var propertyName = $('#propertyName').val();
        var category = $('#mediator_log_category :selected').val();
        var logLevel =  $('#mediator_log_log_level :selected').val();
        var separator = $("#mediator_log_log_separator").val();
        var logMediatorElement =  window.parent.curElement.data('jsonConfig');
        var logMediatorData = logMediatorElement.log;

        logMediatorData._logLevel = logLevel;
        logMediatorData._category = category;
        logMediatorData.description = propertyName;
        logMediatorData.seperator = separator;

        logMediatorElement.log = logMediatorData;
        window.parent.setUpdatedDataCallBack(logMediatorElement);

    });

});