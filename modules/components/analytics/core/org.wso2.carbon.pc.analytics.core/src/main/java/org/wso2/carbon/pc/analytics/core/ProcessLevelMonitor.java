/**
 * Copyright 2005-2015 WSO2, Inc. (http://wso2.com)
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
package org.wso2.carbon.pc.analytics.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.pc.analytics.core.clients.AnalyticsRestClient;
import org.wso2.carbon.pc.analytics.core.models.AggregateField;
import org.wso2.carbon.pc.analytics.core.models.AggregateQuery;
import org.wso2.carbon.pc.analytics.core.models.SearchQuery;
import org.wso2.carbon.pc.analytics.core.utils.Helper;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ProcessLevelMonitor keeps all the functionalites for the process level monitoring
 */
public class ProcessLevelMonitor {
	private static final Log log = LogFactory.getLog(ProcessLevelMonitor.class);

	/**
	 * perform query: SELECT processDefinitionId, AVG(duration) AS avgExecutionTime FROM
	 *                PROCESS_USAGE_SUMMARY WHERE <date range> GROUP BY processDefinitionId;
	 * @param from is the long value of a given date
	 * @param to is the long value of a given date
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsProcessId(long from, long to) {
		AggregateField avgField = new AggregateField();
		avgField.setFieldName(AnalyticConstants.DURATION);
		avgField.setAggregate(AnalyticConstants.AVG);
		avgField.setAlias(AnalyticConstants.AVG_EXECUTION_TIME);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(avgField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
		query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
		query.setAggregateFields(aggregateFields);

		if (log.isDebugEnabled()) {
			log.debug(Helper.getJSONString(query));
		}

		String result = null;
		try {
			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (IOException | XMLStreamException e) {
			String errMsg = "Data Analytics Server configuration error.";
			log.error(errMsg, e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Result = " + result);
		}

		return result;
	}

	/**
	 * perform query: SELECT processDefinitionId, COUNT(processInstanceId) AS processInstanceCount
	 *                FROM PROCESS_USAGE_SUMMARY WHERE <date range> GROUP BY processDefinitionId;
	 * @param from is the long value of a given date
	 * @param to is the long value of a given date
	 * @return the result as a JSON string
	 */
	public String getProcessInstanceCountVsProcessId(long from, long to) {
		AggregateField countField = new AggregateField();
		countField.setFieldName(AnalyticConstants.ALL);
		countField.setAggregate(AnalyticConstants.COUNT);
		countField.setAlias(AnalyticConstants.PROCESS_INSTANCE_COUNT);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(countField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
		query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
		query.setAggregateFields(aggregateFields);

		if(log.isDebugEnabled()){
			log.debug(Helper.getJSONString(query));
		}

		String result = null;
		try {
			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (IOException | XMLStreamException e) {
			String errMsg = "Data Analytics Server configuration error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}

	/**
	 * perform query: SELECT processVersion, AVG(duration) AS avgExecutionTime FROM
	 *                PROCESS_USAGE_SUMMARY WHERE <date range> AND <processId> GROUP BY
	 *                processVersion;
	 * @param processId process definition key
	 * @param to is the long value of a given date
	 * @param from is the long value of a given date
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsProcessVersion(long from, long to, String processId) {
		AggregateField avgField = new AggregateField();
		avgField.setFieldName(AnalyticConstants.DURATION);
		avgField.setAggregate(AnalyticConstants.AVG);
		avgField.setAlias(AnalyticConstants.AVG_EXECUTION_TIME);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(avgField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.PROCESS_VERSION);
		query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to)
		               + " AND " + "processDefinitionId:" + processId);
		query.setAggregateFields(aggregateFields);

		if(log.isDebugEnabled()){
			log.debug(Helper.getJSONString(query));
		}

		String result = null;
		try {
			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (IOException | XMLStreamException e) {
			String errMsg = "Data Analytics Server configuration error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}

	/**
	 * perform query: SELECT processVersion, COUNT(processInstanceId) AS processInstanceCount
	 *                FROM PROCESS_USAGE_SUMMARY GROUP BY processVersion;
	 * @param processId process definition key
	 * @return the result as a JSON string
	 */
	public String getProcessInstanceCountVsProcessVersion(String processId) {
		AggregateField countField = new AggregateField();
		countField.setFieldName(AnalyticConstants.ALL);
		countField.setAggregate(AnalyticConstants.COUNT);
		countField.setAlias(AnalyticConstants.PROCESS_INSTANCE_COUNT);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(countField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.PROCESS_VERSION);
		query.setQuery("processDefinitionId:" + processId);
		query.setAggregateFields(aggregateFields);

		if(log.isDebugEnabled()){
			log.debug(Helper.getJSONString(query));
		}

		String result = null;
		try {
			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (IOException | XMLStreamException e) {
			String errMsg = "Data Analytics Server configuration error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}

	/**
	 * perform query: SELECT DISTINCT processInstanceId, duration FROM PROCESS_USAGE_SUMMARY
	 *                WHERE <date range> AND <processId>
	 * @param processId process definition key
	 * @param from is the long value of a given date
	 * @param to is the long value of a given date
	 * @param limit is used to limit the number of records to be retrieved
	 * @return the result as a JSON string
	 */
	public String getExecutionTimeVsProcessInstanceId(String processId, long from, long to, int limit) {
		SearchQuery searchQuery = new SearchQuery();
		searchQuery.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
		searchQuery.setQuery("processDefinitionId:" + processId + " AND " +
		                     Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from,
		                                              to));
		searchQuery.setStart(0);
		searchQuery.setCount(limit);

		if(log.isDebugEnabled()){
			log.debug(Helper.getJSONString(searchQuery));
		}

		String result = null;
		try {
			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_SEARCH),
			                                  Helper.getJSONString(searchQuery));
		} catch (IOException | XMLStreamException e) {
			String errMsg = "Data Analytics Server configuration error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}

	public String getAvgExecuteTimeVsProcessId() {
		AggregateField avgField = new AggregateField();
		avgField.setFieldName("duration");
		avgField.setAggregate("AVG");
		avgField.setAlias("avgExecutionTime");

		ArrayList<AggregateField> aggregateFields = new ArrayList<AggregateField>();
		aggregateFields.add(avgField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName("PROCESS_USAGE_SUMMARY_DATA");
		query.setGroupByField("processDefKey");
		query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, 1444069800000l, 1444156200000l));
		query.setAggregateFields(aggregateFields);

		if(log.isDebugEnabled()){
			log.debug(Helper.getJSONString(query));
		}

		String output = null;
		try {
			output = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (IOException | XMLStreamException e) {
			String errMsg = "Data Analytics Server configuration error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Query = " + query.getQuery());
			log.debug("Result = " + output);
		}

		return output;
	}
}
