/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.pc.core.audit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.core.ProcessCenterConstants;
import org.wso2.carbon.pc.core.audit.bean.LogBean;
import org.wso2.carbon.registry.core.LogEntry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ListIterator;

/**
 * class for getting registry logs and filter them to create activity logs
 */

public class LogEntryProcessUtils {

    private static final Log log = LogFactory.getLog(LogEntryProcessUtils.class);

    /**
     * @param registry  user registry from registry service
     * @param logBean   bean for log entry
     * @param logResult json result for log entry
     * @param path      the path to process asset
     * @throws RegistryException
     * @throws JSONException
     */
    public void processLogResult(UserRegistry registry, LogBean logBean, JSONArray logResult, String path)
            throws RegistryException, JSONException {

        logBean.setProcessEntries(registry.getLogs(
                ProcessCenterConstants.AUDIT.PROCESS_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setProcessTxtEntries(registry.getLogs(
                ProcessCenterConstants.AUDIT.PROCESS_TEXT_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setPdfEntries(registry.getLogs(
                ProcessCenterConstants.AUDIT.PROCESS_PDF_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setFlowchartEntries(registry.getLogs(
                ProcessCenterConstants.AUDIT.PROCESS_FLOW_CHART_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setDocEntries(registry.getLogs(
                ProcessCenterConstants.AUDIT.PROCESS_DOC_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setBpmnEntries(registry.getLogs(
                ProcessCenterConstants.AUDIT.PROCESS_BPMN + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setLifecycleEntries(registry.getLogs(
                ProcessCenterConstants.AUDIT.PROCESS_LIFECYCLE_HISTORY + path.replace("/", "_"), LogEntry.ALL, null, null, new Date(), true));

        ArrayList<LogEntry> list = new ArrayList<>(Arrays.asList(logBean.getProcessEntries()));
        filterProcessCreation(list, path);
        LogEntry[] processEntriesArr = list.toArray(new LogEntry[list.size()]);

        processLogEntryResponse(processEntriesArr, ProcessCenterConstants.AUDIT.PROCESS, path, logResult);
        processLogEntryResponse(logBean.getProcessTxtEntries(), ProcessCenterConstants.AUDIT.PROCESS_TEXT, path, logResult);
        processLogEntryResponse(logBean.getPdfEntries(), ProcessCenterConstants.AUDIT.PDF, path, logResult);
        processLogEntryResponse(logBean.getFlowchartEntries(), ProcessCenterConstants.AUDIT.FLOW_CHART, path, logResult);
        processLogEntryResponse(logBean.getDocEntries(), ProcessCenterConstants.AUDIT.DOCUMENT, path, logResult);
        processLogEntryResponse(logBean.getBpmnEntries(), ProcessCenterConstants.AUDIT.BPMN, path, logResult);
        processLogEntryResponse(logBean.getLifecycleEntries(), ProcessCenterConstants.AUDIT.LIFE_CYCLE, path, logResult);

    }

    /**
     * @param entries     log entries taken from the registry logs
     * @param type        log entry type
     * @param processPath the path to the process asset
     * @param result      resultant json of the log entries
     * @throws JSONException
     */
    private void processLogEntryResponse(LogEntry[] entries, String type, String processPath, JSONArray result) throws JSONException {

        for (LogEntry logEntry : entries) {
            try {
                JSONObject entryObj = new JSONObject();
                if (logEntry.getAction() == LogEntry.UPDATE) {

                    entryObj.put(ProcessCenterConstants.AUDIT.ASSET_TYPE, processPath);
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION, "update");
                    entryObj.put(ProcessCenterConstants.AUDIT.USER, logEntry.getUserName());
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION_TYPE, type);
                    entryObj.put(ProcessCenterConstants.AUDIT.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);

                } else if (logEntry.getAction() == LogEntry.ADD) {
                    entryObj.put(ProcessCenterConstants.AUDIT.ASSET_TYPE, processPath);
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION, "add");
                    entryObj.put(ProcessCenterConstants.AUDIT.USER, logEntry.getUserName());
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION_TYPE, type);
                    entryObj.put(ProcessCenterConstants.AUDIT.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);

                } else if (logEntry.getAction() == LogEntry.DELETE_RESOURCE) {
                    entryObj.put(ProcessCenterConstants.AUDIT.ASSET_TYPE, processPath);
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION, "delete");
                    entryObj.put(ProcessCenterConstants.AUDIT.USER, logEntry.getUserName());
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION_TYPE, type);
                    entryObj.put(ProcessCenterConstants.AUDIT.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);

                } else if (logEntry.getAction() == LogEntry.TAG) {
                    entryObj.put(ProcessCenterConstants.AUDIT.ASSET_TYPE, processPath);
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION, "tag");
                    entryObj.put(ProcessCenterConstants.AUDIT.USER, logEntry.getUserName());
                    entryObj.put(ProcessCenterConstants.AUDIT.ACTION_TYPE, type);
                    entryObj.put(ProcessCenterConstants.AUDIT.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);
                }
            } catch (JSONException e) {
                String msg = "Error processing log entries for process in " + processPath;
                log.error(msg, e);
                throw new JSONException(msg);
            }
        }
    }

    /**
     * @param entries log entries taken from the registry logs
     * @param path    the path to process asset
     */
    private void filterProcessCreation(ArrayList<LogEntry> entries, String path) {

        int count = 0;
        for (ListIterator<LogEntry> iterator = entries.listIterator(entries.size()); iterator.hasPrevious(); ) {
            iterator.previous();
            if (count > 0 && count < 5) {
                iterator.remove();
            }

            if (count == 5) {
                return;
            }
            count++;
        }
    }


}
