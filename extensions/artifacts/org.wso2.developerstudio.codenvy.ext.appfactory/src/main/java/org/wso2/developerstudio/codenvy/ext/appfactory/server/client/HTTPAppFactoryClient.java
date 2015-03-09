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
package org.wso2.developerstudio.codenvy.ext.appfactory.server.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryLoginResponse;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.ErrorType;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles all type of HTTP/HTTPS requests and responses from DevStudio to AppFactory/ AppCloud server
 */
public class HTTPAppFactoryClient {
    private static final Logger logger = LoggerFactory.getLogger(HTTPAppFactoryClient.class);

    private static HttpClient client;
    private static String cookie;

    /**
     * Creates an HTTP Entity using given request parameters
     *
     * @param serverURL          AppFactory/ AppCloud server URL to connect with for login request
     * @param loginRequestParams Map that contains login request params as key value pairs
     * @return Populated AppFactoryLoginResponse object
     * @throws org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException
     */
    public static AppFactoryLoginResponse doLogin(String serverURL, Map<String, String> loginRequestParams)
            throws AppFactoryServerException {
        AppFactoryLoginResponse loginResponse = new AppFactoryLoginResponse();
        String successLogin;

        //Creating an http client and wrap it
        client = new DefaultHttpClient();
        client = wrapClient(client, serverURL);

        //Creating a http post request by giving server URL
        HttpPost httpLoginRequest = new HttpPost(serverURL);
        HttpResponse httpLoginResponse;

        try {
            httpLoginRequest.setEntity(createEntityFromRequestParams(loginRequestParams));
        } catch (UnsupportedEncodingException e) {
            logger.error("Error in encoding login request parameters to an entity, Parameters : " + loginRequestParams, e);
            throw new AppFactoryServerException("Unable to login user, Details : " + loginRequestParams, e);
        }

        //Sending HTTP post request to AppFactory/ AppCloud
        try {
            httpLoginResponse = client.execute(httpLoginRequest);
        } catch (IOException e) {
            logger.error("Error while sending HTTP Post request to server, Server URL : " + serverURL
                    + ", HTTP Request : " + httpLoginRequest, e);
            throw new AppFactoryServerException("Unable to login user, Details : " + loginRequestParams, e);
        }

        if (200 == httpLoginResponse.getStatusLine().getStatusCode()) {    //If response status is 200(OK)
            //Extracting cookie value from response
            cookie = httpLoginResponse.getFirstHeader("Set-Cookie").getValue().split(";")[0];

            HttpEntity loginResponseEntity = httpLoginResponse.getEntity();
            BufferedReader bufferedReader;
            StringBuilder responseBuilder = new StringBuilder();
            String responseLine;

            try {
                bufferedReader = new BufferedReader(new InputStreamReader(loginResponseEntity.getContent()));
            } catch (IOException e) {
                logger.error("Unable to create an input stream from HTTP response received", e);
                throw new AppFactoryServerException("Unable to login user, Details : " + loginRequestParams, e);
            }

            try {
                while ((responseLine = bufferedReader.readLine()) != null) {
                    responseBuilder.append(responseLine);
                }
            } catch (IOException e) {
                logger.error("Error in reading login response", e);
                throw new AppFactoryServerException("Unable to login user, Details : " + loginRequestParams, e);
            }
            successLogin = responseBuilder.toString();

            if ("true".equals(successLogin)) {
                loginResponse.setLoggedIn(true);
                loginResponse.setErroneousRequest(false);
            } else {
                loginResponse.setLoggedIn(false);
                loginResponse.setErroneousRequest(true);
                loginResponse.setErrorType(ErrorType.INVALID_CREDENTIALS);
            }

            try {
                EntityUtils.consume(loginResponseEntity);
            } catch (IOException e) {
                logger.error("Error in closing Entity stream of App Factory login response", e);
            }

        } else {
            loginResponse.setLoggedIn(false);
            loginResponse.setErroneousRequest(true);
            loginResponse.setErrorType(ErrorType.CONNECTION_FAILURE);
        }


        return loginResponse;
    }

    /**
     * Wrap the HTTP client to be used as a secure client
     *
     * @param baseClient HttpClient to be wrapped
     * @param serverURL  Server URL
     * @return Wrapped HttpClient object
     */
    public static HttpClient wrapClient(HttpClient baseClient, String serverURL) throws AppFactoryServerException {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Unable to create an SSL context for TLS provider", e);
            throw new AppFactoryServerException("Error in wrapping HTTP client", e);
        }
        X509TrustManager trustManager = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        try {
            sslContext.init(null, new TrustManager[]{trustManager}, null);
        } catch (KeyManagementException e) {
            logger.error("Error when initializing context with trust manager", e);
            throw new AppFactoryServerException("Error in wrapping HTTP client", e);
        }
        SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        ClientConnectionManager clientConnManager = new ThreadSafeClientConnManager();
        SchemeRegistry schemeRegistry = clientConnManager.getSchemeRegistry();
        URL url;
        try {
            url = new URL(serverURL);
        } catch (MalformedURLException e) {
            logger.error("Can't create a URL instance with given server URL, URL : " + serverURL, e);
            throw new AppFactoryServerException("Error in wrapping HTTP client", e);
        }
        int port = url.getPort();
        String protocol = url.getProtocol();
        schemeRegistry.register(new Scheme(protocol, socketFactory, port));
        return new DefaultHttpClient(clientConnManager, baseClient.getParams());
    }

    /**
     * Creates an HTTP Entity using given request parameters
     *
     * @param requestParams Map that contains request params as key value pairs
     * @return Populated HTTPEntity object
     * @throws java.io.UnsupportedEncodingException
     */
    private static HttpEntity createEntityFromRequestParams(Map<String, String> requestParams) throws UnsupportedEncodingException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Set<String> keySet = requestParams.keySet();

        for (String key : keySet) {
            nameValuePairs.add(new BasicNameValuePair(key, requestParams.get(key)));
        }

        return new UrlEncodedFormEntity(nameValuePairs);
    }

}
