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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.exception.AppFactoryServerException;
import org.wso2.developerstudio.codenvy.ext.appfactory.server.model.UserCredentials;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles all the App Cloud tenant related operations
 */
public class CloudAdminServiceClient {
    private static final Logger log = LoggerFactory.getLogger(CloudAdminServiceClient.class);

    private final static String AUTH_ADMIN_SERVICE = "https://identity.cloud.wso2.com/services/AuthenticationAdmin";
    private final static String USER_MGR_SERVICE = "https://identity.cloud.wso2.com/services/CloudUserManager";
    private final static String USER_MGT_SERVICE_NS = "http://manager.user.cloud.carbon.wso2.org";
    private final static String ADMIN_AUTH_SERVICE_NS = "http://authentication.services.core.carbon.wso2.org";

    /**
     * Get related tenant domains for the App Cloud user
     *
     * @param credentials user credentials
     * @return a Map contains tenants
     * @throws AppFactoryServerException
     */
    public static Map<String, String> getTenantDomains(UserCredentials credentials) throws AppFactoryServerException {
        Map<String, String> tenants = new HashMap<>();
        QName serviceName;
        QName portName;
        Service service;
        Dispatch<SOAPMessage> dispatch;
        MessageFactory messageFactory;
        SOAPElement operation;
        Map<String, Object> headers;
        Map<String, List<String>> reqHeaders;
        List<String> cookieList;
        SOAPMessage response;
        SOAPMessage request;
        SOAPBody body;
        SOAPElement user;
        NodeList tenantElements;

        serviceName = new QName(USER_MGT_SERVICE_NS, "CloudUserManager");
        portName = new QName(USER_MGT_SERVICE_NS, "CloudUserManagerHttpsSoap11Endpoint");

        service = Service.create(serviceName);
        service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, USER_MGR_SERVICE);

        dispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);

        try {
            //Creating a SOAP request
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
            request = messageFactory.createMessage();
            body = request.getSOAPPart().getEnvelope().getBody();
            operation = body.addChildElement("getTenantDisplayNames", "ns", USER_MGT_SERVICE_NS);
            user = operation.addChildElement("user");
            user.addTextNode(credentials.getTenantAwareUserName());
            request.saveChanges();
        } catch (SOAPException e) {
            log.error("Error in creating SOAP request while getting tenant domains of App CLoud", e);
            throw new AppFactoryServerException("Error occurred in App Cloud tenant handling", e);
        }

        //Manipulating the request headers with cookies
        headers = dispatch.getRequestContext();
        reqHeaders = (Map) headers.get(MessageContext.HTTP_REQUEST_HEADERS);
        if (reqHeaders == null) {
            reqHeaders = new HashMap<>();
        }
        cookieList = new ArrayList<>();
        try {
            cookieList.add(getCookie(credentials.getTenantAwareUserName(), credentials.getPassword()));
        } catch (Exception e) {
            log.error("Error while getting authentication cookies for user: " + credentials.getTenantAwareUserName());
        }
        reqHeaders.put("Cookie", cookieList);
        headers.put(MessageContext.HTTP_REQUEST_HEADERS, reqHeaders);

        //Invoking service operation
        response = dispatch.invoke(request);

        //Tenant data extraction from SOAP response
        try {
            tenantElements = response.getSOAPBody().getFirstChild().getChildNodes();
        } catch (SOAPException e) {
            log.error("Error while getting tenant elements from SOAP response", e);
            throw new AppFactoryServerException("Error while getting tenant elements from SOAP response", e);
        }
        for (int i = 0; i < tenantElements.getLength(); i++) {
            String displayName = tenantElements.item(i).getFirstChild().getTextContent();
            String domainName = tenantElements.item(i).getLastChild().getTextContent();
            tenants.put(displayName, domainName);
        }

        return tenants;
    }

    /**
     * Get admin authentication cookie for the given user
     *
     * @param userName user credentials
     * @param password password
     * @return cookie value
     * @throws AppFactoryServerException
     */
    private static String getCookie(String userName, String password) throws AppFactoryServerException {
        QName serviceName;
        QName portName;
        Service service;
        Dispatch<SOAPMessage> dispatch;
        MessageFactory messageFactory;
        SOAPElement operation;
        SOAPMessage response;
        SOAPMessage request;
        SOAPBody requestBody;
        SOAPBody responseBody;
        SOAPElement user;
        SOAPElement pwd;
        SOAPElement remoteAddr;

        serviceName = new QName(ADMIN_AUTH_SERVICE_NS, "AuthenticationAdmin");
        portName = new QName(ADMIN_AUTH_SERVICE_NS, "AuthenticationAdminHttpsSoap11Endpoint");

        service = Service.create(serviceName);
        service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, AUTH_ADMIN_SERVICE);

        dispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);

        try {
            //Creating a SOAP request
            messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
            request = messageFactory.createMessage();
            requestBody = request.getSOAPPart().getEnvelope().getBody();
            operation = requestBody.addChildElement("login", "ns", ADMIN_AUTH_SERVICE_NS);
            user = operation.addChildElement("username");
            user.addTextNode(userName);
            pwd = operation.addChildElement("password");
            pwd.addTextNode(password);
            remoteAddr = operation.addChildElement("remoteAddress");
            remoteAddr.addTextNode("wso2.org");
            request.saveChanges();
        } catch (SOAPException e) {
            log.error("Error in creating SOAP request while getting tenant domains of App CLoud", e);
            throw new AppFactoryServerException("Error occurred in App Cloud tenant handling", e);
        }

        //Invoking service operation
        response = dispatch.invoke(request);

        try {
            responseBody = response.getSOAPBody();
        } catch (SOAPException e) {
            log.error("Unable to get SOAP response body while getting cookies", e);
            throw new AppFactoryServerException("Unable to get SOAP response body while getting cookies", e);
        }

        //If the authentication success, extracting JSESSIONID cookie value
        if ("true".equals(responseBody.getFirstChild().getTextContent())) {
            List cookieList;
            Map respHeaders;
            Map<String, Object> headers;

            headers = dispatch.getResponseContext();
            respHeaders = (Map) headers.get(MessageContext.HTTP_RESPONSE_HEADERS);
            // This is the JSESSIONID cookie.
            cookieList = (List) respHeaders.get("Set-cookie");
            return (String) cookieList.get(0);
        } else {
            return null;
        }

    }
}
