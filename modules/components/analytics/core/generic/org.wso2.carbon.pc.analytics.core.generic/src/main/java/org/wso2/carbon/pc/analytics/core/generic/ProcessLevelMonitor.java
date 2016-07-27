/**
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.pc.analytics.core.generic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wso2.carbon.pc.analytics.core.generic.clients.AnalyticsRestClient;
import org.wso2.carbon.pc.analytics.core.generic.models.AggregateField;
import org.wso2.carbon.pc.analytics.core.generic.models.AggregateQuery;
import org.wso2.carbon.pc.analytics.core.generic.models.SearchQuery;
import org.wso2.carbon.pc.analytics.core.generic.utils.AnalyticsUtils;

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
					query.setQuery(AnalyticsUtils.getDateRangeQuery(
							AnalyticsConstants.COLUMN_FINISHED_TIME, from, to));
				}
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Avg Execution Time Vs ProcessId Result:" +
					          AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
						      AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Double> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						String processDefKey =
								values.getJSONArray(AnalyticsConstants.PROCESS_DEFINITION_KEY)
								      .getString(0);
						double avgExecTime =
								values.getDouble(AnalyticsConstants.AVG_EXECUTION_TIME);
						table.put(processDefKey, avgExecTime);
					}
					sortedResult = AnalyticsUtils.getDoubleValueSortedList(table,
					                                                       AnalyticsConstants.PROCESS_DEFINITION_KEY,
					                                                       AnalyticsConstants.AVG_EXECUTION_TIME,
					                                                       order, processCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core ProcessLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Avg Execution Time Vs ProcessId Result:" + sortedResult);
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
					log.debug("Query to get the Process Instance Count Vs ProcessId Result:" +
					          AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
						      AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Integer> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						String processDefKey =
								values.getJSONArray(AnalyticsConstants.PROCESS_DEFINITION_KEY)
								      .getString(0);
						int processInstanceCount =
								values.getInt(AnalyticsConstants.PROCESS_INSTANCE_COUNT);
						table.put(processDefKey, processInstanceCount);
					}
					sortedResult = AnalyticsUtils.getIntegerValueSortedList(table,
					                                                        AnalyticsConstants.PROCESS_DEFINITION_KEY,
					                                                        AnalyticsConstants.PROCESS_INSTANCE_COUNT,
					                                                        order, processCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core ProcessLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Process Instance Count Vs ProcessId Result:" + sortedResult);
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
				String processKey = filterObj.getString(AnalyticsConstants.PROCESS_KEY);
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
				query.setQuery("processKeyName:" + "\"'" + processKey + "'\"");
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Avg Execution Time Vs Process Version Result:" +
					          AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
						      AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Double> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						String processVersion =
								values.getJSONArray(AnalyticsConstants.PROCESS_VERSION)
								      .getString(0);
						double avgExecTime =
								values.getDouble(AnalyticsConstants.AVG_EXECUTION_TIME);
						table.put(processVersion, avgExecTime);
					}
					sortedResult = AnalyticsUtils
							.getDoubleValueSortedList(table, AnalyticsConstants.PROCESS_VERSION,
							                          AnalyticsConstants.AVG_EXECUTION_TIME, order,
							                          processCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core ProcessLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Avg Execution Time Vs Process Version Result:" + sortedResult);
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
				String processKey = filterObj.getString(AnalyticsConstants.PROCESS_KEY);
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
				query.setQuery("processKeyName:" + "\"'" + processKey + "'\"");
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Process Instance Count Vs Process Version Result:" +
					          AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
						      AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Integer> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						String processVersion =
								values.getJSONArray(AnalyticsConstants.PROCESS_VERSION)
								      .getString(0);
						int processInstanceCount =
								values.getInt(AnalyticsConstants.PROCESS_INSTANCE_COUNT);
						table.put(processVersion, processInstanceCount);
					}
					sortedResult = AnalyticsUtils
							.getIntegerValueSortedList(table, AnalyticsConstants.PROCESS_VERSION,
							                           AnalyticsConstants.PROCESS_INSTANCE_COUNT,
							                           order, processCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core ProcessLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Process Instance Count Vs Process Version Result:" + sortedResult);
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
					queryStr += " AND " + AnalyticsUtils
							.getDateRangeQuery(AnalyticsConstants.COLUMN_FINISHED_TIME, from, to);
				}
				searchQuery.setQuery(queryStr);
				searchQuery.setStart(AnalyticsConstants.MIN_COUNT);
				searchQuery.setCount(AnalyticsConstants.MAX_COUNT);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Execution Time Vs Process Instance Id Result:" +
					          AnalyticsUtils.getJSONString(searchQuery));
				}

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_SEARCH),
						      AnalyticsUtils.getJSONString(searchQuery));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<String, Double> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						String processDefKey =
								values.getString(AnalyticsConstants.PROCESS_INSTANCE_ID);
						double executionTime = values.getDouble(AnalyticsConstants.DURATION);
						table.put(processDefKey, executionTime);
					}
					sortedResult = AnalyticsUtils
							.getDoubleValueSortedList(table, AnalyticsConstants.PROCESS_INSTANCE_ID,
							                          AnalyticsConstants.DURATION, order, limit);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core ProcessLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Execution Time Vs Process Instance Id Result:" + sortedResult);
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
				JSONArray processIdList =
						filterObj.getJSONArray(AnalyticsConstants.PROCESS_ID_LIST);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticsConstants.FINISHED_TIME);
				String queryStr = AnalyticsUtils
						.getDateRangeQuery(AnalyticsConstants.COLUMN_FINISHED_TIME, from, to);

				if (processIdList.length() != 0) {
					queryStr += " AND ";
					for (int i = 0; i < processIdList.length(); i++) {
						if (i == 0) {
							queryStr +=
									"(processDefinitionId:" + "\"'" + processIdList.getString(i) +
									"'\"";
						} else {
							queryStr += " OR " + "processDefinitionId:" + "\"'" +
							            processIdList.getString(i) + "'\"";
						}
						if (i == processIdList.length() - 1) {
							queryStr += ")";
						}
					}
				}
				query.setQuery(queryStr);
				query.setAggregateFields(aggregateFields);

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
						      AnalyticsUtils.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<Long, Integer> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						long completedTime = Long.parseLong(
								values.getJSONArray(AnalyticsConstants.FINISHED_TIME).getString(0));
						int processInstanceCount =
								values.getInt(AnalyticsConstants.PROCESS_INSTANCE_COUNT);
						table.put(completedTime, processInstanceCount);
					}
					sortedResult = AnalyticsUtils
							.getLongKeySortedList(table, AnalyticsConstants.FINISHED_TIME,
							                      AnalyticsConstants.PROCESS_INSTANCE_COUNT);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core ProcessLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Date Vs Process Instance Count Result:" + sortedResult);
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
					log.debug("Query to get the Process Id List Result:" +
					          AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
						      AnalyticsUtils.getJSONString(query));

				JSONArray array = new JSONArray(result);
				JSONArray resultArray = new JSONArray();

				if (array.length() != 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObj = array.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						String processDefKey =
								values.getJSONArray(AnalyticsConstants.PROCESS_DEFINITION_KEY)
								      .getString(0);
						JSONObject o = new JSONObject();
						o.put(AnalyticsConstants.PROCESS_DEFINITION_KEY, processDefKey);
						resultArray.put(o);
					}
					processIdList = resultArray.toString();
				}

				if (log.isDebugEnabled()) {
					log.debug("Process Id List Result:" + processIdList);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core - process id list error.", e);
		}
		return processIdList;
	}

	/**
	 * Get process key list
	 *
	 * @return process key list as a JSON array string
	 */
	public String getProcessKeyList() {
		String processKeyList = "";
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
				query.setGroupByField(AnalyticsConstants.PROCESS_KEY);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Process Key List Result:" +
					          AnalyticsUtils.getJSONString(query));
				}

				String result = AnalyticsRestClient
						.post(AnalyticsUtils.getURL(AnalyticsConstants.ANALYTICS_AGGREGATE),
						      AnalyticsUtils.getJSONString(query));

				JSONArray array = new JSONArray(result);
				JSONArray resultArray = new JSONArray();

				if (array.length() != 0) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject jsonObj = array.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject(AnalyticsConstants.VALUES);
						String processKey =
								values.getJSONArray(AnalyticsConstants.PROCESS_KEY)
								      .getString(0);
						JSONObject o = new JSONObject();
						o.put(AnalyticsConstants.PROCESS_KEY, processKey);
						resultArray.put(o);
					}
					processKeyList = resultArray.toString();
				}

				if (log.isDebugEnabled()) {
					log.debug("Process Key List Result:" + processKeyList);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core - process key list error.", e);
		}
		return processKeyList;
	}
}
