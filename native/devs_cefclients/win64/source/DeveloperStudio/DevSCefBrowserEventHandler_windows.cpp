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

#include "DeveloperStudio/DevSCefBrowserEventHandler.h"

#include <string>
#include <windows.h>
#include <shlobj.h> 

#include "resource.h"
#include "include/cef_browser.h"
#include "include/wrapper/cef_helpers.h"

void DevSCefBrowserEventHandler::OnTitleChange(CefRefPtr<CefBrowser> browser, const CefString& title) {
		CEF_REQUIRE_UI_THREAD();

		CefWindowHandle hwnd = browser->GetHost()->GetWindowHandle();
		LPCWSTR devStudioTitle = L"WSO2 Developer Studio ";
		SetWindowText(hwnd, devStudioTitle); 
}

void DevSCefBrowserEventHandler::SendNotification(NotificationType type) {
	UINT id;
	switch (type) {
	case NOTIFY_CONSOLE_MESSAGE:
		id = ID_WARN_CONSOLEMESSAGE;
		break;
	case NOTIFY_DOWNLOAD_COMPLETE:
		id = ID_WARN_DOWNLOADCOMPLETE;
		break;
	case NOTIFY_DOWNLOAD_ERROR:
		id = ID_WARN_DOWNLOADERROR;
		break;
	default:
		return;
	}
	PostMessage(main_handle_, WM_COMMAND, id, 0);
}


std::string DevSCefBrowserEventHandler::GetDownloadPath(const std::string& file_name) {
	TCHAR szFolderPath[MAX_PATH];
	std::string path;

	// Save the file in the user's "My Documents" folder.
	if (SUCCEEDED(SHGetFolderPath(NULL, CSIDL_PERSONAL | CSIDL_FLAG_CREATE, NULL, 0, szFolderPath))) {
		path = CefString(szFolderPath);
		path += "\\" + file_name;
	}

	return path;
}
