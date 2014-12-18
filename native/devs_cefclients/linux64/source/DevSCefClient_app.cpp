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

char pid_killcmd[1024];
char base_path[1024];

DevSCefClient::DevSCefClient() {
}

std::string GetFileContents(const char *filename) {
    std::FILE *file = std::fopen(filename, "rb");
    if (file) {
        std::string contents;
        std::fseek(file, 0, SEEK_END);
        contents.resize(std::ftell(file));
        std::rewind(file);
        std::fread(&contents[0], 1, contents.size(), file);
        std::fclose(file);
        return (contents);
    } else {
        return "0";
    }
}

void *ExecuteServerInBackground(void *args_ptr) {

    std::string path(base_path);

    std::string server_launch_cmd = path + "/bin/wso2studio_server.sh &";
    char server_launch_command_arr[1024];
    strncpy(server_launch_command_arr, server_launch_cmd.c_str(), 
            sizeof (server_launch_command_arr));

    int server_startup_status = system(server_launch_command_arr);
    if (server_startup_status == 0) {
        std::cout << "Server started successfully.";
    } else {
        std::cerr << "Error during the server startup: "
                "please refer to log for more details.\n"
                "StatusCode: " << server_startup_status << '\n';
    }
    return NULL;

}

void DevSCefClient::OnContextInitialized() {
    CEF_REQUIRE_UI_THREAD();

    // Information used when creating the native window.
    CefWindowInfo window_info;
    window_info.height = 1024;
    window_info.width = 1024;

#if defined(OS_WIN)
    // On Windows we need to specify certain flags that will be passed to
    // CreateWindowEx().
    window_info.SetAsPopup(NULL, "DevSCefClient");
#endif

    realpath("../", base_path);
    std::string path(base_path);

    //starting the server
    int server_args = 0;
    pthread_t server_thread;

    if (pthread_create(&server_thread, NULL, ExecuteServerInBackground, 
            &server_args)) {
        fprintf(stderr, "Error creating server thread.\n");
        std::cout << "Error creating server thread.\n" << stderr << std::endl;
    }

    usleep(2);

    // starting the workspace selector and splash screen
    std::string workspace_selector_cmd = path + "/bin/wso2studio_workspace.sh";
    char workspace_selector_cmd_arr[1024];
    strncpy(workspace_selector_cmd_arr, workspace_selector_cmd.c_str(), 
            sizeof (workspace_selector_cmd_arr));

    std::string bash_s = "/bin/bash";
    char bash[1024];
    strncpy(bash, bash_s.c_str(), sizeof (bash));
    std::string command_s = "-c";
    char command[1024];
    strncpy(command, command_s.c_str(), sizeof (command));
    char *name[] = {bash, command, workspace_selector_cmd_arr, NULL};
    int pid = fork();
    if (pid == 0) {
        int workspace_selector_status = execvp(name[0], name);
        if (workspace_selector_status == 0) {
            std::cout << "workspace selector started successfully";
        } else {
            std::cerr << "Error during the server startup: "
                    "please refer log files more details.\n"
                    "StatusCode: " << workspace_selector_status << '\n';
        }
    }

    usleep(2);

    // waiting the sever startup
    std::string url;
    std::string sever_pid;
    std::string urlpath = path + "/bin/url.txt";

    char url_cpath[1024];
    strncpy(url_cpath, urlpath.c_str(), sizeof (url_cpath));

    std::string pidpath = path + "/bin/pid";
    char pid_cpath[1024];
    strncpy(pid_cpath, pidpath.c_str(), sizeof (pid_cpath));

    while (true) {
        std::FILE *url_file = std::fopen(url_cpath, "rb");
        if (url_file) {
            std::fclose(url_file);
            url = GetFileContents(url_cpath);
            sever_pid = GetFileContents(pid_cpath);
            int url_file_remove_status = std::remove(url_cpath);
            std::remove(pid_cpath);
            if (url_file_remove_status == 0) {
                std::cout << "File was deleted successfully.\n";
            } else {
                std::cerr << "Error during file deletion: "
                        "StatusCode: " 
                        << url_file_remove_status << '\n';
            }
            break;

        } else {
            std::cout << " Waiting for URL |";
            sleep(2);
        }

    }

    std::string killcmd = "kill -9 " + sever_pid;
    strncpy(pid_killcmd, killcmd.c_str(), sizeof (pid_killcmd));

    // SimpleHandler implements browser-level call-backs.
    CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

    // Specify CEF browser settings here.
    CefBrowserSettings browser_settings;

    // Create the first browser window.
    CefBrowserHost::CreateBrowser(window_info, handler.get(), url,
            browser_settings, NULL);
}


