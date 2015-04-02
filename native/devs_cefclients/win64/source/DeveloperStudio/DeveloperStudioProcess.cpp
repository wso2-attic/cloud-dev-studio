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

#include <string>
#include <windows.h>
#include <stdio.h>
#include <cstdio>
#include <iostream>
#include <process.h>
#include <direct.h>
#include <stdlib.h>
#include "wtypes.h"
#include "SystemUtils.h"
#include "Messages.h"
#include "DeveloperStudioProcess.h"



void createServerProcess()
{
	STARTUPINFO serverStartupInfo;
	PROCESS_INFORMATION serverProcessInfo;
	ZeroMemory(&serverStartupInfo, sizeof(serverStartupInfo));
	serverStartupInfo.cb = sizeof(serverStartupInfo);
	ZeroMemory(&serverProcessInfo, sizeof(serverProcessInfo));

	//to hide the cmd window
	serverStartupInfo.wShowWindow = SW_HIDE;
	serverStartupInfo.dwFlags = STARTF_USESHOWWINDOW;

	BOOL result = SystemUtils::CreateInternalProcess(SystemUtils::WSO2STUDIO_CHE_START.c_str(), serverStartupInfo, serverProcessInfo);
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

	BOOL result = SystemUtils::CreateInternalProcess(SystemUtils::WSO2STUDIO_WORKSPACE.c_str(), workspaceStartupInfo, workspaceProcessInfo);
}


BOOL createCheServerStopProcess()
{
	STARTUPINFO serverStartupInfo;
	PROCESS_INFORMATION serverProcessInfo;
	ZeroMemory(&serverStartupInfo, sizeof(serverStartupInfo));
	serverStartupInfo.cb = sizeof(serverStartupInfo);
	ZeroMemory(&serverProcessInfo, sizeof(serverProcessInfo));

	//to hide the cmd window
	serverStartupInfo.wShowWindow = SW_HIDE;
	serverStartupInfo.dwFlags = STARTF_USESHOWWINDOW;

	BOOL result = SystemUtils::CreateInternalProcess(SystemUtils::WSO2STUDIO_CHE_STOP.c_str(), serverStartupInfo, serverProcessInfo);
	return result;
}

DeveloperStudioProcess::DeveloperStudioProcess() {
}

DeveloperStudioProcess::~DeveloperStudioProcess() {
}


int DeveloperStudioProcess::GetServerPort() 
{
	return serverPort;
}

std::string DeveloperStudioProcess::GetUrl() 
{
	return url;
}

std::string DeveloperStudioProcess::GetBasePath() 
{
	return basePath;
}


int DeveloperStudioProcess::Stop() {
	return  createCheServerStopProcess();
}

int DeveloperStudioProcess::Start() 
{
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
	basePath = path;
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
	serverPort = atoi(sever_port.c_str()); //This is used in DevSCefBrowserEvemtHandler DoClose method to terminate the server
	int pid_file_remove_status = std::remove(portFilePath);
	if (pid_file_remove_status != 0) {
		//std::cerr << Messages::ERROR_IN_FILE_DELETE << pid_file_remove_status << std::endl;
	}
	

	if (serverPort > 0) {

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
		url = SystemUtils::GetFileContents(url_cpath);
		//close url file
		int url_file_remove_status = std::remove(url_cpath);
		if (url_file_remove_status != 0) {
			//cout << Messages::ERROR_IN_FILE_DELETE << url_file_remove_status << std::endl;
		}
	}

	return 0;
}


