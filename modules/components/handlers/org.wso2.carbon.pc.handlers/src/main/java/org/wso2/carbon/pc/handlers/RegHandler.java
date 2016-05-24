package org.wso2.carbon.pc.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.ResourceImpl;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.jdbc.handlers.Handler;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;

public class RegHandler extends Handler{

    private static final Log log = LogFactory.getLog(RegHandler.class);

    public void put(RequestContext requestContext) throws RegistryException {
        Resource resource = requestContext.getResource();

        ((ResourceImpl) resource).setAuthorUserName("test_user");
        ((ResourceImpl) resource).prepareContentForPut();

        requestContext.getRepository().put(requestContext.getResourcePath().getPath(),resource);
        requestContext.setProcessingComplete(true);

    }
}
