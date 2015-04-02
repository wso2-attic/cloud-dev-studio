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


#include <string>
#include <iostream>
#include <sstream>
#include <stdlib.h>
#include <stdio.h>
#include <cstdio>
#include <pthread.h>
#include <unistd.h>

#include "DevSCefBrowserEventHandler.h"
#include "include/cef_browser.h"
#include "include/cef_command_line.h"
#include "include/wrapper/cef_helpers.h"
#include "DevSCefClient_app.h"
#include "SystemUtils.h"
#include "Messages.h"
#include "DeveloperStudioProcess.h"

DeveloperStudioProcess *developerStudioProcess;

DevSCefClient::DevSCefClient() {
}

void DevSCefClient::OnContextInitialized() {
    CEF_REQUIRE_UI_THREAD();

    // Information used when creating the native window.
    CefWindowInfo window_info;
    window_info.height = SystemUtils::DEFAULT_WINDOW_HEIGHT;
    window_info.width = SystemUtils::DEFAULT_WINDOW_WIDTH;

    int width, height;
    if (SystemUtils::GetScreenSize(&width, &height) == 0) {
    	window_info.height = height;
    	window_info.width = width;
    }

    developerStudioProcess = new DeveloperStudioProcess;

     if (developerStudioProcess->StartProcess() == 0) {

        std::string operatingURL = developerStudioProcess->GetURLFromFile();
        // SimpleHandler implements browser-level call-backs.
        CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

        // Specify CEF browser settings here.
        CefBrowserSettings browser_settings;

        // Create the first browser window.
        CefBrowserHost::CreateBrowser(window_info, handler.get(), operatingURL, browser_settings, NULL);
     }
}
