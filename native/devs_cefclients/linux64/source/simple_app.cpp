// Copyright (c) 2013 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

#include "simple_app.h"

#include <string>
#include <iostream>

#include <sstream>
#include <stdlib.h>
#include <stdio.h>
#include <cstdio>
#include <pthread.h>
#include <unistd.h>

#include "simple_handler.h"
#include "include/cef_browser.h"
#include "include/cef_command_line.h"
#include "include/wrapper/cef_helpers.h"

char pild_killcmd[1024];
char basePath[1024];

SimpleApp::SimpleApp() {
}

std::string get_file_contents(const char *filename)
{
    std::FILE *fp = std::fopen(filename, "rb");
    if (fp)
    {
        std::string contents;
        std::fseek(fp, 0, SEEK_END);
        contents.resize(std::ftell(fp));
        std::rewind(fp);
        std::fread(&contents[0], 1, contents.size(), fp);
        std::fclose(fp);
        return(contents);
    }else{
     return "0";
    }
}

void *inc_x(void *x_void_ptr)
{
    /*char basePath[255] = "";

    realpath("../../", basePath);*/
    std::string path(basePath);

    std::string workerpath = path+"/bin/wso2studio_server.sh &";
    char worker_cpath[1024];
    strncpy(worker_cpath, workerpath.c_str(), sizeof(worker_cpath));

    int ret_code = system(worker_cpath);
        if (ret_code == 0) {
            std::cout << "server startd sucess fully";
        } else {
            std::cerr << "Error during the server startup: please refer log files more details " << ret_code << '\n';
        }
    return NULL;

}

void SimpleApp::OnContextInitialized() {
  CEF_REQUIRE_UI_THREAD();

  // Information used when creating the native window.
  CefWindowInfo window_info;

  window_info.height = 1024;
  window_info.width = 1024;

#if defined(OS_WIN)
  // On Windows we need to specify certain flags that will be passed to
  // CreateWindowEx().
  window_info.SetAsPopup(NULL, "cefsimple");
#endif

     realpath("../", basePath);
    //starting the server

    std::string path(basePath);

     int x = 0;

     pthread_t inc_x_thread;

     if(pthread_create(&inc_x_thread, NULL, inc_x, &x)) {
            fprintf(stderr, "Error creating thread\n");
            std::cout << "Error creating thread\n" << stderr << std::endl;
     }

     usleep(2);

    // starting the workspace selector and splace screen

    std::string workerpath =  path+"/bin/wso2studio_workspace.sh";
    char worker_cpath[1024];
    strncpy(worker_cpath, workerpath.c_str(), sizeof(worker_cpath));
    std::string bash_s = "/bin/bash";
    char bash[1024];
    strncpy(bash, bash_s.c_str(), sizeof(bash));
    std::string commad_s = "-c";
    char commad[1024];
    strncpy(commad, commad_s.c_str(), sizeof(commad));
    char *name[] = {bash,commad,worker_cpath, NULL };
    int pid = fork();
    if ( pid == 0 ) {
        int ret_code =  execvp(name[0], name);
        if (ret_code == 0) {
            std::cout << "workspace selector sucessfull";
        } else {
            std::cerr << "Error during the server startup: please refer log files more details " << ret_code << '\n';
        }
    }

    usleep(2);

    std::ostringstream ss;
    ss << getpid();

    // waiting the sever startup

    std::string url;
    std::string sever_pid;
    std::string urlpath =  path+"/bin/url.txt";

    char url_cpath[1024];
    strncpy(url_cpath, urlpath.c_str(), sizeof(url_cpath));

    std::string pidpath =  path+"/bin/pid";
    char pid_cpath[1024];
    strncpy(pid_cpath, pidpath.c_str(), sizeof(pid_cpath));

    while (true) {
        std::FILE *fp = std::fopen(url_cpath, "rb");
        if (fp) {
           std::fclose(fp);
            url = get_file_contents(url_cpath);
            sever_pid = get_file_contents(pid_cpath);
            int ret_code = std::remove(url_cpath);
            std::remove(pid_cpath);
            if (ret_code == 0) {
                std::cout << "File was successfully deleted\n";
            } else {
                std::cerr << "Error during the deletion: " << ret_code << '\n';
            }
            break;

        }else{
            std::cout << "Waiting for URL...";
            sleep(2);
        }

    }

    std::string killcmd = "kill -9 "+sever_pid;
    strncpy(pild_killcmd, killcmd.c_str(), sizeof(pild_killcmd));

  // SimpleHandler implements browser-level callbacks.
  CefRefPtr<SimpleHandler> handler(new SimpleHandler());

  // Specify CEF browser settings here.
  CefBrowserSettings browser_settings;

  // Check if a "--url=" value was provided via the command-line. If so, use
  // that instead of the default URL.
  /*CefRefPtr<CefCommandLine> command_line =
      CefCommandLine::GetGlobalCommandLine();
  url = command_line->GetSwitchValue("url");
  if (url.empty())
    url = "http://www.google.com";*/

  // Create the first browser window.
  CefBrowserHost::CreateBrowser(window_info, handler.get(), url,
                                browser_settings, NULL);
}


