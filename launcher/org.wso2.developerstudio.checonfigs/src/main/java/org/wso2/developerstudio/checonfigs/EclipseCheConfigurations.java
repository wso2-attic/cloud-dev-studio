/*
 * Copyright (c) 2014-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.developerstudio.checonfigs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Properties;

/**
 * changes the ports of eclipse che, eclipse che uses default tomcat port in cloud instance
 * for desktop as 8080 is a commonly used port, replace it with a rarely used on build time
 * ----------------------------------------------------------------------------------
 * in runtime change it dynamically only if the build time assigned port is occupied
 */
public class EclipseCheConfigurations {

	private static final Logger log = LoggerFactory.getLogger(EclipseCheConfigurations.class);


	public static void main(String[] args) throws Exception {
		String developerStudioPort = args[0]; //dev studio startup port value
		String shutDownPort = args[1]; //dev studio shutdown port
		String sdkRootDirectory = args[2]; // developer studio root location
		editForCheTemplateLoc(sdkRootDirectory);
		if (!changeRunningPort(developerStudioPort, shutDownPort, sdkRootDirectory, Constants.DEFAULT_PORT)) {
			log.error("Could not change the port value in tomcat ");
			throw new Exception(); //to ensure build fails on eclipse che default ports change failure
		}

	}

	/**
	 * changes the startup and shutdown port parameters in the distributed tomcat in Eclipse che
	 * since the default 8080 is a commonly used port
	 *
	 * @param rootDir      developer studio root directory
	 * @param startUpPort  the port for developer studio startup
	 * @param shutDownPort port for developer studio shutdown
	 * @return status success/failure
	 */
	private static boolean modifyServerXML(String rootDir, String startUpPort, String shutDownPort) {
		String filePath = rootDir + Constants.CONF_SERVER_XML_LOC;
		File serverXML = new File(filePath);
		if (serverXML.exists()) {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(filePath);
				Node connectorNode = doc.getElementsByTagName(Constants.CONNECTOR_XML_TAG).item(0);
				NamedNodeMap connectorAttr = connectorNode.getAttributes();
				Node connectorPortAttr = connectorAttr.getNamedItem(Constants.PORT_ATTRIBUTE);
				connectorPortAttr.setTextContent(startUpPort);

				Node serverNode = doc.getElementsByTagName(Constants.SERVER_TAG).item(0);
				NamedNodeMap serverAttr = serverNode.getAttributes();
				Node shutdownPortAttr = serverAttr.getNamedItem(Constants.PORT_ATTRIBUTE);
				shutdownPortAttr.setTextContent(shutDownPort);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(filePath));
				transformer.transform(source, result);

				return true;
			} catch (TransformerException | IOException | SAXException | ParserConfigurationException e) {
				log.error("could not change the tomcat port", e);
				return false;
			}
		} else {
			log.error("could not change the tomcat port, server xml file was not found");
			return false;
		}

	}

	/**
	 * changes the default port in which eclipse che runs, because 8080 is a commonly used port
	 *
	 * @param fileName      the name of the file to edit (sh file and bat file for che configuration)
	 * @param lineToReplace the line with port value to replace
	 * @param newLine       the new line with port value to write
	 * @return success/failure status
	 */
	private static boolean replaceCheServerPort(String fileName, String lineToReplace, String newLine) {
		BufferedReader bufferedReader = null;
		FileOutputStream fileOutputStream = null;
		String line;
		String input = "#";
		try {
			File fileToChange = new File(fileName);
			if (fileToChange.exists()) {
				bufferedReader = new BufferedReader(new FileReader(fileName));
				while ((line = bufferedReader.readLine()) != null) {
					input += line + '\n';
				}
				input = input.replace(lineToReplace, newLine);
				fileOutputStream = new FileOutputStream(fileName);
				fileOutputStream.write(input.getBytes());
				return true;
			} else {
				log.error("could not change the eclipse che port in {}, file does not exist ", fileName);
				return false;
			}
		} catch (IOException e) {
			log.error("could not change the eclipse che port in {} file", fileName, e);
			return false;
		} finally {
			try {
				if (null != bufferedReader) {
					bufferedReader.close();
				}
				if (null != fileOutputStream) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				return false;
			}
		}
	}

	/**
	 * for runtime dynamic port change in case the build time assigned port being occupied
	 *
	 * @param startUpPort      developer studio startup port
	 * @param shutDownPort     developer studio shutdown port
	 * @param sdkRootDirectory developer studio root directory
	 * @param currentPort      the existing port value that needs to be replaced
	 * @return success/failure status
	 */
	public static boolean changeRunningPort(String startUpPort, String shutDownPort, String sdkRootDirectory,
	                                        String currentPort) {
		if (!modifyServerXML(sdkRootDirectory, startUpPort, shutDownPort) ||
		    !replaceCheServerPort(sdkRootDirectory + Constants.BIN_SETENV_SH_LOC,
		                          Constants.EXPORT_SERVER_PORT + currentPort,
		                          Constants.EXPORT_SERVER_PORT + startUpPort) ||
		    !replaceCheServerPort(sdkRootDirectory + Constants.BIN_SETENV_BAT_LOC,
		                          Constants.SET_SERVER_PORT + currentPort, Constants.SET_SERVER_PORT + startUpPort)) {
			log.info("Could not change the port values configuration files for port change ");
			return false;
		} else {
			return true;
		}
	}

	/**
	 * edit the codenvy-api-properties file for che project template location set
	 * @throws IOException
	 */
	public static void editForCheTemplateLoc(String rootDir) throws IOException {
		Properties properties = loadPropertiesFromFile(rootDir + Constants.CODENVY_API_PROPERTIES_FILE);
		String propertyValue = Constants.CATALINA_BASE_TEMP_CHE_TEMPLATES;
		String propertyKey = Constants.PROJECT_TEMPLATE_DESCRIPTIONS_DIR;
		properties.setProperty(propertyKey, propertyValue);
		storePropertiesToFile(rootDir + Constants.CODENVY_API_PROPERTIES_FILE, properties);
	}

	/**
	 * Reads a property file and build a map
	 *
	 * @param fileName Name of the property file available in conf folder
	 * @return Property Map
	 * @throws IOException
	 */
	private static Properties loadPropertiesFromFile(String fileName) throws IOException {
		Properties properties;
		FileInputStream in = null;
		try {
			properties = new Properties();
			in = new FileInputStream(fileName);
			properties.load(in);
		} finally {
			in.close();
		}

		return properties;
	}

	/**
	 * Writes modified properties back to a property file
	 *
	 * @param fileName   Name of the property file available in conf folder
	 * @param properties Properties map
	 * @throws IOException
	 */
	private static void storePropertiesToFile(String fileName, Properties properties) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileName);
			properties.store(out, null);
		} finally {
			out.close();
		}
	}
}
