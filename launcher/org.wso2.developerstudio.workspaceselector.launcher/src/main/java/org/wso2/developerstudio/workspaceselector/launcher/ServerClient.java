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
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static java.io.File.separator;

/**
 * This class would get the server up on the port value retrieved from the workspace class,
 * This
 */
public class ServerClient implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ServerClient.class);

    private static final String DEV_STUDIO = "DevS";
    private static final String HTTP_LOCALHOST = "http://localhost:";
    private static final String IDE_CONTEXT_PATH = "/ws";
    private static final String USER_AGENT = "User-Agent";
    private static final String URL_TXT = "url.txt";
    private static final String BIN = "bin";

    private static final int CLIENT_SLEEP_TIME = 2500;
    private final int port;

    public ServerClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        doSleep();

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            final String ideURL = HTTP_LOCALHOST + port + IDE_CONTEXT_PATH;
            log.info("IDE URL is identified as: {}", ideURL);
            HttpClient client = HttpClientBuilder.create().build();

            while (true) {
                log.info("Send request to : {}", ideURL);

                HttpGet request = new HttpGet(ideURL);
                request.addHeader(USER_AGENT, DEV_STUDIO);
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (HttpHostConnectException e) {
                    //do nothing here since we need to wait till we get a sucesful response to open the client
                }
                // check for the HTTP response 200 OK status
                if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    ConfigurationContext.setServerSystemProperties(Integer.toString(port));
                    ConfigurationContext.setIDEUrl(ideURL);

                    log.info("Response recieved from : {}", ideURL);

                    String path = SWTSplashScreen.ROOT_DIR + separator + BIN + separator + URL_TXT;

                    log.info("Write url to file : {}", path);

                    fileWriter = new FileWriter(path);
                    bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(ideURL);
                    break;
                } else {
                    writeToDebugLog(response);
                }

                doSleep();
            }
        } catch (IOException e) {
            log.error("Error while executing the HTTP Client to check whether IDE is deployed.", e);
            //TODO need to exit application
        } finally {
            try {
                if (null != bufferedWriter) {
                    bufferedWriter.close();
                }
                if (null != fileWriter) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                log.error("Unable to close buffered Reader after reading URL.", e);
            }
        }
    }

    private void doSleep() {
        try {
            Thread.sleep(CLIENT_SLEEP_TIME);
        } catch (InterruptedException e) {
            if (log.isDebugEnabled()) {
                log.debug("Error making HTTP Client sleep.", e);
            }
        }
    }

    private void writeToDebugLog(HttpResponse response) {

        if (log.isDebugEnabled()) {
            StringBuffer result;
            log.debug("HTTP Response Error : Status-Code:{}",
                    response.getStatusLine().getStatusCode());
            try {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                result = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                log.debug(result.toString());
                bufferedReader.close();
            } catch (IOException ioe) {
                // Nothing to do here since we are logging an error here
                // And no need to make the system crash becase of logging
            }
        }
    }

}
