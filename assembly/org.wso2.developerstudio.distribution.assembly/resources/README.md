=====================================================================
  WSO2 Developer Studio 4.0.0
=====================================================================

Welcome to the WSO2 Developer Studio 4.0.0 release

How to run Developer Studio 4.0.0
=============================================

1) Extract the content of the zip file

2) Go to bin folder

3) In linux execute the wso2 wso2studio.sh file as
        ./ wso2devstudio

   In windows execute the Developerstudio.exe file

   In mac os run the devs app file in the wso2-developerstudio-mac extraction (outside bin folder)

               This will open the WSO2 Developer Studio.

WSO2 ESB 4.8.1 distribution directory structure
=============================================
	developer-studio home
		|- bin
        |- conf
        |- ext
        |- icons
        |- lib
        |- logs
        |- sdk-resources
        |- sdk-tools
        |- temp
        |- tomcat
        |- tutorials
        |- webapps
        |- work
        |- extInstall.bat
        |- extInstall.sh
        |- LICENSE
        |- NOTICE
        |- RELEASE-NOTES
        |- RUNNING.txt
        |- RUNNING_CheSDK.txt
		

    - bin
	  Contains various scripts .sh scripts, .bat scripts and executable files

    - icons
	  Chromium loader resources

    - logs
	  Contains all the log files
   
    - conf
	  Contains all the property files needed, for che and developer studio

    - ext
	   Extensions for che from developer studio

    - libs
       Contains the basic set of libraries required to startup developer studio
	                      in standalone mode

    - temp
	   Stores temporary files

	- sdk-resources
	   Che sdk resources files used by Eclipse che

	- sdk-tools
       Che sdk tools used by Eclipse che

    - tomcat
       The runner tomcat used in Eclipse che on running the created projects

    -extInstall.bat / extInstall.sh
       Install script used by Eclipse che to install any che extensions

    - LICENSE.txt
	   Apache License 2.0 and the relevant other licenses under which
	   WSO2 Developer Studio is distributed.

	- LICENSE
	   Apache License 2.0 under which Eclipse Che is distributed

    - README.md
	   This document.


Known issues of WSO2 Developer Studio - 4.0.0
==========================================


Linux startup may fail on first attempt due to libudev.so file not being instaled in the OS.
 In this case please run the install.sh file shipped with the pack in the bin folder and retry

New Project types under Codenvy in the project creation window are not working in the Che-SDK 3.7.3 which is distributed in Develoepr Studio 4.0.0

 

Issue Tracker
==========================================
  
  https://wso2.org/jira/browse/TOOLS

Other known issues
==========================================

  * There exist a known Ubuntu issue in running WSO2 Developer Studio which says missing libgtkglext-x11-1.0.so.0  

--------------------------------------------------------------------------------
(c) Copyright 2014 WSO2 Inc.



