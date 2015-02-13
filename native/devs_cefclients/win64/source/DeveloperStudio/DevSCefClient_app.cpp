// Copyright (c) 2013 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

#include "DeveloperStudio/DevSCefClient_app.h"

#include <string>

#include "DeveloperStudio/DevSCefBrowserEventHandler.h"
#include "include/cef_browser.h"
#include "include/cef_command_line.h"
#include "include/wrapper/cef_helpers.h"
#include "SystemUtils.h"
#include "Messages.h"
#include <windows.h>
#include <stdio.h>
#include <process.h>
#include <direct.h>
#include <stdlib.h>


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
	ZeroMemory( &workspaceStartupInfo, sizeof(workspaceStartupInfo) );
	workspaceStartupInfo.cb = sizeof(workspaceStartupInfo);
	ZeroMemory( &workspaceProcessInfo, sizeof(workspaceProcessInfo) );

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

#if defined(OS_WIN)
	// On Windows we need to specify certain flags that will be passed to
	// CreateWindowEx().
	window_info.SetAsPopup(NULL, "cefsimple");
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

	std::string urlpath = SystemUtils::BIN_URL_TXT;
	char url_cpath[512];
	strncpy(url_cpath, urlpath.c_str(), sizeof (url_cpath));

	std::string pidpath = SystemUtils::BIN_PID;
	char pid_cpath[512];
	strncpy(pid_cpath, pidpath.c_str(), sizeof (pid_cpath));

	std::string url;
	std::string sever_pid;
	while (true) {
		std::FILE *url_file = std::fopen(url_cpath, "rb");
		if (url_file) {
			std::fclose(url_file);
			url = SystemUtils::GetFileContents(url_cpath);
			sever_pid = SystemUtils::GetFileContents(pid_cpath);

			int url_file_remove_status = std::remove(url_cpath);
			if (url_file_remove_status != 0) {
				//cout << Messages::ERROR_IN_FILE_DELETE << url_file_remove_status << std::endl;
			}

			int pid_file_remove_status = std::remove(pid_cpath);
			if (pid_file_remove_status != 0) {
				//std::cerr << Messages::ERROR_IN_FILE_DELETE << pid_file_remove_status << std::endl;
			}

			break;
		} else {
			//std::cout << Messages::WAITING_FOR_URL;
			Sleep(2);
		}
	}


	serverPID = atoi(sever_pid.c_str()); //This is used in DevSCefBrowserEvemtHandler DoClose method to terminate the server

	// SimpleHandler implements browser-level callbacks.
	CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

	// Specify CEF browser settings here.
	CefBrowserSettings browser_settings;

	// Create the first browser window.
	CefBrowserHost::CreateBrowser(window_info, handler.get(), url, browser_settings, NULL);
}
