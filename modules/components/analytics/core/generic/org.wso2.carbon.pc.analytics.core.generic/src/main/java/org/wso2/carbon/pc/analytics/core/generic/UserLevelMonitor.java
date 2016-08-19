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
import org.wso2.carbon.pc.analytics.core.generic.utils.AnalyticsUtils;
import org.wso2.carbon.pc.analytics.core.generic.models.AggregateField;
import org.wso2.carbon.pc.analytics.core.generic.models.AggregateQuery;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * UserLevelMonitor holds all the functionalities for the user level monitoring
 * eg:  1.) Involvement of different users in a given process
 *      2.) Total involved time of different users with a given process
 */
public class UserLevelMonitor {
	private static final Log log = LogFactory.getLog(UserLevelMonitor.class);

	/**
	 * perform query: SELECT assignUser, SUM(duration) AS totalInvolvedTime FROM
	 *                USER_INVOLVE_SUMMARY WHERE <date range> GROUP BY assignUser;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTotalInvolvedTimeVsUserId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int userCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField sumField = new AggregateField();
				sumField.setFieldName(AnalyticsConstants.DURATION);
				sumField.setAggregate(AnalyticsConstants.SUM);
				sumField.setAlias(AnalyticsConstants.TOTAL_INVOLVED_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(sumField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticsConstants.ASSIGN_USER);
				if (from != 0 && to != 0) {
					query.setQuery(AnalyticsUtils.getDateRangeQuery(
							AnalyticsConstants.COLUMN_FINISHED_TIME, from, to));
				}
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Total Involved Time Vs User Id Result:" +
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
						String assignee =
								values.getJSONArray(AnalyticsConstants.ASSIGN_USER).getString(0);
						double totalInvolvedTime =
								values.getDouble(AnalyticsConstants.TOTAL_INVOLVED_TIME);
						table.put(assignee, totalInvolvedTime);
					}
					sortedResult = AnalyticsUtils
							.getDoubleValueSortedList(table, AnalyticsConstants.ASSIGN_USER,
							                          AnalyticsConstants.TOTAL_INVOLVED_TIME, order,
							                          userCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core UserLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Total Involved Time Vs User Id Result:" + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT assignUser, COUNT(*) AS completedTotalTasks FROM
	 *                USER_INVOLVE_SUMMARY_DATA WHERE <date range> GROUP BY assignUser;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTotalCompletedTasksVsUserId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int userCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.COMPLETED_TOTAL_TASKS);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticsConstants.ASSIGN_USER);
				if (from != 0 && to != 0) {
					query.setQuery(AnalyticsUtils.getDateRangeQuery(
							AnalyticsConstants.COLUMN_FINISHED_TIME, from, to));
				}
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Total Completed Tasks Vs User Id Result:" +
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
						String assignee =
								values.getJSONArray(AnalyticsConstants.ASSIGN_USER).getString(0);
						int totalInvolvedTime =
								values.getInt(AnalyticsConstants.COMPLETED_TOTAL_TASKS);
						table.put(assignee, totalInvolvedTime);
					}
					sortedResult = AnalyticsUtils
							.getIntegerValueSortedList(table, AnalyticsConstants.ASSIGN_USER,
							                           AnalyticsConstants.COMPLETED_TOTAL_TASKS,
							                           order, userCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core UserLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Total Completed Tasks Vs User Id Result:" + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT processDefKey, SUM(duration) AS totalInvolvedTime FROM
	 *                USER_INVOLVE_SUMMARY WHERE <assignee> AND <date range> GROUP BY processDefKey;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTotalInvolvedTimeVsProcessId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				String userId = filterObj.getString(AnalyticsConstants.USER_ID);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int count = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField sumField = new AggregateField();
				sumField.setFieldName(AnalyticsConstants.DURATION);
				sumField.setAggregate(AnalyticsConstants.SUM);
				sumField.setAlias(AnalyticsConstants.TOTAL_INVOLVED_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(sumField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticsConstants.PROCESS_DEFINITION_KEY);
				String queryStr = "assignee:" + "\"'" + userId + "'\"";
				if (from != 0 && to != 0) {
					queryStr += " AND " + AnalyticsUtils
							.getDateRangeQuery(AnalyticsConstants.COLUMN_FINISHED_TIME, from, to);
				}
				query.setQuery(queryStr);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the Total Involved Time Vs Process Id Result:" +
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
						double totalInvolvedTime =
								values.getDouble(AnalyticsConstants.TOTAL_INVOLVED_TIME);
						table.put(processDefKey, totalInvolvedTime);
					}
					sortedResult = AnalyticsUtils.getDoubleValueSortedList(table,
					                                                       AnalyticsConstants.PROCESS_DEFINITION_KEY,
					                                                       AnalyticsConstants.TOTAL_INVOLVED_TIME,
					                                                       order, count);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core UserLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Total Involved Time Vs Process Id Result:" + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT processDefKey, COUNT(*) AS totalInstanceCount FROM
	 *                USER_INVOLVE_SUMMARY WHERE <assignee> AND <date range> GROUP BY
	 *                processDefKey;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTotalInvolvedInstanceCountVsProcessId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				long from = filterObj.getLong(AnalyticsConstants.START_TIME);
				long to = filterObj.getLong(AnalyticsConstants.END_TIME);
				String userId = filterObj.getString(AnalyticsConstants.USER_ID);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int count = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.TOTAL_INSTANCE_COUNT);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticsConstants.PROCESS_DEFINITION_KEY);
				String queryStr = "assignee:" + "\"'" + userId + "'\"";
				if (from != 0 && to != 0) {
					queryStr += " AND " + AnalyticsUtils
							.getDateRangeQuery(AnalyticsConstants.COLUMN_FINISHED_TIME, from, to);
				}
				query.setQuery(queryStr);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug(
							"Query to get the Total Involved Instance Count Vs Process Id Result:" +
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
						int instanceCount = values.getInt(AnalyticsConstants.TOTAL_INSTANCE_COUNT);
						table.put(processDefKey, instanceCount);
					}
					sortedResult = AnalyticsUtils.getIntegerValueSortedList(table,
					                                                        AnalyticsConstants.PROCESS_DEFINITION_KEY,
					                                                        AnalyticsConstants.TOTAL_INSTANCE_COUNT,
					                                                        order, count);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core UserLevelMonitoring error.", e);
		}

		if (log.isDebugEnabled()) {
			log.debug("Total Involved Instance Count Vs Process Id Result:" + sortedResult);
		}

		return sortedResult;
	}

	/**
	 * perform query: SELECT taskDefinitionKey, COUNT(taskInstanceId) AS taskInstanceCount FROM
	 *                USER_INVOLVE_SUMMARY_DATA WHERE <assignee> GROUP BY taskDefinitionKey;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getUserLevelTaskInstanceCountVsTaskId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticsConstants.PROCESS_ID);
				String userId = filterObj.getString(AnalyticsConstants.USER_ID);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int taskCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.TASK_INSTANCE_COUNT);
				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticsConstants.TASK_DEFINITION_KEY);
				String queryStr="assignee:" + "\"'" + userId + "'\"";
				queryStr += " AND " + "processDefKey:" + "\"'" + processId + "'\"";
				query.setQuery(queryStr);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the User Level Task Instance Count Vs Task Id Result:" +
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
								values.getJSONArray(AnalyticsConstants.TASK_DEFINITION_KEY)
								      .getString(0);
						int processInstanceCount =
								values.getInt(AnalyticsConstants.TASK_INSTANCE_COUNT);
						table.put(processDefKey, processInstanceCount);
					}
					sortedResult = AnalyticsUtils.getIntegerValueSortedList(table,
					                                                        AnalyticsConstants.TASK_DEFINITION_KEY,
					                                                        AnalyticsConstants.TASK_INSTANCE_COUNT,
					                                                        order, taskCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core UserLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("User Level Task Instance Count Vs Task Id Result:" + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * perform query: SELECT taskDefinitionKey, AVG(duration) AS avgExecutionTime FROM
	 *                USER_INVOLVE_SUMMARY_DATA WHERE <assignee> GROUP BY taskDefinitionKey;
	 *
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getUserLevelAvgExecuteTimeVsTaskId(String filters) {
		String sortedResult = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				JSONObject filterObj = new JSONObject(filters);
				String processId = filterObj.getString(AnalyticsConstants.PROCESS_ID);
				String userId = filterObj.getString(AnalyticsConstants.USER_ID);
				String order = filterObj.getString(AnalyticsConstants.ORDER);
				int taskCount = filterObj.getInt(AnalyticsConstants.NUM_COUNT);

				AggregateField avgField = new AggregateField();
				avgField.setFieldName(AnalyticsConstants.DURATION);
				avgField.setAggregate(AnalyticsConstants.AVG);
				avgField.setAlias(AnalyticsConstants.AVG_EXECUTION_TIME);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(avgField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticsConstants.TASK_DEFINITION_KEY);
				String queryStr="assignee:" + "\"'" + userId + "'\"";
				queryStr += " AND " + "processDefKey:" + "\"'" + processId + "'\"";
				query.setQuery(queryStr);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the User Level Avg Execution Time Vs Task Id Result:" +
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
						String taskDefKey =
								values.getJSONArray(AnalyticsConstants.TASK_DEFINITION_KEY)
								      .getString(0);
						double avgExecTime =
								values.getDouble(AnalyticsConstants.AVG_EXECUTION_TIME);
						table.put(taskDefKey, avgExecTime);
					}
					sortedResult = AnalyticsUtils
							.getDoubleValueSortedList(table, AnalyticsConstants.TASK_DEFINITION_KEY,
							                          AnalyticsConstants.AVG_EXECUTION_TIME, order,
							                          taskCount);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core UserLevelMonitoring error.", e);
		}
		if (log.isDebugEnabled()) {
			log.debug("User Level Avg Execution Time Vs Task Id Result:" + sortedResult);
		}
		return sortedResult;
	}

	/**
	 * Get user id list
	 *
	 * @return user id list as a JSON array string
	 */
	public String getUserList() {
		String userIdList = "";
		try {
			if (AnalyticsUtils.isDASAnalyticsActivated()) {
				AggregateField countField = new AggregateField();
				countField.setFieldName(AnalyticsConstants.ALL);
				countField.setAggregate(AnalyticsConstants.COUNT);
				countField.setAlias(AnalyticsConstants.COMPLETED_TOTAL_TASKS);

				ArrayList<AggregateField> aggregateFields = new ArrayList<>();
				aggregateFields.add(countField);

				AggregateQuery query = new AggregateQuery();
				query.setTableName(AnalyticsConstants.USER_INVOLVE_TABLE);
				query.setGroupByField(AnalyticsConstants.ASSIGN_USER);
				query.setAggregateFields(aggregateFields);

				if (log.isDebugEnabled()) {
					log.debug("Query to get the User List Result:" +
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
						String userId =
								values.getJSONArray(AnalyticsConstants.ASSIGN_USER).getString(0);
						JSONObject o = new JSONObject();
						o.put(AnalyticsConstants.ASSIGN_USER, userId);
						resultArray.put(o);
					}
					userIdList = resultArray.toString();
				}

				if (log.isDebugEnabled()) {
					log.debug("User List Result:" + userIdList);
				}
			}
		} catch (Exception e) {
			log.error("PC Analytics core - user id list error.", e);
		}
		return userIdList;
	}
}
