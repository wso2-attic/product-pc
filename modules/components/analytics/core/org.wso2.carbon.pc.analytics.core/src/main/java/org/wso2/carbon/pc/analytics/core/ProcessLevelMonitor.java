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
import org.wso2.carbon.pc.analytics.core.utils.AnalyticsUtils;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * ProcessLevelMonitor holds all the functionalites for the process level monitoring
 */
public class ProcessLevelMonitor {
	private static final Log log = LogFactory.getLog(ProcessLevelMonitor.class);

	/**
	 * perform query: SELECT processDefinitionId, AVG(duration) AS avgExecutionTime FROM
	 *                PROCESS_USAGE_SUMMARY WHERE <date range> GROUP BY processDefinitionId;
	 *
	 * @param filters is a given date range represents as the JSON string
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsProcessId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticsConstants.DURATION);
				avgField.setAggregate(AnalyticsConstants.AVG);
				avgField.setAlias(AnalyticsConstants.AVG_EXECUTION_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticsConstants.PROCESS_DEFINITION_KEY);
				if (from != 0 && to != 0) {
					query.setQuery(AnalyticsUtils.getDateRangeQuery(AnalyticsConstants.COLUMN_FINISHED_TIME, from, to));
				}
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
				                                         AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Double> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						String processDefKey = values.getJSONArray("processDefKey").getString(0);
						double avgExecTime = values.getDouble("avgExecutionTime");
						table.put(processDefKey, avgExecTime);
					}
					sortedResult = AnalyticsUtils.getDoubleValueSortedList(table, "processDefKey", "avgExecutionTime",
					                                                       order, processCount);
				}
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
	 *
	 * @param filters is a given date range represents as the JSON string
	 * @return the result as a JSON string
	 */
	public String getProcessInstanceCountVsProcessId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticsConstants.PROCESS_DEFINITION_KEY);
				if (from != 0 && to != 0) {
					query.setQuery(AnalyticsUtils.getDateRangeQuery(
							AnalyticsConstants.COLUMN_FINISHED_TIME, from, to));
				}
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
				                                         AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Integer> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						String processDefKey = values.getJSONArray("processDefKey").getString(0);
						int processInstanceCount = values.getInt("processInstanceCount");
						table.put(processDefKey, processInstanceCount);
					}
					sortedResult = AnalyticsUtils.getIntegerValueSortedList(table, "processDefKey", "processInstanceCount",
					                                                        order, processCount);
				}
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
	 * perform query: SELECT processVersion, AVG(duration) AS avgExecutionTime FROM
	 *                PROCESS_USAGE_SUMMARY WHERE <date range> AND <processId> GROUP BY
	 *                processVersion;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsProcessVersion(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticsConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticsConstants.DURATION);
				avgField.setAggregate(AnalyticsConstants.AVG);
				avgField.setAlias(AnalyticsConstants.AVG_EXECUTION_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticsConstants.PROCESS_VERSION);
				query.setQuery("processDefinitionId:" + "\"'" + processId + "'\"");
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
				                                         AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Double> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						String processVersion = values.getJSONArray("processVer").getString(0);
						double avgExecTime = values.getDouble("avgExecutionTime");
						table.put(processVersion, avgExecTime);
					}
					sortedResult = AnalyticsUtils.getDoubleValueSortedList(table, "processVer", "avgExecutionTime",
					                                                       order, processCount);
				}
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
	 * perform query: SELECT processVersion, COUNT(processInstanceId) AS processInstanceCount
	 *                FROM PROCESS_USAGE_SUMMARY GROUP BY processVersion;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getProcessInstanceCountVsProcessVersion(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticsConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticsConstants.PROCESS_VERSION);
				query.setQuery("processDefinitionId:" + "\"'" + processId + "'\"");
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
				                                         AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Integer> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						String processVersion = values.getJSONArray("processVer").getString(0);
						int processInstanceCount = values.getInt("processInstanceCount");
						table.put(processVersion, processInstanceCount);
					}
					sortedResult = AnalyticsUtils.getIntegerValueSortedList(table, "processVer", "processInstanceCount",
					                                                        order, processCount);
				}
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
	 * perform query: SELECT DISTINCT processInstanceId, duration FROM PROCESS_USAGE_SUMMARY
	 *                WHERE <processId> AND <date range>
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getExecutionTimeVsProcessInstanceId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				String processId = filterObj.getString(AnalyticsConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int limit = filterObj.getInt(AnalyticsConstants.LIMIT);

				SearchQuery searchQuery = new SearchQuery();
				searchQuery.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				String queryStr = "processDefinitionId:" + "\"'" + processId + "'\"";
				if (from != 0 && to != 0) {
					queryStr += " AND " + AnalyticsUtils.getDateRangeQuery(AnalyticsConstants.COLUMN_FINISHED_TIME,
					                                                       from, to);
				}
				searchQuery.setQuery(queryStr);
				searchQuery.setStart(AnalyticsConstants.MIN_COUNT);
				searchQuery.setCount(AnalyticsConstants.MAX_COUNT);

				if (log.isDebugEnabled()) {
					log.debug(AnalyticsUtils.getJSONString(searchQuery));
				}

				String result = AnalyticsRestClient.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_SEARCH),
				                                         AnalyticsUtils.getJSONString(searchQuery));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Double> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						String processDefKey = values.getString("processInstanceId");
						double executionTime = values.getDouble("duration");
						table.put(processDefKey, executionTime);
					}
					sortedResult = AnalyticsUtils.getDoubleValueSortedList(table, "processInstanceId", "duration",
					                                                       order, limit);
				}
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
	 * Perform query: SELECT DISTINCT finishTime, COUNT(*) AS processInstanceCount FROM
	 *                PROCESS_USAGE_SUMMARY WHERE <date range> AND <process id list>
	 *                GROUP BY finishTime
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getDateVsProcessInstanceCount(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				JSONArray processIdList = filterObj.getJSONArray(AnalyticsConstants.PROCESS_ID_LIST);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticsConstants.FINISHED_TIME);
				String queryStr = AnalyticsUtils.getDateRangeQuery(AnalyticsConstants.COLUMN_FINISHED_TIME, from, to);

				if (processIdList.length() != 0) {
					queryStr += " AND ";
					for (int i = 0; i < processIdList.length(); i++) {
						if (i == 0) {
							queryStr += "(processDefinitionId:" + "\"'" + processIdList.getString(i) + "'\"";
						} else {
							queryStr += " OR " + "processDefinitionId:" + "\"'" + processIdList.getString(i) + "'\"";
						}
						if (i == processIdList.length() - 1) {
							queryStr += ")";
						}
					}
				}
				query.setQuery(queryStr);
				query.setAggregateFields(aggregateFields);

				String result = AnalyticsRestClient.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
				                                         AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<Long, Integer> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						long completedTime = Long.parseLong(values.getJSONArray("finishTime").getString(0));
						int processInstanceCount = values.getInt("processInstanceCount");
						table.put(completedTime, processInstanceCount);
					}
					sortedResult = AnalyticsUtils.getLongKeySortedList(table, "finishTime", "processInstanceCount");
				}
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
	 * Get process definition key list
	 *
	 * @return process definition key list as a JSON array string
	 */
	public String getProcessIdList() {
		String processIdList = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticsConstants.ALL);
				avgField.setAggregate(AnalyticsConstants.COUNT);
				avgField.setAlias(AnalyticsConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticsConstants.PROCESS_DEFINITION_KEY);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
				                                         AnalyticsUtils.getJSONString(query));

				JSONArray array = new JSONArray(result);
				JSONArray resultArray = new JSONArray();

				if (array.length() != 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObj = array.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						String processDefKey = values.getJSONArray("processDefKey").getString(0);
						JSONObject o = new JSONObject();
						o.put("processDefKey", processDefKey);
						resultArray.put(o);
					}
					processIdList = resultArray.toString();
				}

				if (log.isDebugEnabled()) {
					log.debug("Query = " + query.getQuery());
					log.debug("Result = " + processIdList);
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core - process id list error.";
			log.error(errMsg, e);
		}
		return processIdList;
	}
}
