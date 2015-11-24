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
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.carbon.pc.analytics.core.clients.AnalyticsRestClient;
import org.wso2.carbon.pc.analytics.core.models.AggregateField;
import org.wso2.carbon.pc.analytics.core.models.AggregateQuery;
import org.wso2.carbon.pc.analytics.core.models.SearchQuery;
import org.wso2.carbon.pc.analytics.core.utils.Helper;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * ProcessLevelMonitor keeps all the functionalites for the process level monitoring
 */
public class ProcessLevelMonitor {
	private static final Log log = LogFactory.getLog(ProcessLevelMonitor.class);

	/**
	 * perform query: SELECT processDefinitionId, AVG(duration) AS avgExecutionTime FROM
	 *                PROCESS_USAGE_SUMMARY WHERE <date range> GROUP BY processDefinitionId;
	 * @param filters is a given date range represents as the JSON string
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsProcessId(String filters) {
		String sortedResult = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			long from = filterObj.getLong("startTime");
			long to = filterObj.getLong("endTime");
			String order = filterObj.getString("order");
			int processCount = filterObj.getInt("count");

			AggregateField avgField = new AggregateField();
			avgField.setFieldName(AnalyticConstants.DURATION);
			avgField.setAggregate(AnalyticConstants.AVG);
			avgField.setAlias(AnalyticConstants.AVG_EXECUTION_TIME);

			ArrayList<AggregateField> aggregateFields = new ArrayList<>();
			aggregateFields.add(avgField);

			AggregateQuery query = new AggregateQuery();
			query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
			query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
			if(from != 0 && to != 0){
				query.setQuery(
						Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
			}
			query.setAggregateFields(aggregateFields);


			if (log.isDebugEnabled()) {
				log.debug(Helper.getJSONString(query));
			}

			String result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));

			JSONArray unsortedResultArray = new JSONArray(result);
			Hashtable<String , Double> table = new Hashtable<>();

			if(unsortedResultArray.length() != 0){
				for(int i = 0 ; i < unsortedResultArray.length() ; i++){
					JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
					JSONObject values = jsonObj.getJSONObject("values");
					String processDefKey = values.getJSONArray("processDefKey").getString(0);
					double avgExecTime = values.getDouble("avgExecutionTime");
					table.put(processDefKey, avgExecTime);
				}
				sortedResult = Helper.getDoubleValueSortedList(table, "processDefKey",
				                                               "avgExecutionTime", order,
				                                               processCount);
			}

		} catch (Exception e) {
			String errMsg = "PC Analytics core ProcessLevelMonitoring error.";
			log.error(errMsg, e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Result = " + sortedResult);
		}

		return sortedResult;
	}

	/**
	 * perform query: SELECT processDefinitionId, COUNT(processInstanceId) AS processInstanceCount
	 *                FROM PROCESS_USAGE_SUMMARY WHERE <date range> GROUP BY processDefinitionId;
	 * @param filters is a given date range represents as the JSON string
	 * @return the result as a JSON string
	 */
	public String getProcessInstanceCountVsProcessId(String filters) {
		String sortedResult = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			long from = filterObj.getLong("startTime");
			long to = filterObj.getLong("endTime");
			String order = filterObj.getString("order");
			int processCount = filterObj.getInt("count");

			AggregateField countField = new AggregateField();
			countField.setFieldName(AnalyticConstants.ALL);
			countField.setAggregate(AnalyticConstants.COUNT);
			countField.setAlias(AnalyticConstants.PROCESS_INSTANCE_COUNT);

			ArrayList<AggregateField> aggregateFields = new ArrayList<>();
			aggregateFields.add(countField);

			AggregateQuery query = new AggregateQuery();
			query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
			query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
			if(from != 0 && to != 0){
				query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
			}
			query.setAggregateFields(aggregateFields);

			if(log.isDebugEnabled()){
				log.debug(Helper.getJSONString(query));
			}

			String result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));

			JSONArray unsortedResultArray = new JSONArray(result);
			Hashtable<String , Integer> table = new Hashtable<>();

			if(unsortedResultArray.length() != 0){
				for(int i = 0 ; i < unsortedResultArray.length() ; i++){
					JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
					JSONObject values = jsonObj.getJSONObject("values");
					String processDefKey = values.getJSONArray("processDefKey").getString(0);
					int processInstanceCount = values.getInt("processInstanceCount");
					table.put(processDefKey, processInstanceCount);
				}
				sortedResult = Helper.getIntegerValueSortedList(table, "processDefKey", "processInstanceCount", order, processCount);
			}

		} catch (Exception e) {
			String errMsg = "PC Analytics core ProcessLevelMonitoring error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + sortedResult);
		}

		return sortedResult;
	}

	/**
	 * perform query: SELECT processVersion, AVG(duration) AS avgExecutionTime FROM
	 *                PROCESS_USAGE_SUMMARY WHERE <date range> AND <processId> GROUP BY
	 *                processVersion;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsProcessVersion(String filters) {
		String result = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			long from = filterObj.getLong("startTime");
			long to = filterObj.getLong("endTime");
			String processId = filterObj.getString("processId");

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
			               + " AND " + "processDefinitionId:" + "\"'" + processId + "'\"");
			query.setAggregateFields(aggregateFields);

			if(log.isDebugEnabled()){
				log.debug(Helper.getJSONString(query));
			}

			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (Exception e) {
			String errMsg = "PC Analytics core ProcessLevelMonitoring error.";
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
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getProcessInstanceCountVsProcessVersion(String filters) {
		String result = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			//long from = filterObj.getLong("startTime");
			//long to = filterObj.getLong("endTime");
			String processId = filterObj.getString("processId");

			AggregateField countField = new AggregateField();
			countField.setFieldName(AnalyticConstants.ALL);
			countField.setAggregate(AnalyticConstants.COUNT);
			countField.setAlias(AnalyticConstants.PROCESS_INSTANCE_COUNT);

			ArrayList<AggregateField> aggregateFields = new ArrayList<>();
			aggregateFields.add(countField);

			AggregateQuery query = new AggregateQuery();
			query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
			query.setGroupByField(AnalyticConstants.PROCESS_VERSION);
			query.setQuery("processDefinitionId:" + "\"'" + processId + "'\"");
			query.setAggregateFields(aggregateFields);

			if(log.isDebugEnabled()){
				log.debug(Helper.getJSONString(query));
			}

			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (Exception e) {
			String errMsg = "PC Analytics core ProcessLevelMonitoring error.";
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
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getExecutionTimeVsProcessInstanceId(String filters) {
		String result = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			long from = filterObj.getLong("startTime");
			long to = filterObj.getLong("endTime");
			String processId = filterObj.getString("processId");
			int limit = filterObj.getInt("limit");

			SearchQuery searchQuery = new SearchQuery();
			searchQuery.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
			searchQuery.setQuery("processDefinitionId:" + "\"'" + processId + "'\"" + " AND " +
			                     Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from,
			                                              to));
			searchQuery.setStart(0);
			searchQuery.setCount(limit);

			if(log.isDebugEnabled()){
				log.debug(Helper.getJSONString(searchQuery));
			}

			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_SEARCH),
			                                  Helper.getJSONString(searchQuery));
		} catch (Exception e) {
			String errMsg = "PC Analytics core ProcessLevelMonitoring error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}

//	public String getAvgExecuteTimeVsProcessId(String dateRange) {
//		String output = "";
//		try {
//			JSONObject dateRangeObj = new JSONObject(dateRange);
//			long from = dateRangeObj.getLong("startTime");
//			long to = dateRangeObj.getLong("endTime");
//
//			AggregateField avgField = new AggregateField();
//			avgField.setFieldName("duration");
//			avgField.setAggregate("AVG");
//			avgField.setAlias("avgExecutionTime");
//
//			ArrayList<AggregateField> aggregateFields = new ArrayList<AggregateField>();
//			aggregateFields.add(avgField);
//
//			AggregateQuery query = new AggregateQuery();
//			query.setTableName("PROCESS_USAGE_SUMMARY_DATA");
//			query.setGroupByField("processDefKey");
//			query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
//			query.setAggregateFields(aggregateFields);
//
//			if(log.isDebugEnabled()){
//				log.debug(Helper.getJSONString(query));
//			}
//
//			output = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
//			                                  Helper.getJSONString(query));
//
//			if(log.isDebugEnabled()){
//				log.debug("Query = " + query.getQuery());
//				log.debug("Result = " + output);
//			}
//
//		} catch (Exception e) {
//			String errMsg = "PC Analytics core error.";
//			log.error(errMsg, e);
//		}
//		return output;
//	}
}
