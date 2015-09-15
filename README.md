===================================================================
  Cloud-dev-studio : WSO2 Developer Studio
=====================================================================

Welcome to the WSO2 Developer Studio

Cloud-dev-studio is an Eclipse Che based Development Tool, and is a standalone version for the cloud based Eclipse Che.This standalone version is built by using Apache tomcat & Embedded Chromium Framework(CEF)

## Requirements

* Needs maven 3.1.x or higher to build
* Needs JDK 1.7.xx to run developer studio
* libudev.so file - Linux startup may fail on first attempt due to libudev.so file not  being instaled in the OS.In this case please run the install.sh file shipped with the pack in the bin folder and retry
* libgtkglext-x11-1.0.so.0 in Ubuntu

## Cloud-dev-studio directory structure

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


## How to run

1) Extract the content of the zip file
2) Go to bin folder
3) In linux environment execute the wso2 wso2studio.sh file as sh wso2devstudio
   In windows environment execute the Developerstudio.exe file
   In mac os environment run the devs app file in the wso2-developerstudio-mac extraction (outside bin folder)
This will open the WSO2 Developer Studio.

## How to Contribute

* WSO2 Cloud-dev-studio code is hosted in [GitHub](https://github.com/wso2/cloud-dev-studio.git).
* Please report issues at[JIRA](https://wso2.org/jira/browse/TOOLS)

## Contact us

  WSO2 developers can be contacted via the mailing lists:

  * Developers List : dev@wso2.org
  * Architecture List : architecture@wso2.org
