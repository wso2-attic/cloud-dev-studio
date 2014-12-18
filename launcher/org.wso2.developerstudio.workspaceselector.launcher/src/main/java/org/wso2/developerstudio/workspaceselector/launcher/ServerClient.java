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

package org.wso2.developerstudio.workspaceselector.launcher;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * This class would get the server up on the port value retrieved from the workspace class,
 * This
 */
public class ServerClient implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(ServerClient.class);

	private static final String URL_TXT = "url.txt";
	private static final String BIN = "bin";
	private final String port;

	public ServerClient(String port) {
		this.port = port;
	}

	@Override public void run() {
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		StringBuffer result;

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			if (log.isDebugEnabled()) {
				log.debug("Error making the  HTTP Client sleep.", e);
			}
		}
		try {
			final String ideURL = "http://localhost:" + port + "/ws";
			log.info("IDE URL is set to: " + ideURL);
			HttpClient client = HttpClientBuilder.create().build();
			while (true) {
				HttpGet request = new HttpGet(ideURL);
				request.addHeader("User-Agent", "DevS");
				HttpResponse response = client.execute(request);
				if (response.getStatusLine().getStatusCode() ==
				    HttpStatus.SC_OK) { // check for the HTTP response 200 OK status
					fileWriter = new FileWriter(
							SWTSplashScreen.ROOT_DIR + File.separator + BIN + File.separator +
							URL_TXT);
					bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(ideURL);
					break;
				} else {
					if (log.isDebugEnabled()) {
						log.debug("IDE Starting" + response.getStatusLine().getStatusCode());
						bufferedReader = new BufferedReader(
								new InputStreamReader(response.getEntity().getContent()));
						result = new StringBuffer();
						String line;
						while ((line = bufferedReader.readLine()) != null) {
							result.append(line);
						}
						log.debug(result.toString());
					}
				}
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					if (log.isDebugEnabled()) {
						log.debug("Error making the  HTTP Client sleep.", e);
					}
				}
			}
		} catch (IOException e) {
			log.error("Error while executing the HTTP Client to check whether IDE is deployed.", e);
			//TODO need to exit application
		} finally {
			try {
				if (null != bufferedReader) {
					bufferedReader.close();
				}
				if (null != bufferedWriter) {
					bufferedWriter.close();
				}
				if (null != fileWriter) {
					fileWriter.close();
				}
			} catch (IOException e) {
				log.error("Unable to close the buffered Reader reading the url", e);
			}
		}
	}
}

