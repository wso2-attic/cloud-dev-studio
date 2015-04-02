/*
* Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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


#include "DeveloperStudio/DevSCefClient_app.h"


#include <string>
#include <windows.h>
#include <stdio.h>
#include <process.h>
#include <direct.h>
#include <stdlib.h>

#include "SystemUtils.h"
#include "Messages.h"
#include "DeveloperStudioProcess.h"
#include "DeveloperStudio/DevSCefBrowserEventHandler.h"
#include "include/cef_browser.h"
#include "include/cef_command_line.h"
#include "include/wrapper/cef_helpers.h"


DeveloperStudioProcess *devsProcess;

DevSCefClient::DevSCefClient() {
}

void DevSCefClient::OnContextInitialized() {
	CEF_REQUIRE_UI_THREAD();

	// Information used when creating the native window.
	CefWindowInfo window_info;

#if defined(OS_WIN)
	// On Windows we need to specify certain flags that will be passed to
	// CreateWindowEx().

	window_info.SetAsPopup(NULL, "WSO2 Developer Studio");
#endif

	devsProcess = new DeveloperStudioProcess();
	devsProcess->Start();
	std::string url = devsProcess->GetUrl();

	if (!url.empty()) {

		// SimpleHandler implements browser-level callbacks.
		CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

		// Specify CEF browser settings here.
		CefBrowserSettings browser_settings;

		// Create the first browser window.
		CefBrowserHost::CreateBrowser(window_info, handler.get(), url, browser_settings, NULL);
	} else {
		HANDLE currentProcessHandle = GetCurrentProcess();
		TerminateProcess(currentProcessHandle, 0);
	}

}
