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

#include "DevSCefBrowserEventHandler.h"

#include <X11/Xatom.h>
#include <X11/Xlib.h>
#include <string>

#include "include/cef_browser.h"
#include "include/wrapper/cef_helpers.h"

void DevSCefBrowserEventHandler::OnTitleChange(CefRefPtr<CefBrowser> browser,
        const CefString& title) {
    CEF_REQUIRE_UI_THREAD();
    std::string titleStr(title);

    // Retrieve the X11 display shared with Chromium.
    ::Display* display = cef_get_xdisplay();
    DCHECK(display);

    // Retrieve the X11 window handle for the browser.
    ::Window window = browser->GetHost()->GetWindowHandle();
    DCHECK(window != kNullWindowHandle);

    // Retrieve the atoms required by the below XChangeProperty call.
    const char* kAtoms[] = {
        "_NET_WM_NAME",
        "UTF8_STRING"
    };
    Atom atoms[2];
    int result = XInternAtoms(display, const_cast<char**> (kAtoms), 2, false,
            atoms);
    if (!result)
        NOTREACHED();

    // Set the window title.
    XChangeProperty(display,
            window,
            atoms[0],
            atoms[1],
            8,
            PropModeReplace,
            reinterpret_cast<const unsigned char*> (titleStr.c_str()),
            titleStr.size());

    // TODO(erg): This is technically wrong. So XStoreName and friends expect
    // this in Host Portable Character Encoding instead of UTF-8, which I believe
    // is Compound Text. This shouldn't matter 90% of the time since this is the
    // fallback to the UTF8 property above.
    XStoreName(display, browser->GetHost()->GetWindowHandle(), titleStr.c_str());
}

