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
#include "DeveloperStudio/DevSCefBrowserEventHandler.h"
#include "include/cef_browser.h"
#include "include/cef_command_line.h"
#include "include/wrapper/cef_helpers.h"



int serverPID;

BOOL createProcess(const char* command, STARTUPINFO si, PROCESS_INFORMATION pi)
{
	wchar_t wcharCommand[64];
	mbstowcs(wcharCommand, command, strlen(command)+1);//Plus null

	BOOL result = CreateProcess( NULL,   // No module name (use command line)
		wcharCommand,        // Command line
		NULL,           // Process handle not inheritable
		NULL,           // Thread handle not inheritable
		FALSE,          // Set handle inheritance to FALSE
		0,              // No creation flags
		NULL,           // Use parent's environment block
		NULL,           // Use parent's starting directory 
		&si,            // Pointer to STARTUPINFO structure
		&pi );          // Pointer to PROCESS_INFORMATION structure
	return result;
}

void createServerProcess()
{
	STARTUPINFO serverStartupInfo;
	PROCESS_INFORMATION serverProcessInfo;
	ZeroMemory( &serverStartupInfo, sizeof(serverStartupInfo) );
	serverStartupInfo.cb = sizeof(serverStartupInfo);
	ZeroMemory( &serverProcessInfo, sizeof(serverProcessInfo) );

	//to hide the cmd window
	serverStartupInfo.wShowWindow = SW_HIDE;
	serverStartupInfo.dwFlags = STARTF_USESHOWWINDOW;

	BOOL result = createProcess("server.bat run", serverStartupInfo , serverProcessInfo);
}

void createWorkspaceProcess()
{
	STARTUPINFO workspaceStartupInfo;
	PROCESS_INFORMATION workspaceProcessInfo;
	//TODO comment what this does
	ZeroMemory(&workspaceStartupInfo, sizeof(workspaceStartupInfo));
	workspaceStartupInfo.cb = sizeof(workspaceStartupInfo);
	ZeroMemory(&workspaceProcessInfo, sizeof(workspaceProcessInfo));

	//to hide the cmd window
	workspaceStartupInfo.wShowWindow = SW_HIDE;
	workspaceStartupInfo.dwFlags = STARTF_USESHOWWINDOW;

	BOOL result = createProcess("workspace.bat run", workspaceStartupInfo , workspaceProcessInfo);
}


DevSCefClient::DevSCefClient() {
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
	
	window_info.SetAsPopup(NULL, "WSO2 Developer Studio");
#endif


	char* base_path;
	// Get the current working directory: 
	if( (base_path = _getcwd( NULL, 0 )) == NULL )
	{
		perror( "_getcwd error" );
	}
	std::string path(base_path);
	const size_t last_slash_idx = path.rfind('\\');
	path = path.substr(0, last_slash_idx);
	SystemUtils::APPLICATION_BASE_PATH = path;
	free(base_path);

	createServerProcess();

	createWorkspaceProcess();

	const char* pidFilePath = SystemUtils::BIN_PID.c_str();
	while (true) {
		std::FILE *pidFile = std::fopen(pidFilePath, "rb");
		if (pidFile) {
			std::fclose(pidFile);
			break;
		} else {
			Sleep(2);
		}
	}

	std::string sever_pid = SystemUtils::GetFileContents(pidFilePath);
	serverPID = atoi(sever_pid.c_str()); 
	int pid_file_remove_status = std::remove(pidFilePath);
	if (pid_file_remove_status != 0) {
		//std::cerr << Messages::ERROR_IN_FILE_DELETE << pid_file_remove_status << std::endl;
	}
	if (serverPID < 0) {
		//TODO find a way to exit the cef
		return;
	}

	//wait for url file
	const char * url_cpath = SystemUtils::BIN_URL_TXT.c_str();
	while (true) {
		std::FILE *url_file = std::fopen(url_cpath, "rb");
		if (url_file) {
			std::fclose(url_file);
			break;
		} else {
			Sleep(2);
		}
	}

	//read url file
	std::string url = SystemUtils::GetFileContents(url_cpath);
	//close url file
	int url_file_remove_status = std::remove(url_cpath);
	if (url_file_remove_status != 0) {
		//cout << Messages::ERROR_IN_FILE_DELETE << url_file_remove_status << std::endl;
	}
	

	// SimpleHandler implements browser-level callbacks.
	CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

	// Specify CEF browser settings here.
	CefBrowserSettings browser_settings;

	// Create the first browser window.
	CefBrowserHost::CreateBrowser(window_info, handler.get(), url, browser_settings, NULL);
}
