/*
* Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.developerstudio.workspace.launcher.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkspaceSelectorBootstrap {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkspaceSelectorBootstrap.class);
	public static final String STUDIO_ROOT_ENV_VAR_NAME = "WSO2_DEVELOPER_STUDIO_PATH";
	public static final int MAXIMUM_PROGRESS = 1000; // parameters to design the splashscreen, needs to be sent to a properties file
	public static final int SHELL_WIDTH = 448; // parameters to design the splashscreen
	public static final int SHELL_HEIGHT = 270;// parameters to design the splashscreen
	public static final int IMAGE_LABEL_WIDTH = 450;// parameters to design the splashscreen
	public static final int IMAGE_LABEL_HEIGHT = 300;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_WIDTH = 450;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_HEIGHT = 20;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_X_LOC = 0;// parameters to design the splashscreen
	public static final int PROGRESS_BAR_Y_LOC = 240;// parameters to design the splashscreen

	public static final String SPLASH_SCREEN_IMAGE = "splash.png";
	public static final String ICONS_FOLDER = "icons";
	public static final String ROOT_DIR = System.getenv(STUDIO_ROOT_ENV_VAR_NAME);
	
	public static void main(String args[]) {
		try {
		
			String port = ""+getAvailablePort();
			final String ideURL = "http://localhost:" + port + "/ide";
			logger.info("IDE URL is set to: " + ideURL);
		 
		 	String SplashImageResource = ROOT_DIR + File.separator + ICONS_FOLDER + File.separator + SPLASH_SCREEN_IMAGE;
			SWTSplashScreen swtSplashScreen = new SWTSplashScreen(SplashImageResource, SHELL_WIDTH,
			                                                      SHELL_HEIGHT,
			                                                      MAXIMUM_PROGRESS, IMAGE_LABEL_WIDTH,
			                                                      IMAGE_LABEL_HEIGHT,
			                                                      PROGRESS_BAR_WIDTH,
			                                                      PROGRESS_BAR_HEIGHT,
			                                                      PROGRESS_BAR_X_LOC,
			                                                      PROGRESS_BAR_Y_LOC);	
			swtSplashScreen.updateProgress(100);
			if (!isDefaultWorkSpaceSet()) {
				WorkSpaceLauncher workSpaceLauncher = new WorkSpaceLauncher();
				if (!workSpaceLauncher.isUserWorkSpaceSet()) {
					logger.warn("workspace not selected !");
				}else{
					Files.write(Paths.get(ROOT_DIR+File.separator+"bin"+File.separator+"PORT"), port.getBytes());
				}
			}
			int progress =400;
			HttpClient client = HttpClientBuilder.create().build();			 
			while(true) {
			swtSplashScreen.updateProgress(progress);	
			HttpGet request = new HttpGet(ideURL);
			request.addHeader("User-Agent", "DevS");
			Thread.sleep(1000);
			swtSplashScreen.updateProgress(progress+100);	
			Thread.sleep(1000);
			swtSplashScreen.updateProgress(progress+100);	
			HttpResponse response = client.execute(request);
			swtSplashScreen.updateProgress(progress+100);	
			if(response.getStatusLine().getStatusCode()==200){
				logger.info(ROOT_DIR+File.separator+"bin"+File.separator+"url.txt");	
	        	Files.write(Paths.get(ROOT_DIR+File.separator+"bin"+File.separator+"url.txt"), ideURL.getBytes());	  
	        	System.out.println("CYCLE 33333"+System.currentTimeMillis());	
	        	swtSplashScreen.updateProgress(progress+300);	
				break;
			}else{
				logger.debug("IDE Starting"+response.getStatusLine().getStatusCode());
				progress =progress+300;
				swtSplashScreen.updateProgress(progress);
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				logger.debug(result.toString());
			}
			progress =progress+100;
			swtSplashScreen.updateProgress(progress);
		}
			swtSplashScreen.updateProgress(1000);		     
		} catch (IOException | InterruptedException e) {
			logger.error("Error whiele selecting the works", e);
			System.exit(1);
		}  
	}
	
	
	private static Integer getAvailablePort() throws IOException {

		ServerSocket socket = new ServerSocket(0);
		Integer port = socket.getLocalPort();
		socket.close();
		return port;
	}
	
	private static boolean isDefaultWorkSpaceSet() {
		String defaultWorkSpace = null;
		try {
			defaultWorkSpace = ConfigManager.getDefaultWorkSpaceProperty();
		} catch (IOException e) {
			logger.error(
					"error reading whether user has set default workspace in properties" + e);
		}
		return Boolean.parseBoolean(defaultWorkSpace);
	}

	/**
	 * @param progress
	 * @param messageToDisplay method to call the start up in Splash Screen to display the progress on IDE opening process to the user
	 */

}
