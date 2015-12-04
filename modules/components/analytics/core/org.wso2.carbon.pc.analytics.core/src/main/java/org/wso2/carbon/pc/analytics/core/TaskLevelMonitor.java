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
 * TaskLevelMonitor holds all the functionalites for the task level monitoring
 */
public class TaskLevelMonitor {
	private static final Log log = LogFactory.getLog(TaskLevelMonitor.class);

	/**
	 * perform query: SELECT taskDefinitionKey, AVG(duration) AS avgExecutionTime FROM
	 *                USER_INVOLVE_SUMMARY_DATA GROUP BY taskDefinitionKey;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getAvgExecuteTimeVsTaskId(String filters) {
		String sortedResult = "";
		try {
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int taskCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticConstants.DURATION);
				avgField.setAggregate(AnalyticConstants.AVG);
				avgField.setAlias(AnalyticConstants.AVG_EXECUTION_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticConstants.TASK_DEFINITION_KEY);
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
						String taskDefKey = values.getJSONArray("taskDefId").getString(0);
						double avgExecTime = values.getDouble("avgExecutionTime");
						table.put(taskDefKey, avgExecTime);
					}
					sortedResult = Helper.getDoubleValueSortedList(table, "taskDefId",
					                                               "avgExecutionTime",
					                                               order, taskCount);
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core TaskLevelMonitoring error.";
			log.error(errMsg, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result = " + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT taskDefinitionKey, COUNT(taskInstanceId) AS taskInstanceCount FROM
	 *                USER_INVOLVE_SUMMARY_DATA GROUP BY taskDefinitionKey;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTaskInstanceCountVsTaskId(String filters) {
		String sortedResult = "";
		try {
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticConstants.PROCESS_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int taskCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticConstants.ALL);
				countField.setAggregate(AnalyticConstants.COUNT);
				countField.setAlias(AnalyticConstants.TASK_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticConstants.TASK_DEFINITION_KEY);
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
						String processDefKey = values.getJSONArray("taskDefId").getString(0);
						int processInstanceCount = values.getInt("taskInstanceCount");
						table.put(processDefKey, processInstanceCount);
					}
					sortedResult = Helper.getIntegerValueSortedList(table, "taskDefId",
					                                                "taskInstanceCount", order,
					                                                taskCount);
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core TaskLevelMonitoring error.";
			log.error(errMsg, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result = " + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT assignee, COUNT(taskInstanceId) AS taskInstanceCount FROM
	 *                TASK_USAGE_SUMMARY GROUP BY assignee;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTaskInstanceCountVsUserId(String filters) {
		String sortedResult = "";
		try {
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String taskId = filterObj.getString(AnalyticConstants.TASK_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int taskCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticConstants.ALL);
				countField.setAggregate(AnalyticConstants.COUNT);
				countField.setAlias(AnalyticConstants.TASK_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.ASSIGN_USER);
				query.setQuery("taskDefinitionKey:" + "\"'" + taskId + "'\"");
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
						String userId = values.getJSONArray("assignUser").getString(0);
						int taskInstanceCount = values.getInt("taskInstanceCount");
						table.put(userId, taskInstanceCount);
					}
					sortedResult = Helper.getIntegerValueSortedList(table, "assignUser",
					                                                "taskInstanceCount", order,
					                                                taskCount);
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core TaskLevelMonitoring error.";
			log.error(errMsg, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result = " + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT assignee, AVG(duration) AS avgExecutionTime FROM
	 *                TASK_USAGE_SUMMARY GROUP BY assignee;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getAvgWaitingTimeVsUserId(String filters) {
		String sortedResult = "";
		try {
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String taskId = filterObj.getString(AnalyticConstants.TASK_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int taskCount = filterObj.getInt(AnalyticConstants.NUM_COUNT);

				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticConstants.DURATION);
				avgField.setAggregate(AnalyticConstants.AVG);
				avgField.setAlias(AnalyticConstants.AVG_WAITING_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.ASSIGN_USER);
				query.setQuery("taskDefinitionKey:" + "\"'" + taskId + "'\"");
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
						String userId = values.getJSONArray("assignUser").getString(0);
						double avgExecTime = values.getInt("avgWaitingTime");
						table.put(userId, avgExecTime);
					}
					sortedResult = Helper.getDoubleValueSortedList(table, "assignUser",
					                                               "avgWaitingTime",
					                                               order, taskCount);
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core TaskLevelMonitoring error.";
			log.error(errMsg, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result = " + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT DISTINCT taskInstanceId, duration FROM TASK_USAGE_SUMMARY;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getExecutionTimeVsTaskInstanceId(String filters) {
		String sortedResult = "";
		try {
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticConstants.START_TIME);
				long to = filterObj.getLong(AnalyticConstants.END_TIME);
				String taskId = filterObj.getString(AnalyticConstants.TASK_ID);
				String order = filterObj.getString(AnalyticConstants.ORDER);
				int limit = filterObj.getInt(AnalyticConstants.LIMIT);

				SearchQuery searchQuery = new SearchQuery();
				searchQuery.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
				String queryStr = "taskDefinitionKey:" + "\"'" + taskId + "'\"";
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
						String processDefKey = values.getString("taskInstanceId");
						double executionTime = values.getDouble("duration");
						table.put(processDefKey, executionTime);
					}
					sortedResult = Helper.getDoubleValueSortedList(table, "taskInstanceId",
					                                               "duration",
					                                               order, limit);
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core TaskLevelMonitoring error.";
			log.error(errMsg, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result = " + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * Perform query: SELECT DISTINCT finishTime, COUNT(*) AS taskInstanceCount FROM TASK_USAGE_SUMMARY
	 *                WHERE <date range> AND <task id list> GROUP BY finishTime
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getDateVsTaskInstanceCount(String filters) {
		String sortedResult = "";
		try {
			if (Helper.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticConstants.START_TIME);
				long to = filterObj.getLong(AnalyticConstants.END_TIME);
				JSONArray taskIdList = filterObj.getJSONArray(AnalyticConstants.TASK_ID_LIST);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticConstants.ALL);
				countField.setAggregate(AnalyticConstants.COUNT);
				countField.setAlias(AnalyticConstants.TASK_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.TASK_USAGE_TABLE);
				query.setGroupByField(AnalyticConstants.FINISHED_TIME);
				String queryStr = Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME,
				                                           from, to);

				if (taskIdList.length() != 0) {
					queryStr += " AND ";
					for (int i = 0; i < taskIdList.length(); i++) {
						if (i == 0) {
							queryStr += "(taskDefinitionKey:" + "\"'" + taskIdList.getString(i)
							            + "'\"";
						} else {
							queryStr += " OR " + "taskDefinitionKey:" + "\"'" +
							            taskIdList.getString(i) + "'\"";
						}
						if (i == taskIdList.length() - 1) {
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
						int taskInstanceCount = values.getInt("taskInstanceCount");
						table.put(completedTime, taskInstanceCount);
					}
					sortedResult = Helper.getLongKeySortedList(table, "finishTime", "taskInstanceCount");
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core TaskLevelMonitoring error.";
			log.error(errMsg, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Result = " + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * Get task definition key list
	 *
	 * @return task definition key list as a JSON array string
	 */
	public String getTaskList() {
		String taskIdList = "";
		try {
			if (Helper.isDASAnalyticsActivated()) {
				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticConstants.ALL);
				countField.setAggregate(AnalyticConstants.COUNT);
				countField.setAlias(AnalyticConstants.TASK_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticConstants.TASK_DEFINITION_KEY);
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
						String taskDefKey = values.getJSONArray("taskDefId").getString(0);
						JSONObject o = new JSONObject();
						o.put("taskDefId", taskDefKey);
						resultArray.put(o);
					}
					taskIdList = resultArray.toString();
				}

				if (log.isDebugEnabled()) {
					log.debug("Query = " + query.getQuery());
					log.debug("Result = " + taskIdList);
				}
			}
		} catch (Exception e) {
			String errMsg = "PC Analytics core - task id list error.";
			log.error(errMsg, e);
		}
		return taskIdList;
	}
}
