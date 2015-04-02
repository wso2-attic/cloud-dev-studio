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


class SystemUtils
{
private:

public:
    static int DEFAULT_WINDOW_WIDTH;
    static int DEFAULT_WINDOW_HEIGHT;

    static std::string BIN_BASH;
    static std::string APPLICATION_BASE_PATH;
    static std::string WSO2STUDIO_CHE_START;
	static std::string WSO2STUDIO_CHE_STOP;
    static std::string WSO2STUDIO_WORKSPACE;
    static std::string BIN_URL_TXT;
    static std::string BIN_PORT;

    static int GetScreenSize(int *w, int *h);
	static int GetSize(int x);
    static std::string GetFileContents(const char *filename);
	static BOOL CreateInternalProcess(const char* command, STARTUPINFO si, PROCESS_INFORMATION pi);
};
