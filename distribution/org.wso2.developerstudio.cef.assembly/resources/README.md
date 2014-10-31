=====================================================================
  WSO2 Developer Studio 4.0.0
=====================================================================

Welcome to the WSO2 Developer Studio 4.0.0 release

How to run Developer Studio 4.0.0
=============================================

1) Extract the content of the zip file

2) Go to bin folder

3) Execute the wso2 wso2studio.sh file as
        ./ wso2studio.sh 
		This will open the WSO2 Developer Studio. 

WSO2 ESB 4.8.1 distribution directory structure
=============================================
	developer-studio home
		|- bin <folder>
		|- icons <folder>
		|- logs <folder>	
		|- repository <folder>
			|- cef <folder>
			|- conf <folder>
			|- deployment <folder>
			|- libs <folder>
		|- temp <folder>
		|- LICENSE.txt <file>
		|- README.txt <file>	
		

    - bin
	  Contains various scripts .sh scripts



    - icons
	  chromium loader resources

    - logs
	  Contains all the log files
   
    - repository
	  Used to store tomcat resources
		    - cef
	 		 cef resources required for the stand-alone version

    		    - conf
	  Contains all the property files needed

    - deployment
	   WSO2 cloud dev studio web application contents

    		    - libs
	                           Contains the basic set of libraries required to startup developer studio
	                      in standalone mode
    - temp
	  Stores temporary files


    - LICENSE.txt
	  Apache License 2.0 and the relevant other licenses under which
	  WSO2 Developer Studio is distributed.

    - README.txt
	  This document.


Known issues of WSO2 Developer Studio - 4.0.0
==========================================

 

Issue Tracker
==========================================
  
  https://wso2.org/jira/browse/TOOLS

Other known issues
==========================================

  * There exist a known Ubuntu issue in running WSO2 Developer Studio which says missing libgtkglext-x11-1.0.so.0  

--------------------------------------------------------------------------------
(c) Copyright 2013 WSO2 Inc.



