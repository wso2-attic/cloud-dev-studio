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

#include <sstream>
#include <string>
#include <iostream>
#include <stdio.h>
#include <signal.h>

#include "DevSCefBrowserEventHandler.h"
#include "include/base/cef_bind.h"
#include "include/cef_app.h"
#include "include/wrapper/cef_closure_task.h"
#include "include/wrapper/cef_helpers.h"
#include "Messages.h"
#include "SystemUtils.h"
#include "DeveloperStudioProcess.h"

namespace {
    DevSCefBrowserEventHandler* g_instance = NULL;
}

DevSCefBrowserEventHandler::DevSCefBrowserEventHandler()
: is_closing_(false) {
    DCHECK(!g_instance);
    g_instance = this;
}

DevSCefBrowserEventHandler::~DevSCefBrowserEventHandler() {
    g_instance = NULL;
}

DevSCefBrowserEventHandler* DevSCefBrowserEventHandler::GetInstance() {
    return g_instance;
}

void DevSCefBrowserEventHandler::OnAfterCreated(CefRefPtr<CefBrowser> browser) {
    CEF_REQUIRE_UI_THREAD();

    // Add to the list of existing browsers.
    browser_list_.push_back(browser);
}

bool DevSCefBrowserEventHandler::DoClose(CefRefPtr<CefBrowser> browser) {
    CEF_REQUIRE_UI_THREAD();

    DeveloperStudioProcess::StopProcess();

    // Closing the main window requires special handling. See the DoClose()
    // documentation in the CEF header for a detailed description of this
    // process.
    if (browser_list_.size() == 1) {
        // Set a flag to indicate that the window close should be allowed.
        is_closing_ = true;
    }


    // Allow the close. For windowed browsers this will result in the OS close
    // event being sent.
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

void DevSCefBrowserEventHandler::CloseAllBrowsers(bool force_close) {
    if (!CefCurrentlyOn(TID_UI)) {
        // Execute on the UI thread.
        CefPostTask(TID_UI,
                base::Bind(&DevSCefBrowserEventHandler::CloseAllBrowsers, 
                            this, force_close));
        return;
    }

    if (browser_list_.empty())
        return;

    BrowserList::const_iterator it = browser_list_.begin();
    for (; it != browser_list_.end(); ++it)
        (*it)->GetHost()->CloseBrowser(force_close);
}


