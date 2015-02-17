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
#include <cstdio>
#include <iostream>
#include "wtypes.h"
#include "SystemUtils.h"
#include "Messages.h"

int	SystemUtils::DEFAULT_WINDOW_WIDTH = 1024;
int	SystemUtils::DEFAULT_WINDOW_HEIGHT = 1024;
std::string	SystemUtils::APPLICATION_BASE_PATH;
std::string	SystemUtils::WSO2STUDIO_SERVER_SH_AND = "server.bat";
std::string	SystemUtils::WSO2STUDIO_WORKSPACE = "workspace.bat";
std::string	SystemUtils::BIN_URL_TXT = "url.txt";
std::string SystemUtils::BIN_PID = "pid";

std::string SystemUtils::GetFileContents(const char *filename) {
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

// Get the horizontal and vertical screen sizes in pixel
int SystemUtils::GetScreenSize(int* w, int* h)
{
   RECT desktop;
   // Get a handle to the desktop window
   const HWND hDesktop = GetDesktopWindow();
   // Get the size of screen to the variable desktop
   if (hDesktop != NULL) {
		GetWindowRect(hDesktop, &desktop);
		// The top left corner will have coordinates (0,0)
	   // and the bottom right corner will have coordinates
	   // (horizontal, vertical)
	   *w = desktop.right;
	   *h = desktop.bottom;
	   return 0;
   }
   return -1;
}


