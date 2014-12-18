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

#include "DevSCefClient_app.h"

#include <X11/Xlib.h>
#include <iostream>

#include <sstream>
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

#include "include/base/cef_logging.h"

namespace {
    int XErrorHandlerImpl(Display *display, XErrorEvent *event) {
        LOG(WARNING)
                << "X error received: "
                << "type " << event->type << ", "
                << "serial " << event->serial << ", "
                << "error_code " << static_cast<int> (event->error_code) << ", "
                << "request_code " << static_cast<int> (event->request_code) << ", "
                << "minor_code " << static_cast<int> (event->minor_code);
        return 0;
    }

    int XIOErrorHandlerImpl(Display *display) {
        return 0;
    }
}


// Entry point function for all processes.
int main(int argc, char* argv[]) {

    CefMainArgs main_args(argc, argv);

    // SimpleApp implements application-level call-backs. It will create the first
    // browser instance in OnContextInitialized() after CEF has initialized.
    CefRefPtr<DevSCefClient> app(new DevSCefClient);

    // CEF applications have multiple sub-processes (render, plug-in, GPU, etc)
    // that share the same executable. This function checks the command-line and,
    // if this is a sub-process, executes the appropriate logic.
    int exit_code = CefExecuteProcess(main_args, app.get(), NULL);
    if (exit_code >= 0) {
        // The sub-process has completed so return here.
        return exit_code;
    }

    // Specify CEF global settings here.
    CefSettings settings;
    settings.no_sandbox = 1;

    // Install xlib error handlers so that the application won't be terminated
    // on non-fatal errors.
    XSetErrorHandler(XErrorHandlerImpl);
    XSetIOErrorHandler(XIOErrorHandlerImpl);

    // Initialize CEF for the browser process.
    CefInitialize(main_args, settings, app.get(), NULL);

    // Run the CEF message loop. This will block until CefQuitMessageLoop() is
    // called.
    CefRunMessageLoop();

    // Shut down CEF.
    CefShutdown();

    return 0;
}
