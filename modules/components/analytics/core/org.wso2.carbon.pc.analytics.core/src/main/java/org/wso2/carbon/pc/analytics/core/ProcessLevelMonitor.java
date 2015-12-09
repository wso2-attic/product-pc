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
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticConstants.START_TIME);
				long to = filterObj.getLong(AnalyticConstants.END_TIME);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticConstants.DURATION);
				avgField.setAggregate(AnalyticConstants.AVG);
				avgField.setAlias(AnalyticConstants.AVG_EXECUTION_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
				if (from != 0 && to != 0) {
					query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME,
					                                        from, to));
				}
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(Helper.getJSONString(query));
				}

				String result = AnalyticsRestClient
								.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
						        Helper.getJSONString(query));

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
					sortedResult = Helper.getDoubleValueSortedList(table, "processDefKey",
					                                               "avgExecutionTime", order,
					                                               processCount);
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
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticConstants.START_TIME);
				long to = filterObj.getLong(AnalyticConstants.END_TIME);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticConstants.ALL);
				countField.setAggregate(AnalyticConstants.COUNT);
				countField.setAlias(AnalyticConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
				if (from != 0 && to != 0) {
					query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME,
					                                        from, to));
				}
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(Helper.getJSONString(query));
				}

				String result = AnalyticsRestClient
								.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
						        Helper.getJSONString(query));

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
					sortedResult = Helper.getIntegerValueSortedList(table, "processDefKey",
					                                                "processInstanceCount", order,
					                                                processCount);
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
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticConstants.DURATION);
				avgField.setAggregate(AnalyticConstants.AVG);
				avgField.setAlias(AnalyticConstants.AVG_EXECUTION_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.PROCESS_VERSION);
				query.setQuery("processDefinitionId:" + "\"'" + processId + "'\"");
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(Helper.getJSONString(query));
				}

				String result = AnalyticsRestClient
								.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
						        Helper.getJSONString(query));

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
					sortedResult = Helper.getDoubleValueSortedList(table, "processVer",
					                                               "avgExecutionTime",
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
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int processCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

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

				if (log.isDebugEnabled()) {
					log.debug(Helper.getJSONString(query));
				}

				String result = AnalyticsRestClient
								.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
						        Helper.getJSONString(query));

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
					sortedResult = Helper.getIntegerValueSortedList(table, "processVer",
					                                                "processInstanceCount", order,
					                                                processCount);
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
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticConstants.START_TIME);
				long to = filterObj.getLong(AnalyticConstants.END_TIME);
				String processId = filterObj.getString(AnalyticConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int limit = filterObj.getInt(AnalyticConstants.LIMIT);

				SearchQuery searchQuery = new SearchQuery();
				searchQuery.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
				String queryStr = "processDefinitionId:" + "\"'" + processId + "'\"";
				if (from != 0 && to != 0) {
					queryStr += " AND " +
					            Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME,
					                                     from, to);
				}
				searchQuery.setQuery(queryStr);
				searchQuery.setStart(AnalyticConstants.MIN_COUNT);
				searchQuery.setCount(AnalyticConstants.MAX_COUNT);

				if (log.isDebugEnabled()) {
					log.debug(Helper.getJSONString(searchQuery));
				}

				String result = AnalyticsRestClient
								.post(Helper.getURL(AnalyticConstants.ANALYTICS_SEARCH),
						        Helper.getJSONString(searchQuery));

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
					sortedResult = Helper.getDoubleValueSortedList(table, "processInstanceId",
					                                               "duration",
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
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticConstants.START_TIME);
				long to = filterObj.getLong(AnalyticConstants.END_TIME);
				JSONArray processIdList = filterObj.getJSONArray(AnalyticConstants.PROCESS_ID_LIST);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticConstants.ALL);
				countField.setAggregate(AnalyticConstants.COUNT);
				countField.setAlias(AnalyticConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.FINISHED_TIME);
				String queryStr = Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME,
				                                           from, to);

				if (processIdList.length() != 0) {
					queryStr += " AND ";
					for (int i = 0; i < processIdList.length(); i++) {
						if (i == 0) {
							queryStr += "(processDefinitionId:" + "\"'" + processIdList.getString(i)
							            + "'\"";
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
								.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
						        Helper.getJSONString(query));

				JSONArray unsortedResultArray = new JSONArray(result);
				Hashtable<Long, Integer> table = new Hashtable<>();

				if (unsortedResultArray.length() != 0) {
					for (int i = 0; i < unsortedResultArray.length(); i++) {
						JSONObject jsonObj = unsortedResultArray.getJSONObject(i);
						JSONObject values = jsonObj.getJSONObject("values");
						long completedTime = Long.parseLong(values.getJSONArray("finishTime")
						                                          .getString(0));
						int processInstanceCount = values.getInt("processInstanceCount");
						table.put(completedTime, processInstanceCount);
					}
					sortedResult = Helper.getLongKeySortedList(table, "finishTime",
					                                           "processInstanceCount");
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
			if (Helper.isDASAnalyticsActivated()) {
				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticConstants.ALL);
				avgField.setAggregate(AnalyticConstants.COUNT);
				avgField.setAlias(AnalyticConstants.PROCESS_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.PROCESS_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(Helper.getJSONString(query));
				}

				String result = AnalyticsRestClient
								.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
						        Helper.getJSONString(query));

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
