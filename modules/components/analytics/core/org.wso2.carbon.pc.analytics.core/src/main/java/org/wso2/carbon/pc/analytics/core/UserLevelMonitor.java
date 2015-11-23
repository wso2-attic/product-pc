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
import org.json.JSONObject;
import org.wso2.carbon.pc.analytics.core.clients.AnalyticsRestClient;
import org.wso2.carbon.pc.analytics.core.models.AggregateField;
import org.wso2.carbon.pc.analytics.core.models.AggregateQuery;
import org.wso2.carbon.pc.analytics.core.utils.Helper;

import java.util.ArrayList;

/**
 * UserLevelMonitor keeps all the functionalites for the user level monitoring
 */
public class UserLevelMonitor {
	private static final Log log = LogFactory.getLog(UserLevelMonitor.class);

	/**
	 * perform query: SELECT processDefKey, SUM(duration) AS totalInvolvedTime FROM
	 *                USER_INVOLVE_SUMMARY WHERE <date range> GROUP BY processDefKey;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTotalInvolvedTimeVsProcessId(String filters) {
		String result = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			long from = filterObj.getLong("startTime");
			long to = filterObj.getLong("endTime");
			String userId = filterObj.getString("userId");

			AggregateField sumField = new AggregateField();
			sumField.setFieldName(AnalyticConstants.DURATION);
			sumField.setAggregate(AnalyticConstants.SUM);
			sumField.setAlias(AnalyticConstants.TOTAL_INVOLVED_TIME);

			ArrayList<AggregateField> aggregateFields = new ArrayList<>();
			aggregateFields.add(sumField);

			AggregateQuery query = new AggregateQuery();
			query.setTableName(AnalyticConstants.USER_INVOLVE_TABLE);
			query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
			query.setQuery(Helper.getDateRangeQuery( "assignee:" + "\"'" + userId + "'\"" + " AND " +
			                                         AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
			query.setAggregateFields(aggregateFields);

			if(log.isDebugEnabled()){
				log.debug(Helper.getJSONString(query));
			}

			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (Exception e) {
			String errMsg = "PC Analytics core userLevelMonitoring error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}

	/**
	 * perform query: SELECT assignUser, COUNT(*) AS completedTotalTasks FROM
	 *                USER_INVOLVE_SUMMARY_DATA WHERE <date range> GROUP BY assignUser;
	 * @param filters is used to filter the result
	 * @return the result as a JSON string
	 */
	public String getTotalCompletedTasksVsUserId(String filters) {
		String result = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			long from = filterObj.getLong("startTime");
			long to = filterObj.getLong("endTime");

			AggregateField countField = new AggregateField();
			countField.setFieldName(AnalyticConstants.ALL);
			countField.setAggregate(AnalyticConstants.COUNT);
			countField.setAlias(AnalyticConstants.COMPLETED_TOTAL_TASKS);

			ArrayList<AggregateField> aggregateFields = new ArrayList<>();
			aggregateFields.add(countField);

			AggregateQuery query = new AggregateQuery();
			query.setTableName(AnalyticConstants.USER_INVOLVE_TABLE);
			query.setGroupByField(AnalyticConstants.ASSIGN_USER);
			query.setQuery(Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
			query.setAggregateFields(aggregateFields);

			if(log.isDebugEnabled()){
				log.debug(Helper.getJSONString(query));
			}

			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (Exception e) {
			String errMsg = "PC Analytics core userLevelMonitoring error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}

	/**
	 * perform query: SELECT processDefKey, COUNT(*) AS totalInstanceCount FROM
	 *                USER_INVOLVE_SUMMARY GROUP BY processDefKey;
	 * @param filters is used to filter the result
	 * @return  the result as a JSON string
	 */
	public String getInvlovedInstanceCountVsProcessId(String filters) {
		String result = "";
		try {
			JSONObject filterObj = new JSONObject(filters);
			long from = filterObj.getLong("startTime");
			long to = filterObj.getLong("endTime");
			String userId = filterObj.getString("userId");

			AggregateField countField = new AggregateField();
			countField.setFieldName(AnalyticConstants.ALL);
			countField.setAggregate(AnalyticConstants.COUNT);
			countField.setAlias(AnalyticConstants.TOTAL_INSTANCE_COUNT);

			ArrayList<AggregateField> aggregateFields = new ArrayList<>();
			aggregateFields.add(countField);

			AggregateQuery query = new AggregateQuery();
			query.setTableName(AnalyticConstants.USER_INVOLVE_TABLE);
			query.setGroupByField(AnalyticConstants.PROCESS_DEFINITION_KEY);
			query.setQuery("assignee:" + "\"'" + userId + "'\"" + " AND " +
			               Helper.getDateRangeQuery(AnalyticConstants.COLUMN_FINISHED_TIME, from, to));
			query.setAggregateFields(aggregateFields);

			if(log.isDebugEnabled()){
				log.debug(Helper.getJSONString(query));
			}

			result = AnalyticsRestClient.post(Helper.getURL(AnalyticConstants.ANALYTICS_AGGREGATE),
			                                  Helper.getJSONString(query));
		} catch (Exception e) {
			String errMsg = "PC Analytics core userLevelMonitoring error.";
			log.error(errMsg, e);
		}

		if(log.isDebugEnabled()){
			log.debug("Result = " + result);
		}

		return result;
	}
}
