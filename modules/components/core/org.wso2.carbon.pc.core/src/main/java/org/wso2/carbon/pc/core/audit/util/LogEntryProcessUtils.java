package org.wso2.carbon.pc.core.audit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wso2.carbon.pc.core.audit.bean.LogBean;
import org.wso2.carbon.registry.core.LogEntry;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.session.UserRegistry;

import java.util.*;

public class LogEntryProcessUtils {

    private static final Log log = LogFactory.getLog(LogEntryProcessUtils.class);

    public static void filterProcessCreation(ArrayList<LogEntry> entries, String path) {

        int count = 0;
        try {
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
        } catch (Exception e) {
            log.error("error occured", e);
        }
    }

    public static void processLogResult(UserRegistry registry, LogBean logBean, JSONArray logResult, String path )
            throws RegistryException {

        logBean.setProcessEntries(registry.getLogs(
                Constants.REG.PROCESS_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setProcessTxtEntries(registry.getLogs(
                Constants.REG.PROCESS_TEXT_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setPdfEntries(registry.getLogs(
                Constants.REG.PROCESS_PDF_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setFlowchartEntries(registry.getLogs(
                Constants.REG.PROCESS_FLOW_CHART_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setDocEntries(registry.getLogs(
                Constants.REG.PROCESS_DOC_PATH + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setBpmnEntries(registry.getLogs(
                Constants.REG.PROCESS_BPMN + path, LogEntry.ALL, null, null, new Date(), true));
        logBean.setLifecycleEntries(registry.getLogs(
                Constants.REG.PROCESS_LIFECYCLE_HISTORY+path.replace("/","_"),LogEntry.ALL, null, null, new Date(), true));

        ArrayList<LogEntry> list = new ArrayList<>(Arrays.asList(logBean.getProcessEntries()));
        filterProcessCreation(list, path);
        LogEntry[] processEntriesArr = list.toArray(new LogEntry[list.size()]);

        processLogEntryResponse(processEntriesArr, Constants.PROCESS, path, logResult);
        processLogEntryResponse(logBean.getProcessTxtEntries(), Constants.PROCESS_TEXT, path, logResult);
        processLogEntryResponse(logBean.getPdfEntries(), Constants.PDF, path, logResult);
        processLogEntryResponse(logBean.getFlowchartEntries(), Constants.FLOW_CHART, path, logResult);
        processLogEntryResponse(logBean.getDocEntries(), Constants.DOCUMENT, path, logResult);
        processLogEntryResponse(logBean.getBpmnEntries(), Constants.BPMN, path, logResult);
        processLogEntryResponse(logBean.getLifecycleEntries(), Constants.LIFE_CYCLE, path, logResult);

    }

    private static void processLogEntryResponse(LogEntry[] entries, String type, String processPath, JSONArray result) {

        for (LogEntry logEntry : entries) {
            try {
                JSONObject entryObj = new JSONObject();
                if (logEntry.getAction() == LogEntry.UPDATE) {

                    entryObj.put(Constants.LOGENTRY.ASSET_TYPE, processPath);
                    entryObj.put(Constants.LOGENTRY.ACTION, "update");
                    entryObj.put(Constants.LOGENTRY.USER, logEntry.getUserName());
                    entryObj.put(Constants.LOGENTRY.ACTION_TYPE, type);
                    entryObj.put(Constants.LOGENTRY.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);

                } else if (logEntry.getAction() == LogEntry.ADD) {
                    entryObj.put(Constants.LOGENTRY.ASSET_TYPE, processPath);
                    entryObj.put(Constants.LOGENTRY.ACTION, "add");
                    entryObj.put(Constants.LOGENTRY.USER, logEntry.getUserName());
                    entryObj.put(Constants.LOGENTRY.ACTION_TYPE, type);
                    entryObj.put(Constants.LOGENTRY.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);

                } else if (logEntry.getAction() == LogEntry.DELETE_RESOURCE) {
                    entryObj.put(Constants.LOGENTRY.ASSET_TYPE, processPath);
                    entryObj.put(Constants.LOGENTRY.ACTION, "delete");
                    entryObj.put(Constants.LOGENTRY.USER, logEntry.getUserName());
                    entryObj.put(Constants.LOGENTRY.ACTION_TYPE, type);
                    entryObj.put(Constants.LOGENTRY.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);

                } else if (logEntry.getAction() == LogEntry.TAG) {
                    entryObj.put(Constants.LOGENTRY.ASSET_TYPE, processPath);
                    entryObj.put(Constants.LOGENTRY.ACTION, "tag");
                    entryObj.put(Constants.LOGENTRY.USER, logEntry.getUserName());
                    entryObj.put(Constants.LOGENTRY.ACTION_TYPE, type);
                    entryObj.put(Constants.LOGENTRY.TIME_STAMP, logEntry.getDate().getTime());
                    result.put(entryObj);
                }
            } catch (JSONException e) {
                String msg = "Error processing log entries";
                log.error(msg, e);
            }
        }
    }


    private static ArrayList<LogEntry> removeLogEntriesWithDuplicatePaths(LogEntry[] logEntries) {
        Set set = new HashSet();
        ArrayList newList = new ArrayList();
        for (int i = 0; i < logEntries.length; i++) {
            if (!set.contains(logEntries[i].getResourcePath())) {
                if (logEntries[i].getAction() == LogEntry.DELETE_RESOURCE ||
                        logEntries[i].getAction() == LogEntry.UPDATE ||
                        logEntries[i].getAction() == LogEntry.DELETE_COMMENT ||
                        logEntries[i].getAction() == LogEntry.REMOVE_TAG ||
                        logEntries[i].getAction() == LogEntry.ADD ||
                        logEntries[i].getAction() == LogEntry.TAG ||
                        logEntries[i].getAction() == LogEntry.COMMENT ||
                        logEntries[i].getAction() == LogEntry.ADD_ASSOCIATION ||
                        logEntries[i].getAction() == LogEntry.MOVE ||
                        logEntries[i].getAction() == LogEntry.COPY ||
                        logEntries[i].getAction() == LogEntry.RENAME ||
                        logEntries[i].getAction() == LogEntry.RESTORE) {
                    if (logEntries[i].getAction() != LogEntry.COPY) {
                        set.add(logEntries[i].getResourcePath());
                    }
                    newList.add(logEntries[i]);
                }
            }
        }
        return newList;
    }
}
