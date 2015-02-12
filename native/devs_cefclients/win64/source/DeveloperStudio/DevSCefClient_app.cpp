// Copyright (c) 2013 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

#include "DeveloperStudio/DevSCefClient_app.h"

#include <string>

#include "DeveloperStudio/DevSCefBrowserEventHandler.h"
#include "include/cef_browser.h"
#include "include/cef_command_line.h"
#include "include/wrapper/cef_helpers.h"
#include <windows.h>
#include <stdio.h>
#include <process.h>
//using namespace System::Diagnostics;


unsigned Counter; 
unsigned __stdcall SecondThreadFunc( void* pArguments )
{
    printf( "In second thread...\n" );
	system("cmd mybatch.bat");
    while ( Counter < 10000000 )
        Counter++;

    _endthreadex( 0 );
    return 0;
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

  // SimpleHandler implements browser-level callbacks.
  CefRefPtr<DevSCefBrowserEventHandler> handler(new DevSCefBrowserEventHandler());

  // Specify CEF browser settings here.
  CefBrowserSettings browser_settings;

  std::string url;


  HANDLE hThread;
  unsigned threadID;

  printf( "Creating second thread...\n" );
  
  //Debug::WriteLine("This will be written to debug output");
  //TRACE(_T("Hello World!"));

   // Create the second thread.
   hThread = (HANDLE)_beginthreadex( NULL, 0, &SecondThreadFunc, NULL, 0, &threadID );

    // Wait until second thread terminates. If you comment out the line
    // below, Counter will not be correct because the thread has not
    // terminated, and Counter most likely has not been incremented to
    // 1000000 yet.
    //WaitForSingleObject( hThread, INFINITE );
    printf( "Counter should be 1000000; it is-> %d\n", Counter );
    


  // Check if a "--url=" value was provided via the command-line. If so, use
  // that instead of the default URL.
  CefRefPtr<CefCommandLine> command_line =
      CefCommandLine::GetGlobalCommandLine();
  url = command_line->GetSwitchValue("url");
  if (url.empty())
    url = "http://www.google.com";

  // Create the first browser window.
  CefBrowserHost::CreateBrowser(window_info, handler.get(), url,
                                browser_settings, NULL);

  // Destroy the thread object.
    CloseHandle( hThread );

}
