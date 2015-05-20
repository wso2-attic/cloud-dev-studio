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
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.authentication.Authenticator;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.constants.AppFactoryAPIConstants;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.AppFactoryHTTPResponse;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.ErrorType;
import org.wso2.developerstudio.codenvy.ext.appfactory.shared.dto.LoginResponse;

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
    private static final Logger log = LoggerFactory.getLogger(HTTPAppFactoryClient.class);

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
    public static LoginResponse doLogin(String serverURL, Map<String, String> loginRequestParams)
            throws AppFactoryServerException {
        LoginResponse loginResponse = org.eclipse.che.dto.server.DtoFactory.getInstance().createDto(LoginResponse
                .class);
        String successLogin;

        //Creating an http client and wrap it
        client = HttpClientBuilder.create().build();
        client = wrapClient(client, serverURL);

        //Creating a http post request by giving server URL
        HttpPost httpLoginRequest = new HttpPost(serverURL);
        HttpResponse httpLoginResponse;

        try {
            httpLoginRequest.setEntity(createEntityFromRequestParams(loginRequestParams));
        } catch (UnsupportedEncodingException e) {
            log.error("Error in encoding login request parameters to an entity, Parameters : " + loginRequestParams, e);
            throw new AppFactoryServerException("Unable to login user, Details : " + loginRequestParams, e);
        }

        //Sending HTTP post request to AppFactory/ AppCloud
        try {
            httpLoginResponse = client.execute(httpLoginRequest);
        } catch (IOException e) {
            log.error("Error while sending HTTP Post request to server, Server URL : " + serverURL
                    + ", HTTP Request : " + httpLoginRequest, e);
            throw new AppFactoryServerException("Unable to login user, Details : " + loginRequestParams, e);
        }

        if (HttpStatus.SC_OK == httpLoginResponse.getStatusLine().getStatusCode()) {    //If response status is 200(OK)
            //Extracting cookie value from response
            cookie = httpLoginResponse.getFirstHeader("Set-Cookie").getValue().split(";")[0];

            HttpEntity loginResponseEntity = httpLoginResponse.getEntity();
            BufferedReader bufferedReader;
            StringBuilder responseBuilder = new StringBuilder();
            String responseLine;

            try {
                bufferedReader = new BufferedReader(new InputStreamReader(loginResponseEntity.getContent()));
                while ((responseLine = bufferedReader.readLine()) != null) {
                    responseBuilder.append(responseLine);
                }
            } catch (IOException e) {
                log.error("Unable to create an input stream from HTTP response received", e);
                throw new AppFactoryServerException("Unable to login user, Details : " + loginRequestParams, e);
            }

            successLogin = responseBuilder.toString();

            if ("true".equals(successLogin)) {
                loginResponse.setLoggedIn(true);
                loginResponse.setErroneousRequest(false);
            } else if ("false".equals(successLogin)) {
                loginResponse.setLoggedIn(false);
                loginResponse.setErroneousRequest(true);
                loginResponse.setErrorMessage("Invalid User Credentials");
            } else {
                loginResponse.setLoggedIn(false);
                loginResponse.setErroneousRequest(true);
                loginResponse.setErrorMessage("Invalid Response from App Factory/ App Cloud");
            }

            try {
                EntityUtils.consume(loginResponseEntity);
            } catch (IOException e) {
                log.error("Error in closing Entity stream of App Factory login response", e);
            }

        } else {
            log.error("Connection failed to App Factory/ App Cloud, Server URL: " + serverURL);
            loginResponse.setLoggedIn(false);
            loginResponse.setErroneousRequest(true);
            loginResponse.setErrorMessage("Connection failed to App Factory/ App Cloud");
        }

        return loginResponse;
    }

    /**
     * Send an HTTP Post request to App Factory/ App Cloud server and return the response from server
     *
     * @param serverURL     AppFactory/ AppCloud server URL to connect
     * @param requestParams Map that contains request params as key value pairs
     * @return String response from server
     * @throws org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException
     */
    public static AppFactoryHTTPResponse sendHTTPPostRequest(String serverURL, Map<String, String> requestParams)
            throws AppFactoryServerException {
        AppFactoryHTTPResponse appFactoryHttpResponse = org.eclipse.che.dto.server.DtoFactory.getInstance()
                .createDto(AppFactoryHTTPResponse.class);
        HttpResponse httpResponse;
        String response;

        //If the user not already authenticated, send authentication failure to client
        if (client == null || cookie == null) {
            appFactoryHttpResponse.setRequestSuccess(false);
            appFactoryHttpResponse.setErrorType(ErrorType.AUTHENTICATION_FAILURE);
            return appFactoryHttpResponse;
        }

        //Creating a http post request by giving server URL
        HttpPost httpPostRequest = new HttpPost(serverURL);
        requestParams.put(AppFactoryAPIConstants.USERNAME_PARAM, Authenticator.getInstance().getUserCredentials()
                .getUserName());

        try {
            httpPostRequest.setEntity(createEntityFromRequestParams(requestParams));
        } catch (UnsupportedEncodingException e) {
            log.error("Error in encoding login request parameters to an entity, Parameters : " + requestParams, e);
            throw new AppFactoryServerException("Error while processing HTTP Post request, Details : " +
                    requestParams, e);
        }

        //Setting HTTP cookie with the request
        httpPostRequest.setHeader("Cookie", cookie);

        //Sending HTTP post request to AppFactory/ AppCloud
        try {
            httpResponse = client.execute(httpPostRequest);
        } catch (ClientProtocolException e) {
            log.error("Error while sending HTTP Post request to server, Server URL : " + serverURL
                    + ", HTTP Request : " + httpPostRequest, e);
            throw new AppFactoryServerException("Error while processing HTTP Post request, Details : " +
                    requestParams, e);
        } catch (IOException e) {
            log.error("Error while sending HTTP Post request to server, Server URL : " + serverURL
                    + ", HTTP Request : " + httpPostRequest, e);
            appFactoryHttpResponse.setRequestSuccess(false);
            appFactoryHttpResponse.setErrorType(ErrorType.CONNECTION_FAILURE);
            return appFactoryHttpResponse;
        }

        if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {     //If response status is 200(OK)
            HttpEntity responseEntity = httpResponse.getEntity();
            StringBuilder responseBuilder = new StringBuilder();
            BufferedReader bufferedReader;
            String responseLine;

            try {
                bufferedReader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
            } catch (IOException e) {
                log.error("Unable to create an input stream from HTTP response received", e);
                throw new AppFactoryServerException("Error while processing HTTP Post request, Details : " +
                        requestParams, e);
            }

            try {
                while ((responseLine = bufferedReader.readLine()) != null) {
                    responseBuilder.append(responseLine);
                }
            } catch (IOException e) {
                log.error("Error in reading HTTP response", e);
                throw new AppFactoryServerException("Error while processing HTTP Post request, Details : " +
                        requestParams, e);
            }

            response = responseBuilder.toString();
            if ("false".equals(response)) {
                appFactoryHttpResponse.setRequestSuccess(false);
                appFactoryHttpResponse.setErrorType(ErrorType.AUTHENTICATION_FAILURE);
            } else {
                appFactoryHttpResponse.setRequestSuccess(true);
                appFactoryHttpResponse.setResponse(response);
            }

            //Make sure entity stream is fully consumed and closed
            try {
                EntityUtils.consume(responseEntity);
                responseEntity.getContent().close();
            } catch (IOException e) {
                log.error("Error in closing Entity stream of App Factory login response", e);
            }
        } else {
            appFactoryHttpResponse.setRequestSuccess(false);
            appFactoryHttpResponse.setErrorType(ErrorType.CONNECTION_FAILURE);
        }

        client.getConnectionManager().closeExpiredConnections();

        return appFactoryHttpResponse;
    }

    /**
     * Wrap the HTTP client to be used as a secure client
     *
     * @param baseClient HttpClient to be wrapped
     * @param serverURL  Server URL
     * @return Wrapped HttpClient object
     */
    private static HttpClient wrapClient(HttpClient baseClient, String serverURL) throws AppFactoryServerException {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to create an SSL context for TLS provider", e);
            throw new AppFactoryServerException("Error in wrapping HTTP client", e);
        }
        X509TrustManager trustManager = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        try {
            sslContext.init(null, new TrustManager[]{trustManager}, null);
        } catch (KeyManagementException e) {
            log.error("Error when initializing context with trust manager", e);
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
            log.error("Can't create a URL instance with given server URL, URL : " + serverURL, e);
            throw new AppFactoryServerException("Error in wrapping HTTP client", e);
        }
        int port = url.getPort();
        String protocol = url.getProtocol();
        if (port == -1) {
            if ("https".equals(protocol)) {
                port = 443;
            } else if ("http".equals(protocol)) {
                port = 80;
            }
        }
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
    private static HttpEntity createEntityFromRequestParams(Map<String, String> requestParams) throws
            UnsupportedEncodingException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Set<String> keySet = requestParams.keySet();

        for (String key : keySet) {
            nameValuePairs.add(new BasicNameValuePair(key, requestParams.get(key)));
        }

        return new UrlEncodedFormEntity(nameValuePairs);
    }

}
