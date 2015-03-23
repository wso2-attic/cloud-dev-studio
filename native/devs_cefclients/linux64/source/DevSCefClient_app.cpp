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



int serverPORT;


DevSCefClient::DevSCefClient() {
}

void *ExecuteCheServerInBackground(void *args_ptr) {

    std::string server_launch_cmd = SystemUtils::APPLICATION_BASE_PATH + SystemUtils::WSO2STUDIO_CHE_SH_AND;
    int server_startup_status = system(server_launch_cmd.c_str());
        if (server_startup_status == 0) {
            std::cout << Messages::SERVER_STARTED << std::endl;
        } else {
            std::cerr << Messages::SERVER_STARTUP_ERROR << server_startup_status << std::endl;
        }
    return NULL;
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


  char *actualpath;
  actualpath = realpath("../", NULL);

  if (actualpath != NULL) {

   		std::string path(actualpath);
        SystemUtils::APPLICATION_BASE_PATH = path;
  
        // starting the workspace selector and splash screen
        std::string workspace_selector_cmd = path + SystemUtils::WSO2STUDIO_WORKSPACE;
        std::string bash_s = SystemUtils::BIN_BASH;

        char workspace_selector_cmd_arr[1024];
        strncpy(workspace_selector_cmd_arr, workspace_selector_cmd.c_str(), sizeof (workspace_selector_cmd_arr));

        char bash[1024];
        strncpy(bash, bash_s.c_str(), sizeof (bash));
        std::string command_s = "-c";

        char command[1024];
        strncpy(command, command_s.c_str(), sizeof (command));
        char *name[] = {bash,  command, workspace_selector_cmd_arr, NULL};

        int pid = fork();
            if (pid == 0) {
                int workspace_selector_status = execvp(bash_s.c_str(), name);
                if (workspace_selector_status == 0) {
                    std::cout << Messages::WORKSPACE_SELECTOR_STARTED << std::endl;
                } else {
                    std::cerr << Messages::SERVER_STARTUP_ERROR << workspace_selector_status << std::endl;
                }
            }
        // waiting the sever startup
        std::string url;
        std::string sever_port;
        std::string urlpath = path + SystemUtils::BIN_URL_TXT;

        std::string portpath = path + SystemUtils::BIN_PORT;

        //wait for the port file, accommodate for the close button in workspace selector
            while (true) {
                std::FILE *portFile = std::fopen(portpath.c_str(), "rb");
                if (portFile) {
                    std::fclose(portFile);
                    break;
                } else {
                    sleep(2);
                }
            }

        sever_port = SystemUtils::GetFileContents(portpath.c_str());
        serverPORT = atoi(sever_port.c_str());
        int port_file_remove_status = std::remove(portpath.c_str());
            if (port_file_remove_status != 0) {
                std::cerr << Messages::ERROR_IN_FILE_DELETE << port_file_remove_status << std::endl;
            }
            if (serverPORT < 0) {
                //TODO find a way to exit the cef
                return;
            }
        pthread_t che_thread;
        int server_args = 0;

            if (pthread_create(&che_thread, NULL, ExecuteCheServerInBackground, (void *)&server_args)) {
                std::cout << Messages::ERROR_CREATING_SERVER_THREAD << stderr << std::endl;
            }

        //check for the url file
            while (true) {
                std::FILE *url_file = std::fopen(urlpath.c_str(), "rb");
                if (url_file) {
                    std::fclose(url_file);
                    break;
                } else {
                    std::cout << Messages::WAITING_FOR_URL;
                    sleep(2);
                }
            }

        url = SystemUtils::GetFileContents(urlpath.c_str());
        int url_file_remove_status = std::remove(urlpath.c_str());
            if (url_file_remove_status != 0) {
                 std::cerr << Messages::ERROR_IN_FILE_DELETE << url_file_remove_status << std::endl;
             }


        serverPORT = atoi(sever_port.c_str()); //This is used in DevSCefBrowserEvemtHandler DoClose method to terminate the server
        //std::cout << "serverPORT is " << serverPORT << "\n";

        // SimpleHandler implements browser-level call-backs.
        CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

        // Specify CEF browser settings here.
        CefBrowserSettings browser_settings;

        // Create the first browser window.
        CefBrowserHost::CreateBrowser(window_info, handler.get(), url, browser_settings, NULL);
   }
}