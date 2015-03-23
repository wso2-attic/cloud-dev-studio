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



int serverPORT;


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

	BOOL result = SystemUtils::CreateInternalProcess("che.bat run", serverStartupInfo , serverProcessInfo);
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

	BOOL result = SystemUtils::CreateInternalProcess("workspace.bat", workspaceStartupInfo , workspaceProcessInfo);
}


DevSCefClient::DevSCefClient() {
}


void DevSCefClient::OnContextInitialized() {
	CEF_REQUIRE_UI_THREAD();

	// Information used when creating the native window.
	CefWindowInfo window_info;

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

	createWorkspaceProcess();

	const char* portFilePath = SystemUtils::BIN_PORT.c_str();
	while (true) {
		std::FILE *portFile = std::fopen(portFilePath, "rb");
		if (portFile) {
			std::fclose(portFile);
			break;
		} else {
			Sleep(2);
		}
	}

	
	createServerProcess();

	std::string sever_port = SystemUtils::GetFileContents(portFilePath);
	serverPORT = atoi(sever_port.c_str()); //This is used in DevSCefBrowserEvemtHandler DoClose method to terminate the server
	int pid_file_remove_status = std::remove(portFilePath);
	if (pid_file_remove_status != 0) {
		//std::cerr << Messages::ERROR_IN_FILE_DELETE << pid_file_remove_status << std::endl;
	}
	

	if (serverPORT > 0) {

		//wait for url file
		const char * url_cpath = SystemUtils::BIN_URL_TXT.c_str();
		while (true) {
			std::FILE *url_file = std::fopen("url.txt", "rb");
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
	} else {
		HANDLE currentProcessHandle = GetCurrentProcess();
		TerminateProcess(currentProcessHandle, 0);
	}

}
