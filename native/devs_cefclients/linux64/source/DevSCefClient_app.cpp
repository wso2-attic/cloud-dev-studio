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



int serverPID;

DevSCefClient::DevSCefClient() {
}


void *ExecuteServerInBackground(void *args_ptr) {

    std::string server_launch_cmd = SystemUtils::APPLICATION_BASE_PATH + SystemUtils::WSO2STUDIO_SERVER_SH_AND;

    char server_launch_command_arr[1024];
    strncpy(server_launch_command_arr, server_launch_cmd.c_str(), sizeof (server_launch_command_arr));

    int server_startup_status = system(server_launch_command_arr);
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
    	//std::cout << "getScreenSize DefaultScreenOfDisplay size is " << height << ":" << width << std::endl;
    	window_info.height = height;
    	window_info.width = width;
    }


#if defined(OS_WIN)
    // On Windows we need to specify certain flags that will be passed to
    // CreateWindowEx().
    window_info.SetAsPopup(NULL, "DevSCefClient");
#endif

    char base_path[1024];
    realpath("../", base_path);
    std::string path(base_path);
    SystemUtils::APPLICATION_BASE_PATH = path;
    //std::cout << "BASE_PATH is " << SystemUtils::APPLICATION_BASE_PATH << std::endl;

    //starting the server
    int server_args = 0;
    pthread_t server_thread;

    if (pthread_create(&server_thread, NULL, ExecuteServerInBackground, &server_args)) {
        std::cout << Messages::ERROR_CREATING_SERVER_THREAD << stderr << std::endl;
    }

    //usleep(2);
    // starting the workspace selector and splash screen
    std::string workspace_selector_cmd = path + SystemUtils::WSO2STUDIO_WORKSPACE;
    char workspace_selector_cmd_arr[1024];
    strncpy(workspace_selector_cmd_arr, workspace_selector_cmd.c_str(),
            sizeof (workspace_selector_cmd_arr));

    //char bash[] = "bin/bash";
    //char command[] = "-c";
    std::string bash_s = SystemUtils::BIN_BASH;
    char bash[1024];
    strncpy(bash, bash_s.c_str(), sizeof (bash));
    std::string command_s = "-c";
    char command[1024];
    strncpy(command, command_s.c_str(), sizeof (command));
    char *name[] = {bash,  command, workspace_selector_cmd_arr, NULL};
    int pid = fork();
    if (pid == 0) {
        int workspace_selector_status = execvp(name[0], name);
        if (workspace_selector_status == 0) {
            std::cout << Messages::WORKSPACE_SELECTOR_STARTED << std::endl;
        } else {
            std::cerr << Messages::SERVER_STARTUP_ERROR << workspace_selector_status << std::endl;
        }
    }

    //usleep(2);
    // waiting the sever startup
    std::string url;
    std::string sever_pid;
    std::string urlpath = path + SystemUtils::BIN_URL_TXT;

    char url_cpath[1024];
    strncpy(url_cpath, urlpath.c_str(), sizeof (url_cpath));

    std::string pidpath = path + SystemUtils::BIN_PID;
    char pid_cpath[1024];
    strncpy(pid_cpath, pidpath.c_str(), sizeof (pid_cpath));

    while (true) {
        std::FILE *url_file = std::fopen(url_cpath, "rb");
        if (url_file) {
            std::fclose(url_file);
            url = SystemUtils::GetFileContents(url_cpath);
            sever_pid = SystemUtils::GetFileContents(pid_cpath);

            int url_file_remove_status = std::remove(url_cpath);
            if (url_file_remove_status != 0) {
                std::cerr << Messages::ERROR_IN_FILE_DELETE << url_file_remove_status << std::endl;
            }

            int pid_file_remove_status = std::remove(pid_cpath);
            if (pid_file_remove_status != 0) {
				std::cerr << Messages::ERROR_IN_FILE_DELETE << pid_file_remove_status << std::endl;
			}

            break;

        } else {
            std::cout << Messages::WAITING_FOR_URL;
            sleep(2);
        }

    }

    serverPID = atoi(sever_pid.c_str()); //This is used in DevSCefBrowserEvemtHandler DoClose method to terminate the server
    //std::cout << "serverPID is " << serverPID << "\n";

    // SimpleHandler implements browser-level call-backs.
    CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

    // Specify CEF browser settings here.
    CefBrowserSettings browser_settings;

    // Create the first browser window.
    CefBrowserHost::CreateBrowser(window_info, handler.get(), url, browser_settings, NULL);
}




