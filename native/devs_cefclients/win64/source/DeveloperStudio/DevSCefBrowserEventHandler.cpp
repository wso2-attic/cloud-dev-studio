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

#include <string>
#include "DeveloperStudioProcess.h"
#include "DeveloperStudio/DevSCefBrowserEventHandler.h"
#include <sstream>
#include <signal.h>

#include "include/base/cef_bind.h"
#include "include/cef_app.h"
#include "include/wrapper/cef_closure_task.h"
#include "include/wrapper/cef_helpers.h"
#include "SystemUtils.h"


extern DeveloperStudioProcess *devsProcess;

HWND GetRootHwnd(CefRefPtr<CefBrowser> browser) {
  return ::GetAncestor(browser->GetHost()->GetWindowHandle(), GA_ROOT);
}

// Toggles the current display state.
void Toggle(HWND root_hwnd, UINT nCmdShow) {
  // Retrieve current window placement information.
  WINDOWPLACEMENT placement;
  ::GetWindowPlacement(root_hwnd, &placement);

  if (placement.showCmd == nCmdShow)
    ::ShowWindow(root_hwnd, SW_RESTORE);
  else
    ::ShowWindow(root_hwnd, nCmdShow);
}

void Maximize(CefRefPtr<CefBrowser> browser) {
  Toggle(GetRootHwnd(browser), SW_MAXIMIZE);
}

namespace {
	DevSCefBrowserEventHandler* g_instance = NULL;
}  // namespace

DevSCefBrowserEventHandler::DevSCefBrowserEventHandler()
	: is_closing_(false),
	main_handle_(NULL) {
		DCHECK(!g_instance);
		g_instance = this;
}

DevSCefBrowserEventHandler::~DevSCefBrowserEventHandler() {
	g_instance = NULL;
}

void DevSCefBrowserEventHandler::SetMainWindowHandle(ClientWindowHandle handle) {
	if (!CefCurrentlyOn(TID_UI)) {
		// Execute on the UI thread.
		CefPostTask(TID_UI,
			base::Bind(&DevSCefBrowserEventHandler::SetMainWindowHandle, this, handle));
		return;
	}

	main_handle_ = handle;
}

ClientWindowHandle DevSCefBrowserEventHandler::GetMainWindowHandle() const {
	CEF_REQUIRE_UI_THREAD();
	return main_handle_;
}

// static
DevSCefBrowserEventHandler* DevSCefBrowserEventHandler::GetInstance() {
	return g_instance;
}

void DevSCefBrowserEventHandler::OnAfterCreated(CefRefPtr<CefBrowser> browser) {
	CEF_REQUIRE_UI_THREAD();

	// Add to the list of existing browsers.
	browser_list_.push_back(browser);
	Maximize(browser);
}

bool DevSCefBrowserEventHandler::DoClose(CefRefPtr<CefBrowser> browser) {
	CEF_REQUIRE_UI_THREAD();

	// Closing the main window requires special handling. See the DoClose()
	// documentation in the CEF header for a detailed destription of this
	// process.
	if (browser_list_.size() == 1) {
		// Set a flag to indicate that the window close should be allowed.
		is_closing_ = true;
	}

	//int res = TerminateServerProcess((DWORD)serverPID);
	int res = devsProcess->Stop();
	delete devsProcess;
	devsProcess = NULL;
	return false;
}



void DevSCefBrowserEventHandler::OnBeforeClose(CefRefPtr<CefBrowser> browser) {
	CEF_REQUIRE_UI_THREAD();

	// Remove from the list of existing browsers.
	BrowserList::iterator bit = browser_list_.begin();
	for (; bit != browser_list_.end(); ++bit) {
		if ((*bit)->IsSame(browser)) {
			browser_list_.erase(bit);
			break;
		}
	}

	if (browser_list_.empty()) {
		// All browser windows have closed. Quit the application message loop.
		CefQuitMessageLoop();
	}
}

void DevSCefBrowserEventHandler::OnLoadError(CefRefPtr<CefBrowser> browser,
	CefRefPtr<CefFrame> frame,
	ErrorCode errorCode,
	const CefString& errorText,
	const CefString& failedUrl) {
		CEF_REQUIRE_UI_THREAD();

		// Don't display an error for downloaded files.
		if (errorCode == ERR_ABORTED)
			return;

		// Display a load error message.
		std::stringstream ss;
		ss << "<html><body bgcolor=\"white\">"
			"<h2>Failed to load URL " << std::string(failedUrl) <<
			" with error " << std::string(errorText) << " (" << errorCode <<
			").</h2></body></html>";
		frame->LoadString(ss.str(), failedUrl);
}


void DevSCefBrowserEventHandler::OnBeforeDownload(
	CefRefPtr<CefBrowser> browser,
	CefRefPtr<CefDownloadItem> download_item,
	const CefString& suggested_name,
	CefRefPtr<CefBeforeDownloadCallback> callback) {
		
	CEF_REQUIRE_UI_THREAD();
	// Continue the download and show the "Save As" dialog.
	callback->Continue(GetDownloadPath(suggested_name), true);
}

void DevSCefBrowserEventHandler::SetLastDownloadFile(const std::string& fileName) {
	CEF_REQUIRE_UI_THREAD();
	last_download_file_ = fileName;
}

std::string DevSCefBrowserEventHandler::GetLastDownloadFile() const {
	CEF_REQUIRE_UI_THREAD();
	return last_download_file_;
}

void DevSCefBrowserEventHandler::OnDownloadUpdated(
	CefRefPtr<CefBrowser> browser,
	CefRefPtr<CefDownloadItem> download_item,
	CefRefPtr<CefDownloadItemCallback> callback) {
		
		CEF_REQUIRE_UI_THREAD();
		if (download_item->IsComplete()) {
			SetLastDownloadFile(download_item->GetFullPath());
			SendNotification(NOTIFY_DOWNLOAD_COMPLETE);
		}
}


void DevSCefBrowserEventHandler::CloseAllBrowsers(bool force_close) {

	if (!CefCurrentlyOn(TID_UI)) {
		// Execute on the UI thread.
		CefPostTask(TID_UI, base::Bind(&DevSCefBrowserEventHandler::CloseAllBrowsers, this, force_close));
		return;
	}

	if (browser_list_.empty()) {
		return;
	}

	BrowserList::const_iterator it = browser_list_.begin();
	for (; it != browser_list_.end(); ++it) {
		(*it)->GetHost()->CloseBrowser(force_close);
	}
}
