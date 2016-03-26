package org.wso2.carbon.pc.analytics.config;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.pc.analytics.config.utils.DASConfigurationUtils;
import org.wso2.carbon.registry.core.utils.RegistryUtils;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by samithac on 24/3/16.
 */
public class BPSConfigRestClient {
    private static final Log log = LogFactory.getLog(BPSConfigRestClient.class);

    /**
     * Send post request to a BPS BPMN rest web service
     * @param url used to locate the webservice functionality
     * @param message is the request message that need to be sent to the web service
     * @return the result as a String
     */
    public static String post(String message) throws IOException, XMLStreamException {
        String url=DASConfigurationUtils.getURL();
        RegistryUtils.setTrustStoreSystemProperties();
        HttpClient httpClient = new HttpClient();
        PostMethod postRequest = new PostMethod(url);
        postRequest.setRequestHeader("Authorization", DASConfigurationUtils.getAuthorizationHeader());
        BufferedReader br = null;
        try {
            StringRequestEntity input =
                    new StringRequestEntity(message, "application/json", "UTF-8");
            postRequest.setRequestEntity(input);

            int returnCode = httpClient.executeMethod(postRequest);

            if (returnCode != HttpStatus.SC_OK) {
                String errorCode = "Failed : HTTP error code : " + returnCode;
                throw new RuntimeException(errorCode);
            }

            InputStreamReader reader =
                    new InputStreamReader((postRequest.getResponseBodyAsStream()));
            br = new BufferedReader(reader);

            String output = null;
            StringBuilder totalOutput = new StringBuilder();

            if (log.isDebugEnabled()) {
                log.debug("Output from Server .... \n");
            }

            while ((output = br.readLine()) != null) {
                totalOutput.append(output);
            }

            if (log.isDebugEnabled()) {
                log.debug("Output = " + totalOutput.toString());
            }

            return totalOutput.toString();

        } catch (UnsupportedEncodingException e) {
            String errMsg = "Async BPS client unsupported encoding exception.";
            throw new UnsupportedEncodingException(errMsg);
        } catch (UnsupportedOperationException e) {
            String errMsg = "Async BPS client unsupported operation exception.";
            throw new UnsupportedOperationException(errMsg);
        } catch (IOException e) {
            String errMsg = "Async BPS client I/O exception.";
            log.error(errMsg, e);
        } finally {
            postRequest.releaseConnection();
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    String errMsg = "Async BPS rest client BufferedReader close exception.";
                    log.error(errMsg, e);
                }
            }
        }
        return null;
    }



}
