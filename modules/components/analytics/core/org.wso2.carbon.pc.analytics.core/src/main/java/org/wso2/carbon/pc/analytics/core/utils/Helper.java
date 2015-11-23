/**
 * Copyright 2005-2015 WSO2, Inc. (http://wso2.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.pc.analytics.core.utils;

import com.google.gson.Gson;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.analytics.core.AnalyticConstants;
import org.wso2.carbon.utils.CarbonUtils;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Helper class is used to keep the methods which are helpful to the monitor classes.
 */
public class Helper {
	/**
	 * Build the lucene query format for the date range
	 *
	 * @param columnName to hold the column name for the task finishedTime in the table
	 * @param from       is the long value of a given date
	 * @param to         is the long value of a given date
	 * @return a query string in the format of lucene
	 */
	public static String getDateRangeQuery(String columnName, long from, long to) {
		return columnName + " : [" + from + " TO " + to + "]";
	}

	/**
	 * Convert query object as a JSON String
	 *
	 * @param query to hold the query object
	 * @return a JSON String
	 */
	public static String getJSONString(Object query) {
		return new Gson().toJson(query);
	}

	/**
	 * Get property iterator
	 *
	 * @return Iterator object
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private static Iterator getPropertyIterator() throws IOException, XMLStreamException {
		String carbonConfigDirPath = CarbonUtils.getCarbonConfigDirPath();
		String pcConfigPath =
				carbonConfigDirPath + File.separator + AnalyticConstants.PC_CONFIGURATION_FILE_NAME;
		File configFile = new File(pcConfigPath);
		String configContent = FileUtils.readFileToString(configFile);
		OMElement configElement = AXIOMUtil.stringToOM(configContent);
		OMElement firstChild = configElement.getFirstChildWithName(
				new QName(AnalyticConstants.PC_NAMESPACE, AnalyticConstants.ANALYTICS));
		Iterator properties =
				firstChild.getChildrenWithName(new QName(null, AnalyticConstants.PROPERTY));
		return properties;
	}

	/**
	 * Build and return the DAS rest API urls
	 *
	 * @param path hold the relative path to a particular webservice
	 * @return the base url of DAS
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static String getURL(String path) throws IOException, XMLStreamException {
		Iterator properties = getPropertyIterator();
		while (properties.hasNext()) {
			OMElement property = (OMElement) properties.next();
			if (AnalyticConstants.CONFIG_BASE_URL
					.equals(property.getAttributeValue(new QName(null, AnalyticConstants.NAME)))) {
				String baseUrl =
						property.getAttributeValue(new QName(null, AnalyticConstants.VALUE));
				if (baseUrl != null && !baseUrl.isEmpty()) {
					if (!baseUrl.endsWith(File.separator)) {
						baseUrl += File.separator;
					}
					return baseUrl + path;
				}
			}
		}
		return null;
	}

	/**
	 * Get authorization header
	 *
	 * @return encoded auth header
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	public static String getAuthorizationHeader() throws IOException, XMLStreamException {
		String requestHeader = "Basic ";
		String userName = null;
		String password = null;
		Iterator properties = getPropertyIterator();

		while (properties.hasNext()) {
			if (userName != null && password != null) {
				break;
			}
			OMElement property = (OMElement) properties.next();
			if (AnalyticConstants.CONFIG_USER_NAME
					.equals(property.getAttributeValue(new QName(null, AnalyticConstants.NAME)))) {
				String name = property.getAttributeValue(new QName(null, AnalyticConstants.VALUE));
				if (name != null && !name.isEmpty()) {
					userName = name;
				}
			}
			if (AnalyticConstants.CONFIG_PASSWORD
					.equals(property.getAttributeValue(new QName(null, AnalyticConstants.NAME)))) {
				String pwd = property.getAttributeValue(new QName(null, AnalyticConstants.VALUE));
				if (pwd != null && !pwd.isEmpty()) {
					password = pwd;
				}
			}
		}

		if (userName != null && password != null) {
			String headerPortion = userName + ":" + password;
			byte[] encodedBytes = headerPortion.getBytes("UTF-8");
			String encodedString = DatatypeConverter.printBase64Binary(encodedBytes);
			requestHeader += encodedString;
			return requestHeader;
		}
		return null;
	}

	/**
	 * Get sorted list
	 * @param table is a hash table to keep the result as key-value pairs
	 * @param key1 is the name for the first value of the JSON object
	 * @param key2 is the name for the second value for the JSON object
	 * @return a sorted list as a JSON array string
	 * @throws JSONException
	 */
	public static String getDoubleValueSortedList(Hashtable<String, Double> table, String key1, String key2)
																			throws JSONException {
		//Transfer as List and sort it
		ArrayList<Map.Entry<String, Double>> l = new ArrayList(table.entrySet());
		Collections.sort(l, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		JSONArray array = new JSONArray();
		for (int i = 0 ; i < l.size() ; i++){
			JSONObject o = new JSONObject();
			o.put(key1, l.get(i).getKey());
			o.put(key2, l.get(i).getValue());
			array.put(o);
		}
		return array.toString();
	}

	/**
	 * Get sorted list
	 * @param table is a hash table to keep the result as key-value pairs
	 * @param key1 is the name for the first value of the JSON object
	 * @param key2 is the name for the second value for the JSON object
	 * @return a sorted list as a JSON array string
	 * @throws JSONException
	 */
	public static String getIntegerValueSortedList(Hashtable<String, Integer> table, String key1, String key2)
																			throws JSONException {
		ArrayList<Map.Entry<String, Integer>> l = new ArrayList(table.entrySet());
		Collections.sort(l, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		JSONArray array = new JSONArray();
		for (int i = 0 ; i < l.size() ; i++){
			JSONObject o = new JSONObject();
			o.put(key1, l.get(i).getKey());
			o.put(key2, l.get(i).getValue());
			array.put(o);
		}
		return array.toString();
	}
}
