BPMN-Analytics-Explorer
=======================

BPMN-Analytics-Explorer jaggery based web-app providing user interface to visualize summarized data in graphical charts.

Configuring BPMN-Analytics-Explorer
===================================

1. Upload <BPS_HOME>/analytics/Process_Center_Analytics.car file to Data Analytics Server.

2. Copy <BPS_HOME>/analytics/PCAnalyticsUDF.jar file to <DAS_HOME>/repository/components/lib/ folder.

3. Add the following configuration line into <DAS_HOME>/repository/conf/analytics/spark/spark-udf-config.xml file.
    <class-name>org.wso2.carbon.pc.analytics.udf.AnalyticsUDF</class-name>

4. Restart the Data Analytics Server.