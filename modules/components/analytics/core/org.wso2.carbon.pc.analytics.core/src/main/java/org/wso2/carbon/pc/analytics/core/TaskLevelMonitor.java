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
 * TaskLevelMonitor keeps all the functionalites for the task level monitoring
 */
public class TaskLevelMonitor {
	private static final Log log = LogFactory.getLog(TaskLevelMonitor.class);

	/**
	 * perform query: SELECT taskDefinitionKey, AVG(duration) AS avgExecutionTime FROM
	 *                TASK_USAGE_SUMMARY GROUP BY taskDefinitionKey;
	 * @param processId process definition key
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsTaskId(String processId) {
		AggregateField avgField = new AggregateField();
		avgField.setFieldName(AnalyticConstants.DURATION);
		avgField.setAggregate(AnalyticConstants.AVG);
		avgField.setAlias(AnalyticConstants.AVG_EXECUTION_TIME);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(avgField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.TASK_DEFINITION_KEY);
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
	 * perform query: SELECT taskDefinitionKey, COUNT(taskInstanceId) AS taskInstanceCount FROM
	 *                TASK_USAGE_SUMMARY GROUP BY taskDefinitionKey;
	 * @param processId process definition key
	 * @return the result as a JSON string
	 */
	public String getTaskInstanceCountVsTaskId(String processId) {
		AggregateField countField = new AggregateField();
		countField.setFieldName(AnalyticConstants.ALL);
		countField.setAggregate(AnalyticConstants.COUNT);
		countField.setAlias(AnalyticConstants.TASK_INSTANCE_COUNT);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(countField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.TASK_DEFINITION_KEY);
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
	 * perform query: SELECT assignee, COUNT(taskInstanceId) AS taskInstanceCount FROM
	 *                TASK_USAGE_SUMMARY GROUP BY assignee;
	 * @param taskId task definition key
	 * @return the result as a JSON string
	 */
	public String getTaskInstanceCountVsUserId(String taskId) {
		AggregateField countField = new AggregateField();
		countField.setFieldName(AnalyticConstants.ALL);
		countField.setAggregate(AnalyticConstants.COUNT);
		countField.setAlias(AnalyticConstants.TASK_INSTANCE_COUNT);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(countField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.ASSIGN_USER);
		query.setQuery("taskDefinitionKey:" + taskId);
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
	 * perform query: SELECT assignee, AVG(duration) AS avgExecutionTime FROM
	 *                TASK_USAGE_SUMMARY GROUP BY assignee;
	 * @param taskId task definition key
	 * @return the result as a JSON string
	 */
	public String getAvgWaitingTimeVsUserId(String taskId) {
		AggregateField avgField = new AggregateField();
		avgField.setFieldName(AnalyticConstants.DURATION);
		avgField.setAggregate(AnalyticConstants.AVG);
		avgField.setAlias(AnalyticConstants.AVG_WAITING_TIME);

		ArrayList<AggregateField> aggregateFields = new ArrayList<>();
		aggregateFields.add(avgField);

		AggregateQuery query = new AggregateQuery();
		query.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
		query.setGroupByField(AnalyticConstants.ASSIGN_USER);
		query.setQuery("taskDefinitionKey:" + taskId);
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
	 * perform query: SELECT DISTINCT taskInstanceId, duration FROM TASK_USAGE_SUMMARY;
	 * @param taskId task definition key
	 * @param from is the long value of a given date
	 * @param to is the long value of a given date
	 * @return the result as a JSON string
	 */
	public String getExectutionTimeVsTaskInstanceId(String taskId, long from, long to) {
		// need to pass start and count attributes to this method
		SearchQuery searchQuery = new SearchQuery();
		searchQuery.setTableName("TASK_USAGE_SUMMARY_DATA");
		searchQuery.setQuery(Helper.getDateRangeQuery("taskDefinitionKey:" + taskId + " AND " +
		                                              AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
		searchQuery.setStart(0);
		searchQuery.setCount(100);

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
}
