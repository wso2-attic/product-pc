
/**
 * EventStreamAdminServiceStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.1-wso2v14  Built on : Jul 25, 2015 (11:19:54 IST)
 */
package org.wso2.carbon.pc.analytics.config.stubs;

        

        /*
        *  EventStreamAdminServiceStub java implementation
        */


public class EventStreamAdminServiceStub extends org.apache.axis2.client.Stub
{
    protected org.apache.axis2.description.AxisOperation[] _operations;

    //hashmaps to keep the fault mapping
    private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
    private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
    private java.util.HashMap faultMessageMap = new java.util.HashMap();

    private static int counter = 0;

    private static synchronized java.lang.String getUniqueSuffix(){
        // reset the counter if it is greater than 99999
        if (counter > 99999){
            counter = 0;
        }
        counter = counter + 1;
        return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
    }


    private void populateAxisService() throws org.apache.axis2.AxisFault {

        //creating the Service with a unique name
        _service = new org.apache.axis2.description.AxisService("EventStreamAdminService" + getUniqueSuffix());
        addAnonymousOperations();

        //creating the operations
        org.apache.axis2.description.AxisOperation __operation;

        _operations = new org.apache.axis2.description.AxisOperation[13];

        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "addEventStreamDefinitionAsDto"));
        _service.addOperation(__operation);




        _operations[0]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "getStreamDefinitionDto"));
        _service.addOperation(__operation);




        _operations[1]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "convertEventStreamDefinitionDtoToString"));
        _service.addOperation(__operation);




        _operations[2]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "editEventStreamDefinitionAsString"));
        _service.addOperation(__operation);




        _operations[3]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "getStreamDefinitionAsString"));
        _service.addOperation(__operation);




        _operations[4]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "getAllEventStreamDefinitionDto"));
        _service.addOperation(__operation);




        _operations[5]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "removeEventStreamDefinition"));
        _service.addOperation(__operation);




        _operations[6]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "addEventStreamDefinitionAsString"));
        _service.addOperation(__operation);




        _operations[7]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "getStreamDetailsForStreamId"));
        _service.addOperation(__operation);




        _operations[8]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "editEventStreamDefinitionAsDto"));
        _service.addOperation(__operation);




        _operations[9]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "convertStringToEventStreamDefinitionDto"));
        _service.addOperation(__operation);




        _operations[10]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "getStreamNames"));
        _service.addOperation(__operation);




        _operations[11]=__operation;


        __operation = new org.apache.axis2.description.OutInAxisOperation();


        __operation.setName(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org", "generateSampleEvent"));
        _service.addOperation(__operation);




        _operations[12]=__operation;


    }

    //populates the faults
    private void populateFaults(){



    }

    /**
     *Constructor that takes in a configContext
     */

    public EventStreamAdminServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
                                       java.lang.String targetEndpoint)
            throws org.apache.axis2.AxisFault {
        this(configurationContext,targetEndpoint,false);
    }


    /**
     * Constructor that takes in a configContext  and useseperate listner
     */
    public EventStreamAdminServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
                                       java.lang.String targetEndpoint, boolean useSeparateListener)
            throws org.apache.axis2.AxisFault {
        //To populate AxisService
        populateAxisService();
        populateFaults();

        _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext,_service);


        _serviceClient.getOptions().setTo(new org.apache.axis2.addressing.EndpointReference(
                targetEndpoint));
        _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

        //Set the soap version
        _serviceClient.getOptions().setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);


    }

    /**
     * Default Constructor
     */
    public EventStreamAdminServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext) throws org.apache.axis2.AxisFault {

        this(configurationContext,"https://127.0.0.1:9443/services/EventStreamAdminService.EventStreamAdminServiceHttpsSoap12Endpoint/" );

    }

    /**
     * Default Constructor
     */
    public EventStreamAdminServiceStub() throws org.apache.axis2.AxisFault {

        this("https://127.0.0.1:9443/services/EventStreamAdminService.EventStreamAdminServiceHttpsSoap12Endpoint/" );

    }

    /**
     * Constructor taking the target endpoint
     */
    public EventStreamAdminServiceStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
        this(null,targetEndpoint);
    }




    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#addEventStreamDefinitionAsDto
     * @param addEventStreamDefinitionAsDto

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDtoResponse addEventStreamDefinitionAsDto(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDto addEventStreamDefinitionAsDto)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[0].getName());
            _operationClient.getOptions().setAction("urn:addEventStreamDefinitionAsDto");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    addEventStreamDefinitionAsDto,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "addEventStreamDefinitionAsDto")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "addEventStreamDefinitionAsDto"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDtoResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDtoResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addEventStreamDefinitionAsDto"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addEventStreamDefinitionAsDto"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addEventStreamDefinitionAsDto"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#getStreamDefinitionDto
     * @param getStreamDefinitionDto

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDtoResponse getStreamDefinitionDto(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDto getStreamDefinitionDto)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[1].getName());
            _operationClient.getOptions().setAction("urn:getStreamDefinitionDto");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getStreamDefinitionDto,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamDefinitionDto")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamDefinitionDto"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDtoResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDtoResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDefinitionDto"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDefinitionDto"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDefinitionDto"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#convertEventStreamDefinitionDtoToString
     * @param convertEventStreamDefinitionDtoToString

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToStringResponse convertEventStreamDefinitionDtoToString(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToString convertEventStreamDefinitionDtoToString)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[2].getName());
            _operationClient.getOptions().setAction("urn:convertEventStreamDefinitionDtoToString");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    convertEventStreamDefinitionDtoToString,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "convertEventStreamDefinitionDtoToString")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "convertEventStreamDefinitionDtoToString"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToStringResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToStringResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"convertEventStreamDefinitionDtoToString"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"convertEventStreamDefinitionDtoToString"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"convertEventStreamDefinitionDtoToString"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#editEventStreamDefinitionAsString
     * @param editEventStreamDefinitionAsString

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsStringResponse editEventStreamDefinitionAsString(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsString editEventStreamDefinitionAsString)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[3].getName());
            _operationClient.getOptions().setAction("urn:editEventStreamDefinitionAsString");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    editEventStreamDefinitionAsString,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "editEventStreamDefinitionAsString")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "editEventStreamDefinitionAsString"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsStringResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsStringResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"editEventStreamDefinitionAsString"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"editEventStreamDefinitionAsString"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"editEventStreamDefinitionAsString"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#getStreamDefinitionAsString
     * @param getStreamDefinitionAsString

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsStringResponse getStreamDefinitionAsString(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsString getStreamDefinitionAsString)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[4].getName());
            _operationClient.getOptions().setAction("urn:getStreamDefinitionAsString");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getStreamDefinitionAsString,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamDefinitionAsString")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamDefinitionAsString"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsStringResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsStringResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDefinitionAsString"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDefinitionAsString"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDefinitionAsString"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#getAllEventStreamDefinitionDto
     * @param getAllEventStreamDefinitionDto

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse getAllEventStreamDefinitionDto(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto getAllEventStreamDefinitionDto)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[5].getName());
            _operationClient.getOptions().setAction("urn:getAllEventStreamDefinitionDto");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getAllEventStreamDefinitionDto,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getAllEventStreamDefinitionDto")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getAllEventStreamDefinitionDto"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getAllEventStreamDefinitionDto"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getAllEventStreamDefinitionDto"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getAllEventStreamDefinitionDto"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#removeEventStreamDefinition
     * @param removeEventStreamDefinition

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinitionResponse removeEventStreamDefinition(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinition removeEventStreamDefinition)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[6].getName());
            _operationClient.getOptions().setAction("urn:removeEventStreamDefinition");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    removeEventStreamDefinition,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "removeEventStreamDefinition")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "removeEventStreamDefinition"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinitionResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinitionResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"removeEventStreamDefinition"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"removeEventStreamDefinition"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"removeEventStreamDefinition"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#addEventStreamDefinitionAsString
     * @param addEventStreamDefinitionAsString

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse addEventStreamDefinitionAsString(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString addEventStreamDefinitionAsString)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[7].getName());
            _operationClient.getOptions().setAction("urn:addEventStreamDefinitionAsString");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    addEventStreamDefinitionAsString,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "addEventStreamDefinitionAsString")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "addEventStreamDefinitionAsString"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addEventStreamDefinitionAsString"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addEventStreamDefinitionAsString"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"addEventStreamDefinitionAsString"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#getStreamDetailsForStreamId
     * @param getStreamDetailsForStreamId

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse getStreamDetailsForStreamId(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId getStreamDetailsForStreamId)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[8].getName());
            _operationClient.getOptions().setAction("urn:getStreamDetailsForStreamId");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getStreamDetailsForStreamId,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamDetailsForStreamId")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamDetailsForStreamId"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDetailsForStreamId"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDetailsForStreamId"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamDetailsForStreamId"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#editEventStreamDefinitionAsDto
     * @param editEventStreamDefinitionAsDto

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDtoResponse editEventStreamDefinitionAsDto(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDto editEventStreamDefinitionAsDto)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[9].getName());
            _operationClient.getOptions().setAction("urn:editEventStreamDefinitionAsDto");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    editEventStreamDefinitionAsDto,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "editEventStreamDefinitionAsDto")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "editEventStreamDefinitionAsDto"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDtoResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDtoResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"editEventStreamDefinitionAsDto"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"editEventStreamDefinitionAsDto"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"editEventStreamDefinitionAsDto"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#convertStringToEventStreamDefinitionDto
     * @param convertStringToEventStreamDefinitionDto

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDtoResponse convertStringToEventStreamDefinitionDto(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDto convertStringToEventStreamDefinitionDto)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[10].getName());
            _operationClient.getOptions().setAction("urn:convertStringToEventStreamDefinitionDto");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    convertStringToEventStreamDefinitionDto,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "convertStringToEventStreamDefinitionDto")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "convertStringToEventStreamDefinitionDto"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDtoResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDtoResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"convertStringToEventStreamDefinitionDto"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"convertStringToEventStreamDefinitionDto"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"convertStringToEventStreamDefinitionDto"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#getStreamNames
     * @param getStreamNames

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNamesResponse getStreamNames(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames getStreamNames)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[11].getName());
            _operationClient.getOptions().setAction("urn:getStreamNames");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getStreamNames,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamNames")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "getStreamNames"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNamesResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNamesResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamNames"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamNames"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"getStreamNames"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    /**
     * Auto generated method signature
     *
     * @see org.wso2.carbon.event.stream.admin.EventStreamAdminService#generateSampleEvent
     * @param generateSampleEvent

     */



    public  org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEventResponse generateSampleEvent(

            org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEvent generateSampleEvent)


            throws java.rmi.RemoteException

    {
        org.apache.axis2.context.MessageContext _messageContext = null;
        try{
            org.apache.axis2.client.OperationClient _operationClient = _serviceClient.createClient(_operations[12].getName());
            _operationClient.getOptions().setAction("urn:generateSampleEvent");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);



            addPropertyToOperationClient(_operationClient,org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,"&");


            // create a message context
            _messageContext = new org.apache.axis2.context.MessageContext();



            // create SOAP envelope with that payload
            org.apache.axiom.soap.SOAPEnvelope env = null;


            env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    generateSampleEvent,
                    optimizeContent(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "generateSampleEvent")), new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "generateSampleEvent"));

            //adding SOAP soap_headers
            _serviceClient.addHeadersToEnvelope(env);
            // set the message context with that soap envelope
            _messageContext.setEnvelope(env);

            // add the message contxt to the operation client
            _operationClient.addMessageContext(_messageContext);

            //execute the operation client
            _operationClient.execute(true);


            org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient.getMessageContext(
                    org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();


            java.lang.Object object = fromOM(
                    _returnEnv.getBody().getFirstElement() ,
                    org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEventResponse.class,
                    getEnvelopeNamespaces(_returnEnv));


            return (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEventResponse)object;

        }catch(org.apache.axis2.AxisFault f){

            org.apache.axiom.om.OMElement faultElt = f.getDetail();
            if (faultElt!=null){
                if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"generateSampleEvent"))){
                    //make the fault by reflection
                    try{
                        java.lang.String exceptionClassName = (java.lang.String)faultExceptionClassNameMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"generateSampleEvent"));
                        java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                        java.lang.Exception ex = (java.lang.Exception) exceptionClass.newInstance();
                        //message class
                        java.lang.String messageClassName = (java.lang.String)faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),"generateSampleEvent"));
                        java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                        java.lang.Object messageObject = fromOM(faultElt,messageClass,null);
                        java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                                new java.lang.Class[]{messageClass});
                        m.invoke(ex,new java.lang.Object[]{messageObject});


                        throw new java.rmi.RemoteException(ex.getMessage(), ex);
                    }catch(java.lang.ClassCastException e){
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.ClassNotFoundException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }catch (java.lang.NoSuchMethodException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }  catch (java.lang.IllegalAccessException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }   catch (java.lang.InstantiationException e) {
                        // we cannot intantiate the class - throw the original Axis fault
                        throw f;
                    }
                }else{
                    throw f;
                }
            }else{
                throw f;
            }
        } finally {
            if (_messageContext.getTransportOut() != null) {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }



    /**
     *  A utility method that copies the namepaces from the SOAPEnvelope
     */
    private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
            org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
    }



    private javax.xml.namespace.QName[] opNameArray = null;
    private boolean optimizeContent(javax.xml.namespace.QName opName) {


        if (opNameArray == null) {
            return false;
        }
        for (int i = 0; i < opNameArray.length; i++) {
            if (opName.equals(opNameArray[i])) {
                return true;
            }
        }
        return false;
    }
    //https://127.0.0.1:9443/services/EventStreamAdminService.EventStreamAdminServiceHttpsSoap12Endpoint/
    public static class EventStreamInfoDto
            implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = EventStreamInfoDto
                Namespace URI = http://admin.stream.event.carbon.wso2.org/xsd
                Namespace Prefix = ns30
                */


        /**
         * field for Editable
         */


        protected boolean localEditable ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEditableTracker = false ;

        public boolean isEditableSpecified(){
            return localEditableTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getEditable(){
            return localEditable;
        }



        /**
         * Auto generated setter method
         * @param param Editable
         */
        public void setEditable(boolean param){

            // setting primitive attribute tracker to true
            localEditableTracker =
                    true;

            this.localEditable=param;


        }


        /**
         * field for StreamDefinition
         */


        protected java.lang.String localStreamDefinition ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamDefinitionTracker = false ;

        public boolean isStreamDefinitionSpecified(){
            return localStreamDefinitionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamDefinition(){
            return localStreamDefinition;
        }



        /**
         * Auto generated setter method
         * @param param StreamDefinition
         */
        public void setStreamDefinition(java.lang.String param){
            localStreamDefinitionTracker = true;

            this.localStreamDefinition=param;


        }


        /**
         * field for StreamDescription
         */


        protected java.lang.String localStreamDescription ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamDescriptionTracker = false ;

        public boolean isStreamDescriptionSpecified(){
            return localStreamDescriptionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamDescription(){
            return localStreamDescription;
        }



        /**
         * Auto generated setter method
         * @param param StreamDescription
         */
        public void setStreamDescription(java.lang.String param){
            localStreamDescriptionTracker = true;

            this.localStreamDescription=param;


        }


        /**
         * field for StreamName
         */


        protected java.lang.String localStreamName ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamNameTracker = false ;

        public boolean isStreamNameSpecified(){
            return localStreamNameTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamName(){
            return localStreamName;
        }



        /**
         * Auto generated setter method
         * @param param StreamName
         */
        public void setStreamName(java.lang.String param){
            localStreamNameTracker = true;

            this.localStreamName=param;


        }


        /**
         * field for StreamVersion
         */


        protected java.lang.String localStreamVersion ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamVersionTracker = false ;

        public boolean isStreamVersionSpecified(){
            return localStreamVersionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamVersion(){
            return localStreamVersion;
        }



        /**
         * Auto generated setter method
         * @param param StreamVersion
         */
        public void setStreamVersion(java.lang.String param){
            localStreamVersionTracker = true;

            this.localStreamVersion=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
            return factory.createOMElement(dataSource,parentQName);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org/xsd");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":EventStreamInfoDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "EventStreamInfoDto",
                            xmlWriter);
                }


            }
            if (localEditableTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "editable", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("editable cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEditable));
                }

                xmlWriter.writeEndElement();
            } if (localStreamDefinitionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "streamDefinition", xmlWriter);


                if (localStreamDefinition==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamDefinition);

                }

                xmlWriter.writeEndElement();
            } if (localStreamDescriptionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "streamDescription", xmlWriter);


                if (localStreamDescription==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamDescription);

                }

                xmlWriter.writeEndElement();
            } if (localStreamNameTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "streamName", xmlWriter);


                if (localStreamName==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamName);

                }

                xmlWriter.writeEndElement();
            } if (localStreamVersionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "streamVersion", xmlWriter);


                if (localStreamVersion==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamVersion);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org/xsd")){
                return "ns30";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localEditableTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "editable"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEditable));
            } if (localStreamDefinitionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "streamDefinition"));

                elementList.add(localStreamDefinition==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamDefinition));
            } if (localStreamDescriptionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "streamDescription"));

                elementList.add(localStreamDescription==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamDescription));
            } if (localStreamNameTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "streamName"));

                elementList.add(localStreamName==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamName));
            } if (localStreamVersionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "streamVersion"));

                elementList.add(localStreamVersion==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamVersion));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static EventStreamInfoDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                EventStreamInfoDto object =
                        new EventStreamInfoDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"EventStreamInfoDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (EventStreamInfoDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","editable").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"editable" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setEditable(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","streamDefinition").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamDefinition(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","streamDescription").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamDescription(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","streamName").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamName(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","streamVersion").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamVersion(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ConvertEventStreamDefinitionDtoToStringResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "convertEventStreamDefinitionDtoToStringResponse",
                "ns31");



        /**
         * field for _return
         */


        protected java.lang.String local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String param){
            local_returnTracker = true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":convertEventStreamDefinitionDtoToStringResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "convertEventStreamDefinitionDtoToStringResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);


                if (local_return==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(local_return);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(local_return==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static ConvertEventStreamDefinitionDtoToStringResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ConvertEventStreamDefinitionDtoToStringResponse object =
                        new ConvertEventStreamDefinitionDtoToStringResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"convertEventStreamDefinitionDtoToStringResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ConvertEventStreamDefinitionDtoToStringResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.set_return(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class AddEventStreamDefinitionAsDtoResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "addEventStreamDefinitionAsDtoResponse",
                "ns31");



        /**
         * field for _return
         */


        protected boolean local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":addEventStreamDefinitionAsDtoResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "addEventStreamDefinitionAsDtoResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static AddEventStreamDefinitionAsDtoResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                AddEventStreamDefinitionAsDtoResponse object =
                        new AddEventStreamDefinitionAsDtoResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"addEventStreamDefinitionAsDtoResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddEventStreamDefinitionAsDtoResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamDefinitionDto
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamDefinitionDto",
                "ns31");



        /**
         * field for StreamId
         */


        protected java.lang.String localStreamId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamIdTracker = false ;

        public boolean isStreamIdSpecified(){
            return localStreamIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamId(){
            return localStreamId;
        }



        /**
         * Auto generated setter method
         * @param param StreamId
         */
        public void setStreamId(java.lang.String param){
            localStreamIdTracker = true;

            this.localStreamId=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamDefinitionDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamDefinitionDto",
                            xmlWriter);
                }


            }
            if (localStreamIdTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "streamId", xmlWriter);


                if (localStreamId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamId);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localStreamIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "streamId"));

                elementList.add(localStreamId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamId));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamDefinitionDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamDefinitionDto object =
                        new GetStreamDefinitionDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamDefinitionDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamDefinitionDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","streamId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GenerateSampleEvent
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "generateSampleEvent",
                "ns31");



        /**
         * field for StreamId
         */


        protected java.lang.String localStreamId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamIdTracker = false ;

        public boolean isStreamIdSpecified(){
            return localStreamIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamId(){
            return localStreamId;
        }



        /**
         * Auto generated setter method
         * @param param StreamId
         */
        public void setStreamId(java.lang.String param){
            localStreamIdTracker = true;

            this.localStreamId=param;


        }


        /**
         * field for EventType
         */


        protected java.lang.String localEventType ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEventTypeTracker = false ;

        public boolean isEventTypeSpecified(){
            return localEventTypeTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getEventType(){
            return localEventType;
        }



        /**
         * Auto generated setter method
         * @param param EventType
         */
        public void setEventType(java.lang.String param){
            localEventTypeTracker = true;

            this.localEventType=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":generateSampleEvent",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "generateSampleEvent",
                            xmlWriter);
                }


            }
            if (localStreamIdTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "streamId", xmlWriter);


                if (localStreamId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamId);

                }

                xmlWriter.writeEndElement();
            } if (localEventTypeTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "eventType", xmlWriter);


                if (localEventType==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localEventType);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localStreamIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "streamId"));

                elementList.add(localStreamId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamId));
            } if (localEventTypeTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "eventType"));

                elementList.add(localEventType==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEventType));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GenerateSampleEvent parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GenerateSampleEvent object =
                        new GenerateSampleEvent();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"generateSampleEvent".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GenerateSampleEvent)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","streamId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventType").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setEventType(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GenerateSampleEventResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "generateSampleEventResponse",
                "ns31");



        /**
         * field for _return
         */


        protected java.lang.String local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String param){
            local_returnTracker = true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":generateSampleEventResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "generateSampleEventResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);


                if (local_return==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(local_return);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(local_return==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GenerateSampleEventResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GenerateSampleEventResponse object =
                        new GenerateSampleEventResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"generateSampleEventResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GenerateSampleEventResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.set_return(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetAllEventStreamDefinitionDto
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getAllEventStreamDefinitionDto",
                "ns31");





        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getAllEventStreamDefinitionDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getAllEventStreamDefinitionDto",
                            xmlWriter);
                }


            }

            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();



            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetAllEventStreamDefinitionDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetAllEventStreamDefinitionDto object =
                        new GetAllEventStreamDefinitionDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getAllEventStreamDefinitionDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetAllEventStreamDefinitionDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class AddEventStreamDefinitionAsDto
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "addEventStreamDefinitionAsDto",
                "ns31");



        /**
         * field for EventStreamDefinitionDto
         */


        protected EventStreamDefinitionDto localEventStreamDefinitionDto ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEventStreamDefinitionDtoTracker = false ;

        public boolean isEventStreamDefinitionDtoSpecified(){
            return localEventStreamDefinitionDtoTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamDefinitionDto
         */
        public  EventStreamDefinitionDto getEventStreamDefinitionDto(){
            return localEventStreamDefinitionDto;
        }



        /**
         * Auto generated setter method
         * @param param EventStreamDefinitionDto
         */
        public void setEventStreamDefinitionDto(EventStreamDefinitionDto param){
            localEventStreamDefinitionDtoTracker = true;

            this.localEventStreamDefinitionDto=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":addEventStreamDefinitionAsDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "addEventStreamDefinitionAsDto",
                            xmlWriter);
                }


            }
            if (localEventStreamDefinitionDtoTracker){
                if (localEventStreamDefinitionDto==null){

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "eventStreamDefinitionDto", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    localEventStreamDefinitionDto.serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamDefinitionDto"),
                            xmlWriter);
                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localEventStreamDefinitionDtoTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "eventStreamDefinitionDto"));


                elementList.add(localEventStreamDefinitionDto==null?null:
                        localEventStreamDefinitionDto);
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static AddEventStreamDefinitionAsDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                AddEventStreamDefinitionAsDto object =
                        new AddEventStreamDefinitionAsDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"addEventStreamDefinitionAsDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddEventStreamDefinitionAsDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamDefinitionDto").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.setEventStreamDefinitionDto(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.setEventStreamDefinitionDto(EventStreamDefinitionDto.Factory.parse(reader));

                            reader.next();
                        }
                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class EditEventStreamDefinitionAsString
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "editEventStreamDefinitionAsString",
                "ns31");



        /**
         * field for StreamStringDefinition
         */


        protected java.lang.String localStreamStringDefinition ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamStringDefinitionTracker = false ;

        public boolean isStreamStringDefinitionSpecified(){
            return localStreamStringDefinitionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamStringDefinition(){
            return localStreamStringDefinition;
        }



        /**
         * Auto generated setter method
         * @param param StreamStringDefinition
         */
        public void setStreamStringDefinition(java.lang.String param){
            localStreamStringDefinitionTracker = true;

            this.localStreamStringDefinition=param;


        }


        /**
         * field for OldStreamId
         */


        protected java.lang.String localOldStreamId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localOldStreamIdTracker = false ;

        public boolean isOldStreamIdSpecified(){
            return localOldStreamIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getOldStreamId(){
            return localOldStreamId;
        }



        /**
         * Auto generated setter method
         * @param param OldStreamId
         */
        public void setOldStreamId(java.lang.String param){
            localOldStreamIdTracker = true;

            this.localOldStreamId=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":editEventStreamDefinitionAsString",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "editEventStreamDefinitionAsString",
                            xmlWriter);
                }


            }
            if (localStreamStringDefinitionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "streamStringDefinition", xmlWriter);


                if (localStreamStringDefinition==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamStringDefinition);

                }

                xmlWriter.writeEndElement();
            } if (localOldStreamIdTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "oldStreamId", xmlWriter);


                if (localOldStreamId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localOldStreamId);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localStreamStringDefinitionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "streamStringDefinition"));

                elementList.add(localStreamStringDefinition==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamStringDefinition));
            } if (localOldStreamIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "oldStreamId"));

                elementList.add(localOldStreamId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOldStreamId));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static EditEventStreamDefinitionAsString parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                EditEventStreamDefinitionAsString object =
                        new EditEventStreamDefinitionAsString();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"editEventStreamDefinitionAsString".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (EditEventStreamDefinitionAsString)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","streamStringDefinition").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamStringDefinition(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","oldStreamId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setOldStreamId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ExtensionMapper{

        public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                     java.lang.String typeName,
                                                     javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{


            if (
                    "http://admin.stream.event.carbon.wso2.org/xsd".equals(namespaceURI) &&
                            "EventStreamAttributeDto".equals(typeName)){

                return  EventStreamAttributeDto.Factory.parse(reader);


            }


            if (
                    "http://admin.stream.event.carbon.wso2.org/xsd".equals(namespaceURI) &&
                            "EventStreamInfoDto".equals(typeName)){

                return  EventStreamInfoDto.Factory.parse(reader);


            }


            if (
                    "http://admin.stream.event.carbon.wso2.org/xsd".equals(namespaceURI) &&
                            "EventStreamDefinitionDto".equals(typeName)){

                return  EventStreamDefinitionDto.Factory.parse(reader);


            }


            throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
        }

    }

    public static class EditEventStreamDefinitionAsStringResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "editEventStreamDefinitionAsStringResponse",
                "ns31");



        /**
         * field for _return
         */


        protected boolean local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":editEventStreamDefinitionAsStringResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "editEventStreamDefinitionAsStringResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static EditEventStreamDefinitionAsStringResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                EditEventStreamDefinitionAsStringResponse object =
                        new EditEventStreamDefinitionAsStringResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"editEventStreamDefinitionAsStringResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (EditEventStreamDefinitionAsStringResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamDefinitionAsString
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamDefinitionAsString",
                "ns31");



        /**
         * field for StreamId
         */


        protected java.lang.String localStreamId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamIdTracker = false ;

        public boolean isStreamIdSpecified(){
            return localStreamIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamId(){
            return localStreamId;
        }



        /**
         * Auto generated setter method
         * @param param StreamId
         */
        public void setStreamId(java.lang.String param){
            localStreamIdTracker = true;

            this.localStreamId=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamDefinitionAsString",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamDefinitionAsString",
                            xmlWriter);
                }


            }
            if (localStreamIdTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "streamId", xmlWriter);


                if (localStreamId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamId);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localStreamIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "streamId"));

                elementList.add(localStreamId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamId));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamDefinitionAsString parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamDefinitionAsString object =
                        new GetStreamDefinitionAsString();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamDefinitionAsString".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamDefinitionAsString)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","streamId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ConvertStringToEventStreamDefinitionDto
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "convertStringToEventStreamDefinitionDto",
                "ns31");



        /**
         * field for StreamStringDefinition
         */


        protected java.lang.String localStreamStringDefinition ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamStringDefinitionTracker = false ;

        public boolean isStreamStringDefinitionSpecified(){
            return localStreamStringDefinitionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamStringDefinition(){
            return localStreamStringDefinition;
        }



        /**
         * Auto generated setter method
         * @param param StreamStringDefinition
         */
        public void setStreamStringDefinition(java.lang.String param){
            localStreamStringDefinitionTracker = true;

            this.localStreamStringDefinition=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":convertStringToEventStreamDefinitionDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "convertStringToEventStreamDefinitionDto",
                            xmlWriter);
                }


            }
            if (localStreamStringDefinitionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "streamStringDefinition", xmlWriter);


                if (localStreamStringDefinition==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamStringDefinition);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localStreamStringDefinitionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "streamStringDefinition"));

                elementList.add(localStreamStringDefinition==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamStringDefinition));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static ConvertStringToEventStreamDefinitionDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ConvertStringToEventStreamDefinitionDto object =
                        new ConvertStringToEventStreamDefinitionDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"convertStringToEventStreamDefinitionDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ConvertStringToEventStreamDefinitionDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","streamStringDefinition").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamStringDefinition(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ConvertEventStreamDefinitionDtoToString
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "convertEventStreamDefinitionDtoToString",
                "ns31");



        /**
         * field for EventStreamDefinitionDto
         */


        protected EventStreamDefinitionDto localEventStreamDefinitionDto ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEventStreamDefinitionDtoTracker = false ;

        public boolean isEventStreamDefinitionDtoSpecified(){
            return localEventStreamDefinitionDtoTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamDefinitionDto
         */
        public  EventStreamDefinitionDto getEventStreamDefinitionDto(){
            return localEventStreamDefinitionDto;
        }



        /**
         * Auto generated setter method
         * @param param EventStreamDefinitionDto
         */
        public void setEventStreamDefinitionDto(EventStreamDefinitionDto param){
            localEventStreamDefinitionDtoTracker = true;

            this.localEventStreamDefinitionDto=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":convertEventStreamDefinitionDtoToString",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "convertEventStreamDefinitionDtoToString",
                            xmlWriter);
                }


            }
            if (localEventStreamDefinitionDtoTracker){
                if (localEventStreamDefinitionDto==null){

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "eventStreamDefinitionDto", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    localEventStreamDefinitionDto.serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamDefinitionDto"),
                            xmlWriter);
                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localEventStreamDefinitionDtoTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "eventStreamDefinitionDto"));


                elementList.add(localEventStreamDefinitionDto==null?null:
                        localEventStreamDefinitionDto);
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static ConvertEventStreamDefinitionDtoToString parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ConvertEventStreamDefinitionDtoToString object =
                        new ConvertEventStreamDefinitionDtoToString();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"convertEventStreamDefinitionDtoToString".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ConvertEventStreamDefinitionDtoToString)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamDefinitionDto").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.setEventStreamDefinitionDto(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.setEventStreamDefinitionDto(EventStreamDefinitionDto.Factory.parse(reader));

                            reader.next();
                        }
                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamDetailsForStreamIdResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamDetailsForStreamIdResponse",
                "ns31");



        /**
         * field for _return
         * This was an Array!
         */


        protected java.lang.String[] local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String[]
         */
        public  java.lang.String[] get_return(){
            return local_return;
        }






        /**
         * validate the array for _return
         */
        protected void validate_return(java.lang.String[] param){

        }


        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String[] param){

            validate_return(param);

            local_returnTracker = true;

            this.local_return=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param java.lang.String
         */
        public void add_return(java.lang.String param){
            if (local_return == null){
                local_return = new java.lang.String[]{};
            }


            //update the setting tracker
            local_returnTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(local_return);
            list.add(param);
            this.local_return =
                    (java.lang.String[])list.toArray(
                            new java.lang.String[list.size()]);

        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamDetailsForStreamIdResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamDetailsForStreamIdResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return!=null) {
                    namespace = "http://admin.stream.event.carbon.wso2.org";
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){

                            writeStartElement(null, namespace, "return", xmlWriter);


                            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return[i]));

                            xmlWriter.writeEndElement();

                        } else {

                            // write null attribute
                            namespace = "http://admin.stream.event.carbon.wso2.org";
                            writeStartElement(null, namespace, "return", xmlWriter);
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    // write the null attribute
                    // write null attribute
                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }

            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                if (local_return!=null){
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                                    "return"));
                            elementList.add(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return[i]));
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                                    "return"));
                            elementList.add(null);

                        }


                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "return"));
                    elementList.add(null);

                }

            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamDetailsForStreamIdResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamDetailsForStreamIdResponse object =
                        new GetStreamDetailsForStreamIdResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamDetailsForStreamIdResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamDetailsForStreamIdResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);

                            reader.next();
                        } else {
                            list1.add(reader.getElementText());
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
                            // Ensure we are at the EndElement
                            while (!reader.isEndElement()){
                                reader.next();
                            }
                            // Step out of this element
                            reader.next();
                            // Step to next element event.
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            if (reader.isEndElement()){
                                //two continuous end elements means we are exiting the xml structure
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);

                                        reader.next();
                                    } else {
                                        list1.add(reader.getElementText());
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.set_return((java.lang.String[])
                                list1.toArray(new java.lang.String[list1.size()]));

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class EditEventStreamDefinitionAsDto
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "editEventStreamDefinitionAsDto",
                "ns31");



        /**
         * field for EventStreamDefinitionDto
         */


        protected EventStreamDefinitionDto localEventStreamDefinitionDto ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEventStreamDefinitionDtoTracker = false ;

        public boolean isEventStreamDefinitionDtoSpecified(){
            return localEventStreamDefinitionDtoTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamDefinitionDto
         */
        public  EventStreamDefinitionDto getEventStreamDefinitionDto(){
            return localEventStreamDefinitionDto;
        }



        /**
         * Auto generated setter method
         * @param param EventStreamDefinitionDto
         */
        public void setEventStreamDefinitionDto(EventStreamDefinitionDto param){
            localEventStreamDefinitionDtoTracker = true;

            this.localEventStreamDefinitionDto=param;


        }


        /**
         * field for OldStreamId
         */


        protected java.lang.String localOldStreamId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localOldStreamIdTracker = false ;

        public boolean isOldStreamIdSpecified(){
            return localOldStreamIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getOldStreamId(){
            return localOldStreamId;
        }



        /**
         * Auto generated setter method
         * @param param OldStreamId
         */
        public void setOldStreamId(java.lang.String param){
            localOldStreamIdTracker = true;

            this.localOldStreamId=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":editEventStreamDefinitionAsDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "editEventStreamDefinitionAsDto",
                            xmlWriter);
                }


            }
            if (localEventStreamDefinitionDtoTracker){
                if (localEventStreamDefinitionDto==null){

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "eventStreamDefinitionDto", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    localEventStreamDefinitionDto.serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamDefinitionDto"),
                            xmlWriter);
                }
            } if (localOldStreamIdTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "oldStreamId", xmlWriter);


                if (localOldStreamId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localOldStreamId);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localEventStreamDefinitionDtoTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "eventStreamDefinitionDto"));


                elementList.add(localEventStreamDefinitionDto==null?null:
                        localEventStreamDefinitionDto);
            } if (localOldStreamIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "oldStreamId"));

                elementList.add(localOldStreamId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localOldStreamId));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static EditEventStreamDefinitionAsDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                EditEventStreamDefinitionAsDto object =
                        new EditEventStreamDefinitionAsDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"editEventStreamDefinitionAsDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (EditEventStreamDefinitionAsDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamDefinitionDto").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.setEventStreamDefinitionDto(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.setEventStreamDefinitionDto(EventStreamDefinitionDto.Factory.parse(reader));

                            reader.next();
                        }
                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","oldStreamId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setOldStreamId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamDetailsForStreamId
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamDetailsForStreamId",
                "ns31");



        /**
         * field for StreamId
         */


        protected java.lang.String localStreamId ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamIdTracker = false ;

        public boolean isStreamIdSpecified(){
            return localStreamIdTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamId(){
            return localStreamId;
        }



        /**
         * Auto generated setter method
         * @param param StreamId
         */
        public void setStreamId(java.lang.String param){
            localStreamIdTracker = true;

            this.localStreamId=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamDetailsForStreamId",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamDetailsForStreamId",
                            xmlWriter);
                }


            }
            if (localStreamIdTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "streamId", xmlWriter);


                if (localStreamId==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamId);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localStreamIdTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "streamId"));

                elementList.add(localStreamId==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamId));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamDetailsForStreamId parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamDetailsForStreamId object =
                        new GetStreamDetailsForStreamId();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamDetailsForStreamId".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamDetailsForStreamId)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","streamId").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamId(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetAllEventStreamDefinitionDtoResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getAllEventStreamDefinitionDtoResponse",
                "ns31");



        /**
         * field for _return
         * This was an Array!
         */


        protected EventStreamInfoDto[] local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamInfoDto[]
         */
        public  EventStreamInfoDto[] get_return(){
            return local_return;
        }






        /**
         * validate the array for _return
         */
        protected void validate_return(EventStreamInfoDto[] param){

        }


        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(EventStreamInfoDto[] param){

            validate_return(param);

            local_returnTracker = true;

            this.local_return=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param EventStreamInfoDto
         */
        public void add_return(EventStreamInfoDto param){
            if (local_return == null){
                local_return = new EventStreamInfoDto[]{};
            }


            //update the setting tracker
            local_returnTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(local_return);
            list.add(param);
            this.local_return =
                    (EventStreamInfoDto[])list.toArray(
                            new EventStreamInfoDto[list.size()]);

        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getAllEventStreamDefinitionDtoResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getAllEventStreamDefinitionDtoResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return!=null){
                    for (int i = 0;i < local_return.length;i++){
                        if (local_return[i] != null){
                            local_return[i].serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "return", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                if (local_return!=null) {
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                                    "return"));
                            elementList.add(local_return[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                                    "return"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "return"));
                    elementList.add(local_return);

                }

            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetAllEventStreamDefinitionDtoResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetAllEventStreamDefinitionDtoResponse object =
                        new GetAllEventStreamDefinitionDtoResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getAllEventStreamDefinitionDtoResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetAllEventStreamDefinitionDtoResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(EventStreamInfoDto.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
                            // We should be at the end element, but make sure
                            while (!reader.isEndElement())
                                reader.next();
                            // Step out of this element
                            reader.next();
                            // Step to next element event.
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            if (reader.isEndElement()){
                                //two continuous end elements means we are exiting the xml structure
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(EventStreamInfoDto.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.set_return((EventStreamInfoDto[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        EventStreamInfoDto.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class EventStreamAttributeDto
            implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = EventStreamAttributeDto
                Namespace URI = http://admin.stream.event.carbon.wso2.org/xsd
                Namespace Prefix = ns30
                */


        /**
         * field for AttributeName
         */


        protected java.lang.String localAttributeName ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAttributeNameTracker = false ;

        public boolean isAttributeNameSpecified(){
            return localAttributeNameTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAttributeName(){
            return localAttributeName;
        }



        /**
         * Auto generated setter method
         * @param param AttributeName
         */
        public void setAttributeName(java.lang.String param){
            localAttributeNameTracker = true;

            this.localAttributeName=param;


        }


        /**
         * field for AttributeType
         */


        protected java.lang.String localAttributeType ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localAttributeTypeTracker = false ;

        public boolean isAttributeTypeSpecified(){
            return localAttributeTypeTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getAttributeType(){
            return localAttributeType;
        }



        /**
         * Auto generated setter method
         * @param param AttributeType
         */
        public void setAttributeType(java.lang.String param){
            localAttributeTypeTracker = true;

            this.localAttributeType=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
            return factory.createOMElement(dataSource,parentQName);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org/xsd");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":EventStreamAttributeDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "EventStreamAttributeDto",
                            xmlWriter);
                }


            }
            if (localAttributeNameTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "attributeName", xmlWriter);


                if (localAttributeName==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAttributeName);

                }

                xmlWriter.writeEndElement();
            } if (localAttributeTypeTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "attributeType", xmlWriter);


                if (localAttributeType==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localAttributeType);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org/xsd")){
                return "ns30";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localAttributeNameTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "attributeName"));

                elementList.add(localAttributeName==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAttributeName));
            } if (localAttributeTypeTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "attributeType"));

                elementList.add(localAttributeType==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAttributeType));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static EventStreamAttributeDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                EventStreamAttributeDto object =
                        new EventStreamAttributeDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"EventStreamAttributeDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (EventStreamAttributeDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","attributeName").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAttributeName(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","attributeType").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setAttributeType(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class AddEventStreamDefinitionAsStringResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "addEventStreamDefinitionAsStringResponse",
                "ns31");



        /**
         * field for _return
         */


        protected boolean local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":addEventStreamDefinitionAsStringResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "addEventStreamDefinitionAsStringResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static AddEventStreamDefinitionAsStringResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                AddEventStreamDefinitionAsStringResponse object =
                        new AddEventStreamDefinitionAsStringResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"addEventStreamDefinitionAsStringResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddEventStreamDefinitionAsStringResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamDefinitionDtoResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamDefinitionDtoResponse",
                "ns31");



        /**
         * field for _return
         */


        protected EventStreamDefinitionDto local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamDefinitionDto
         */
        public  EventStreamDefinitionDto get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(EventStreamDefinitionDto param){
            local_returnTracker = true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamDefinitionDtoResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamDefinitionDtoResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return==null){

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    local_return.serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return"),
                            xmlWriter);
                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));


                elementList.add(local_return==null?null:
                        local_return);
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamDefinitionDtoResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamDefinitionDtoResponse object =
                        new GetStreamDefinitionDtoResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamDefinitionDtoResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamDefinitionDtoResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.set_return(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.set_return(EventStreamDefinitionDto.Factory.parse(reader));

                            reader.next();
                        }
                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamDefinitionAsStringResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamDefinitionAsStringResponse",
                "ns31");



        /**
         * field for _return
         */


        protected java.lang.String local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String param){
            local_returnTracker = true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamDefinitionAsStringResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamDefinitionAsStringResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);


                if (local_return==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(local_return);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(local_return==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamDefinitionAsStringResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamDefinitionAsStringResponse object =
                        new GetStreamDefinitionAsStringResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamDefinitionAsStringResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamDefinitionAsStringResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.set_return(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class RemoveEventStreamDefinitionResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "removeEventStreamDefinitionResponse",
                "ns31");



        /**
         * field for _return
         */


        protected boolean local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":removeEventStreamDefinitionResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "removeEventStreamDefinitionResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static RemoveEventStreamDefinitionResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                RemoveEventStreamDefinitionResponse object =
                        new RemoveEventStreamDefinitionResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"removeEventStreamDefinitionResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RemoveEventStreamDefinitionResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class EditEventStreamDefinitionAsDtoResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "editEventStreamDefinitionAsDtoResponse",
                "ns31");



        /**
         * field for _return
         */


        protected boolean local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(boolean param){

            // setting primitive attribute tracker to true
            local_returnTracker =
                    true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":editEventStreamDefinitionAsDtoResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "editEventStreamDefinitionAsDtoResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "return", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("return cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static EditEventStreamDefinitionAsDtoResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                EditEventStreamDefinitionAsDtoResponse object =
                        new EditEventStreamDefinitionAsDtoResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"editEventStreamDefinitionAsDtoResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (EditEventStreamDefinitionAsDtoResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"return" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.set_return(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamNames
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamNames",
                "ns31");





        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamNames",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamNames",
                            xmlWriter);
                }


            }

            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();



            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamNames parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamNames object =
                        new GetStreamNames();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamNames".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamNames)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class GetStreamNamesResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "getStreamNamesResponse",
                "ns31");



        /**
         * field for _return
         * This was an Array!
         */


        protected java.lang.String[] local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String[]
         */
        public  java.lang.String[] get_return(){
            return local_return;
        }






        /**
         * validate the array for _return
         */
        protected void validate_return(java.lang.String[] param){

        }


        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(java.lang.String[] param){

            validate_return(param);

            local_returnTracker = true;

            this.local_return=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param java.lang.String
         */
        public void add_return(java.lang.String param){
            if (local_return == null){
                local_return = new java.lang.String[]{};
            }


            //update the setting tracker
            local_returnTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(local_return);
            list.add(param);
            this.local_return =
                    (java.lang.String[])list.toArray(
                            new java.lang.String[list.size()]);

        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":getStreamNamesResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "getStreamNamesResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return!=null) {
                    namespace = "http://admin.stream.event.carbon.wso2.org";
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){

                            writeStartElement(null, namespace, "return", xmlWriter);


                            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return[i]));

                            xmlWriter.writeEndElement();

                        } else {

                            // write null attribute
                            namespace = "http://admin.stream.event.carbon.wso2.org";
                            writeStartElement(null, namespace, "return", xmlWriter);
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    // write the null attribute
                    // write null attribute
                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }

            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                if (local_return!=null){
                    for (int i = 0;i < local_return.length;i++){

                        if (local_return[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                                    "return"));
                            elementList.add(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(local_return[i]));
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                                    "return"));
                            elementList.add(null);

                        }


                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                            "return"));
                    elementList.add(null);

                }

            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static GetStreamNamesResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                GetStreamNamesResponse object =
                        new GetStreamNamesResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"getStreamNamesResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (GetStreamNamesResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);

                            reader.next();
                        } else {
                            list1.add(reader.getElementText());
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
                            // Ensure we are at the EndElement
                            while (!reader.isEndElement()){
                                reader.next();
                            }
                            // Step out of this element
                            reader.next();
                            // Step to next element event.
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            if (reader.isEndElement()){
                                //two continuous end elements means we are exiting the xml structure
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);

                                        reader.next();
                                    } else {
                                        list1.add(reader.getElementText());
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.set_return((java.lang.String[])
                                list1.toArray(new java.lang.String[list1.size()]));

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class ConvertStringToEventStreamDefinitionDtoResponse
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "convertStringToEventStreamDefinitionDtoResponse",
                "ns31");



        /**
         * field for _return
         */


        protected EventStreamDefinitionDto local_return ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean local_returnTracker = false ;

        public boolean is_returnSpecified(){
            return local_returnTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamDefinitionDto
         */
        public  EventStreamDefinitionDto get_return(){
            return local_return;
        }



        /**
         * Auto generated setter method
         * @param param _return
         */
        public void set_return(EventStreamDefinitionDto param){
            local_returnTracker = true;

            this.local_return=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":convertStringToEventStreamDefinitionDtoResponse",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "convertStringToEventStreamDefinitionDtoResponse",
                            xmlWriter);
                }


            }
            if (local_returnTracker){
                if (local_return==null){

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org", "return", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();
                }else{
                    local_return.serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return"),
                            xmlWriter);
                }
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (local_returnTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "return"));


                elementList.add(local_return==null?null:
                        local_return);
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static ConvertStringToEventStreamDefinitionDtoResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                ConvertStringToEventStreamDefinitionDtoResponse object =
                        new ConvertStringToEventStreamDefinitionDtoResponse();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"convertStringToEventStreamDefinitionDtoResponse".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (ConvertStringToEventStreamDefinitionDtoResponse)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","return").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            object.set_return(null);
                            reader.next();

                            reader.next();

                        }else{

                            object.set_return(EventStreamDefinitionDto.Factory.parse(reader));

                            reader.next();
                        }
                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class AddEventStreamDefinitionAsString
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "addEventStreamDefinitionAsString",
                "ns31");



        /**
         * field for StreamStringDefinition
         */


        protected java.lang.String localStreamStringDefinition ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamStringDefinitionTracker = false ;

        public boolean isStreamStringDefinitionSpecified(){
            return localStreamStringDefinitionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamStringDefinition(){
            return localStreamStringDefinition;
        }



        /**
         * Auto generated setter method
         * @param param StreamStringDefinition
         */
        public void setStreamStringDefinition(java.lang.String param){
            localStreamStringDefinitionTracker = true;

            this.localStreamStringDefinition=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":addEventStreamDefinitionAsString",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "addEventStreamDefinitionAsString",
                            xmlWriter);
                }


            }
            if (localStreamStringDefinitionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "streamStringDefinition", xmlWriter);


                if (localStreamStringDefinition==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamStringDefinition);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localStreamStringDefinitionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "streamStringDefinition"));

                elementList.add(localStreamStringDefinition==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamStringDefinition));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static AddEventStreamDefinitionAsString parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                AddEventStreamDefinitionAsString object =
                        new AddEventStreamDefinitionAsString();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"addEventStreamDefinitionAsString".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (AddEventStreamDefinitionAsString)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","streamStringDefinition").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamStringDefinition(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class EventStreamDefinitionDto
            implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = EventStreamDefinitionDto
                Namespace URI = http://admin.stream.event.carbon.wso2.org/xsd
                Namespace Prefix = ns30
                */


        /**
         * field for CorrelationData
         * This was an Array!
         */


        protected EventStreamAttributeDto[] localCorrelationData ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localCorrelationDataTracker = false ;

        public boolean isCorrelationDataSpecified(){
            return localCorrelationDataTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamAttributeDto[]
         */
        public  EventStreamAttributeDto[] getCorrelationData(){
            return localCorrelationData;
        }






        /**
         * validate the array for CorrelationData
         */
        protected void validateCorrelationData(EventStreamAttributeDto[] param){

        }


        /**
         * Auto generated setter method
         * @param param CorrelationData
         */
        public void setCorrelationData(EventStreamAttributeDto[] param){

            validateCorrelationData(param);

            localCorrelationDataTracker = true;

            this.localCorrelationData=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param EventStreamAttributeDto
         */
        public void addCorrelationData(EventStreamAttributeDto param){
            if (localCorrelationData == null){
                localCorrelationData = new EventStreamAttributeDto[]{};
            }


            //update the setting tracker
            localCorrelationDataTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(localCorrelationData);
            list.add(param);
            this.localCorrelationData =
                    (EventStreamAttributeDto[])list.toArray(
                            new EventStreamAttributeDto[list.size()]);

        }


        /**
         * field for Description
         */


        protected java.lang.String localDescription ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localDescriptionTracker = false ;

        public boolean isDescriptionSpecified(){
            return localDescriptionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getDescription(){
            return localDescription;
        }



        /**
         * Auto generated setter method
         * @param param Description
         */
        public void setDescription(java.lang.String param){
            localDescriptionTracker = true;

            this.localDescription=param;


        }


        /**
         * field for Editable
         */


        protected boolean localEditable ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEditableTracker = false ;

        public boolean isEditableSpecified(){
            return localEditableTracker;
        }



        /**
         * Auto generated getter method
         * @return boolean
         */
        public  boolean getEditable(){
            return localEditable;
        }



        /**
         * Auto generated setter method
         * @param param Editable
         */
        public void setEditable(boolean param){

            // setting primitive attribute tracker to true
            localEditableTracker =
                    true;

            this.localEditable=param;


        }


        /**
         * field for MetaData
         * This was an Array!
         */


        protected EventStreamAttributeDto[] localMetaData ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localMetaDataTracker = false ;

        public boolean isMetaDataSpecified(){
            return localMetaDataTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamAttributeDto[]
         */
        public  EventStreamAttributeDto[] getMetaData(){
            return localMetaData;
        }






        /**
         * validate the array for MetaData
         */
        protected void validateMetaData(EventStreamAttributeDto[] param){

        }


        /**
         * Auto generated setter method
         * @param param MetaData
         */
        public void setMetaData(EventStreamAttributeDto[] param){

            validateMetaData(param);

            localMetaDataTracker = true;

            this.localMetaData=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param EventStreamAttributeDto
         */
        public void addMetaData(EventStreamAttributeDto param){
            if (localMetaData == null){
                localMetaData = new EventStreamAttributeDto[]{};
            }


            //update the setting tracker
            localMetaDataTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(localMetaData);
            list.add(param);
            this.localMetaData =
                    (EventStreamAttributeDto[])list.toArray(
                            new EventStreamAttributeDto[list.size()]);

        }


        /**
         * field for Name
         */


        protected java.lang.String localName ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localNameTracker = false ;

        public boolean isNameSpecified(){
            return localNameTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getName(){
            return localName;
        }



        /**
         * Auto generated setter method
         * @param param Name
         */
        public void setName(java.lang.String param){
            localNameTracker = true;

            this.localName=param;


        }


        /**
         * field for NickName
         */


        protected java.lang.String localNickName ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localNickNameTracker = false ;

        public boolean isNickNameSpecified(){
            return localNickNameTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getNickName(){
            return localNickName;
        }



        /**
         * Auto generated setter method
         * @param param NickName
         */
        public void setNickName(java.lang.String param){
            localNickNameTracker = true;

            this.localNickName=param;


        }


        /**
         * field for PayloadData
         * This was an Array!
         */


        protected EventStreamAttributeDto[] localPayloadData ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localPayloadDataTracker = false ;

        public boolean isPayloadDataSpecified(){
            return localPayloadDataTracker;
        }



        /**
         * Auto generated getter method
         * @return EventStreamAttributeDto[]
         */
        public  EventStreamAttributeDto[] getPayloadData(){
            return localPayloadData;
        }






        /**
         * validate the array for PayloadData
         */
        protected void validatePayloadData(EventStreamAttributeDto[] param){

        }


        /**
         * Auto generated setter method
         * @param param PayloadData
         */
        public void setPayloadData(EventStreamAttributeDto[] param){

            validatePayloadData(param);

            localPayloadDataTracker = true;

            this.localPayloadData=param;
        }



        /**
         * Auto generated add method for the array for convenience
         * @param param EventStreamAttributeDto
         */
        public void addPayloadData(EventStreamAttributeDto param){
            if (localPayloadData == null){
                localPayloadData = new EventStreamAttributeDto[]{};
            }


            //update the setting tracker
            localPayloadDataTracker = true;


            java.util.List list =
                    org.apache.axis2.databinding.utils.ConverterUtil.toList(localPayloadData);
            list.add(param);
            this.localPayloadData =
                    (EventStreamAttributeDto[])list.toArray(
                            new EventStreamAttributeDto[list.size()]);

        }


        /**
         * field for StreamDefinitionString
         */


        protected java.lang.String localStreamDefinitionString ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localStreamDefinitionStringTracker = false ;

        public boolean isStreamDefinitionStringSpecified(){
            return localStreamDefinitionStringTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getStreamDefinitionString(){
            return localStreamDefinitionString;
        }



        /**
         * Auto generated setter method
         * @param param StreamDefinitionString
         */
        public void setStreamDefinitionString(java.lang.String param){
            localStreamDefinitionStringTracker = true;

            this.localStreamDefinitionString=param;


        }


        /**
         * field for Version
         */


        protected java.lang.String localVersion ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localVersionTracker = false ;

        public boolean isVersionSpecified(){
            return localVersionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getVersion(){
            return localVersion;
        }



        /**
         * Auto generated setter method
         * @param param Version
         */
        public void setVersion(java.lang.String param){
            localVersionTracker = true;

            this.localVersion=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,parentQName);
            return factory.createOMElement(dataSource,parentQName);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org/xsd");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":EventStreamDefinitionDto",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "EventStreamDefinitionDto",
                            xmlWriter);
                }


            }
            if (localCorrelationDataTracker){
                if (localCorrelationData!=null){
                    for (int i = 0;i < localCorrelationData.length;i++){
                        if (localCorrelationData[i] != null){
                            localCorrelationData[i].serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","correlationData"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://admin.stream.event.carbon.wso2.org/xsd", "correlationData", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org/xsd", "correlationData", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            } if (localDescriptionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "description", xmlWriter);


                if (localDescription==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localDescription);

                }

                xmlWriter.writeEndElement();
            } if (localEditableTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "editable", xmlWriter);

                if (false) {

                    throw new org.apache.axis2.databinding.ADBException("editable cannot be null!!");

                } else {
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEditable));
                }

                xmlWriter.writeEndElement();
            } if (localMetaDataTracker){
                if (localMetaData!=null){
                    for (int i = 0;i < localMetaData.length;i++){
                        if (localMetaData[i] != null){
                            localMetaData[i].serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","metaData"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://admin.stream.event.carbon.wso2.org/xsd", "metaData", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org/xsd", "metaData", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            } if (localNameTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "name", xmlWriter);


                if (localName==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localName);

                }

                xmlWriter.writeEndElement();
            } if (localNickNameTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "nickName", xmlWriter);


                if (localNickName==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localNickName);

                }

                xmlWriter.writeEndElement();
            } if (localPayloadDataTracker){
                if (localPayloadData!=null){
                    for (int i = 0;i < localPayloadData.length;i++){
                        if (localPayloadData[i] != null){
                            localPayloadData[i].serialize(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","payloadData"),
                                    xmlWriter);
                        } else {

                            writeStartElement(null, "http://admin.stream.event.carbon.wso2.org/xsd", "payloadData", xmlWriter);

                            // write the nil attribute
                            writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                            xmlWriter.writeEndElement();

                        }

                    }
                } else {

                    writeStartElement(null, "http://admin.stream.event.carbon.wso2.org/xsd", "payloadData", xmlWriter);

                    // write the nil attribute
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);
                    xmlWriter.writeEndElement();

                }
            } if (localStreamDefinitionStringTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "streamDefinitionString", xmlWriter);


                if (localStreamDefinitionString==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localStreamDefinitionString);

                }

                xmlWriter.writeEndElement();
            } if (localVersionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org/xsd";
                writeStartElement(null, namespace, "version", xmlWriter);


                if (localVersion==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localVersion);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org/xsd")){
                return "ns30";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localCorrelationDataTracker){
                if (localCorrelationData!=null) {
                    for (int i = 0;i < localCorrelationData.length;i++){

                        if (localCorrelationData[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                                    "correlationData"));
                            elementList.add(localCorrelationData[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                                    "correlationData"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                            "correlationData"));
                    elementList.add(localCorrelationData);

                }

            } if (localDescriptionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "description"));

                elementList.add(localDescription==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDescription));
            } if (localEditableTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "editable"));

                elementList.add(
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEditable));
            } if (localMetaDataTracker){
                if (localMetaData!=null) {
                    for (int i = 0;i < localMetaData.length;i++){

                        if (localMetaData[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                                    "metaData"));
                            elementList.add(localMetaData[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                                    "metaData"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                            "metaData"));
                    elementList.add(localMetaData);

                }

            } if (localNameTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "name"));

                elementList.add(localName==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localName));
            } if (localNickNameTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "nickName"));

                elementList.add(localNickName==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localNickName));
            } if (localPayloadDataTracker){
                if (localPayloadData!=null) {
                    for (int i = 0;i < localPayloadData.length;i++){

                        if (localPayloadData[i] != null){
                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                                    "payloadData"));
                            elementList.add(localPayloadData[i]);
                        } else {

                            elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                                    "payloadData"));
                            elementList.add(null);

                        }

                    }
                } else {

                    elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                            "payloadData"));
                    elementList.add(localPayloadData);

                }

            } if (localStreamDefinitionStringTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "streamDefinitionString"));

                elementList.add(localStreamDefinitionString==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localStreamDefinitionString));
            } if (localVersionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd",
                        "version"));

                elementList.add(localVersion==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localVersion));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static EventStreamDefinitionDto parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                EventStreamDefinitionDto object =
                        new EventStreamDefinitionDto();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"EventStreamDefinitionDto".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (EventStreamDefinitionDto)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();

                    java.util.ArrayList list1 = new java.util.ArrayList();

                    java.util.ArrayList list4 = new java.util.ArrayList();

                    java.util.ArrayList list7 = new java.util.ArrayList();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","correlationData").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list1.add(null);
                            reader.next();
                        } else {
                            list1.add(EventStreamAttributeDto.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone1 = false;
                        while(!loopDone1){
                            // We should be at the end element, but make sure
                            while (!reader.isEndElement())
                                reader.next();
                            // Step out of this element
                            reader.next();
                            // Step to next element event.
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            if (reader.isEndElement()){
                                //two continuous end elements means we are exiting the xml structure
                                loopDone1 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","correlationData").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list1.add(null);
                                        reader.next();
                                    } else {
                                        list1.add(EventStreamAttributeDto.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone1 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.setCorrelationData((EventStreamAttributeDto[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        EventStreamAttributeDto.class,
                                        list1));

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","description").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setDescription(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","editable").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            throw new org.apache.axis2.databinding.ADBException("The element: "+"editable" +"  cannot be null");
                        }


                        java.lang.String content = reader.getElementText();

                        object.setEditable(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","metaData").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list4.add(null);
                            reader.next();
                        } else {
                            list4.add(EventStreamAttributeDto.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone4 = false;
                        while(!loopDone4){
                            // We should be at the end element, but make sure
                            while (!reader.isEndElement())
                                reader.next();
                            // Step out of this element
                            reader.next();
                            // Step to next element event.
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            if (reader.isEndElement()){
                                //two continuous end elements means we are exiting the xml structure
                                loopDone4 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","metaData").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list4.add(null);
                                        reader.next();
                                    } else {
                                        list4.add(EventStreamAttributeDto.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone4 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.setMetaData((EventStreamAttributeDto[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        EventStreamAttributeDto.class,
                                        list4));

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","name").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setName(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","nickName").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setNickName(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","payloadData").equals(reader.getName())){



                        // Process the array and step past its final element's end.

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                            list7.add(null);
                            reader.next();
                        } else {
                            list7.add(EventStreamAttributeDto.Factory.parse(reader));
                        }
                        //loop until we find a start element that is not part of this array
                        boolean loopDone7 = false;
                        while(!loopDone7){
                            // We should be at the end element, but make sure
                            while (!reader.isEndElement())
                                reader.next();
                            // Step out of this element
                            reader.next();
                            // Step to next element event.
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            if (reader.isEndElement()){
                                //two continuous end elements means we are exiting the xml structure
                                loopDone7 = true;
                            } else {
                                if (new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","payloadData").equals(reader.getName())){

                                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                                    if ("true".equals(nillableValue) || "1".equals(nillableValue)){
                                        list7.add(null);
                                        reader.next();
                                    } else {
                                        list7.add(EventStreamAttributeDto.Factory.parse(reader));
                                    }
                                }else{
                                    loopDone7 = true;
                                }
                            }
                        }
                        // call the converter utility  to convert and set the array

                        object.setPayloadData((EventStreamAttributeDto[])
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                        EventStreamAttributeDto.class,
                                        list7));

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","streamDefinitionString").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setStreamDefinitionString(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org/xsd","version").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setVersion(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    public static class RemoveEventStreamDefinition
            implements org.apache.axis2.databinding.ADBBean{

        public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                "http://admin.stream.event.carbon.wso2.org",
                "removeEventStreamDefinition",
                "ns31");



        /**
         * field for EventStreamName
         */


        protected java.lang.String localEventStreamName ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEventStreamNameTracker = false ;

        public boolean isEventStreamNameSpecified(){
            return localEventStreamNameTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getEventStreamName(){
            return localEventStreamName;
        }



        /**
         * Auto generated setter method
         * @param param EventStreamName
         */
        public void setEventStreamName(java.lang.String param){
            localEventStreamNameTracker = true;

            this.localEventStreamName=param;


        }


        /**
         * field for EventStreamVersion
         */


        protected java.lang.String localEventStreamVersion ;

        /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
        protected boolean localEventStreamVersionTracker = false ;

        public boolean isEventStreamVersionSpecified(){
            return localEventStreamVersionTracker;
        }



        /**
         * Auto generated getter method
         * @return java.lang.String
         */
        public  java.lang.String getEventStreamVersion(){
            return localEventStreamVersion;
        }



        /**
         * Auto generated setter method
         * @param param EventStreamVersion
         */
        public void setEventStreamVersion(java.lang.String param){
            localEventStreamVersionTracker = true;

            this.localEventStreamVersion=param;


        }




        /**
         *
         * @param parentQName
         * @param factory
         * @return org.apache.axiom.om.OMElement
         */
        public org.apache.axiom.om.OMElement getOMElement (
                final javax.xml.namespace.QName parentQName,
                final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{



            org.apache.axiom.om.OMDataSource dataSource =
                    new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME);
            return factory.createOMElement(dataSource,MY_QNAME);

        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            serialize(parentQName,xmlWriter,false);
        }

        public void serialize(final javax.xml.namespace.QName parentQName,
                              javax.xml.stream.XMLStreamWriter xmlWriter,
                              boolean serializeType)
                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{




            java.lang.String prefix = null;
            java.lang.String namespace = null;


            prefix = parentQName.getPrefix();
            namespace = parentQName.getNamespaceURI();
            writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

            if (serializeType){


                java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://admin.stream.event.carbon.wso2.org");
                if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            namespacePrefix+":removeEventStreamDefinition",
                            xmlWriter);
                } else {
                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                            "removeEventStreamDefinition",
                            xmlWriter);
                }


            }
            if (localEventStreamNameTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "eventStreamName", xmlWriter);


                if (localEventStreamName==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localEventStreamName);

                }

                xmlWriter.writeEndElement();
            } if (localEventStreamVersionTracker){
                namespace = "http://admin.stream.event.carbon.wso2.org";
                writeStartElement(null, namespace, "eventStreamVersion", xmlWriter);


                if (localEventStreamVersion==null){
                    // write the nil attribute

                    writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","nil","1",xmlWriter);

                }else{


                    xmlWriter.writeCharacters(localEventStreamVersion);

                }

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();


        }

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://admin.stream.event.carbon.wso2.org")){
                return "ns31";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        /**
         * Utility method to write an element start tag.
         */
        private void writeStartElement(java.lang.String prefix, java.lang.String namespace, java.lang.String localPart,
                                       javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null) {
                xmlWriter.writeStartElement(namespace, localPart);
            } else {
                if (namespace.length() == 0) {
                    prefix = "";
                } else if (prefix == null) {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, localPart, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }

        /**
         * Util method to write an attribute with the ns prefix
         */
        private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (xmlWriter.getPrefix(namespace) == null) {
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            xmlWriter.writeAttribute(namespace,attName,attValue);
        }

        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                    java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName,attValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace,attName,attValue);
            }
        }


        /**
         * Util method to write an attribute without the ns prefix
         */
        private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                         javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            java.lang.String attributeNamespace = qname.getNamespaceURI();
            java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
            if (attributePrefix == null) {
                attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
            }
            java.lang.String attributeValue;
            if (attributePrefix.trim().length() > 0) {
                attributeValue = attributePrefix + ":" + qname.getLocalPart();
            } else {
                attributeValue = qname.getLocalPart();
            }

            if (namespace.equals("")) {
                xmlWriter.writeAttribute(attName, attributeValue);
            } else {
                registerPrefix(xmlWriter, namespace);
                xmlWriter.writeAttribute(namespace, attName, attributeValue);
            }
        }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


        /**
         * Register a namespace prefix
         */
        private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null) {
                prefix = generatePrefix(namespace);
                while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                    prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                }
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            return prefix;
        }



        /**
         * databinding method to get an XML representation of this object
         *
         */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                throws org.apache.axis2.databinding.ADBException{



            java.util.ArrayList elementList = new java.util.ArrayList();
            java.util.ArrayList attribList = new java.util.ArrayList();

            if (localEventStreamNameTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "eventStreamName"));

                elementList.add(localEventStreamName==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEventStreamName));
            } if (localEventStreamVersionTracker){
                elementList.add(new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org",
                        "eventStreamVersion"));

                elementList.add(localEventStreamVersion==null?null:
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localEventStreamVersion));
            }

            return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());



        }



        /**
         *  Factory class that keeps the parse method
         */
        public static class Factory{




            /**
             * static method to create the object
             * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
             *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
             * Postcondition: If this object is an element, the reader is positioned at its end element
             *                If this object is a complex type, the reader is positioned at the end element of its outer element
             */
            public static RemoveEventStreamDefinition parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
                RemoveEventStreamDefinition object =
                        new RemoveEventStreamDefinition();

                int event;
                java.lang.String nillableValue = null;
                java.lang.String prefix ="";
                java.lang.String namespaceuri ="";
                try {

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();


                    if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                        java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                                "type");
                        if (fullTypeName!=null){
                            java.lang.String nsPrefix = null;
                            if (fullTypeName.indexOf(":") > -1){
                                nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                            }
                            nsPrefix = nsPrefix==null?"":nsPrefix;

                            java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);

                            if (!"removeEventStreamDefinition".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (RemoveEventStreamDefinition)ExtensionMapper.getTypeObject(
                                        nsUri,type,reader);
                            }


                        }


                    }




                    // Note all attributes that were handled. Used to differ normal attributes
                    // from anyAttributes.
                    java.util.Vector handledAttributes = new java.util.Vector();




                    reader.next();


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamName").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setEventStreamName(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }


                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();

                    if (reader.isStartElement() && new javax.xml.namespace.QName("http://admin.stream.event.carbon.wso2.org","eventStreamVersion").equals(reader.getName())){

                        nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","nil");
                        if (!"true".equals(nillableValue) && !"1".equals(nillableValue)){


                            java.lang.String content = reader.getElementText();

                            object.setEventStreamVersion(
                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                        } else {


                            reader.getElementText(); // throw away text nodes if any.
                        }

                        reader.next();

                    }  // End of if for expected property start element

                    else {

                    }

                    while (!reader.isStartElement() && !reader.isEndElement())
                        reader.next();

                    if (reader.isStartElement())
                        // A start element we are not expecting indicates a trailing invalid property
                        throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getName());




                } catch (javax.xml.stream.XMLStreamException e) {
                    throw new java.lang.Exception(e);
                }

                return object;
            }

        }//end of factory class



    }


    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDto param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDto.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDtoResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDtoResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDto param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDto.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDtoResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDtoResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToString param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToString.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToStringResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToStringResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsString param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsString.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsStringResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsStringResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsString param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsString.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsStringResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsStringResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinition param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinition.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinitionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinitionResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDto param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDto.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDtoResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDtoResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDto param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDto.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDtoResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDtoResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNamesResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNamesResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEvent param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEvent.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }

    private  org.apache.axiom.om.OMElement  toOM(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEventResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {


        try{
            return param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEventResponse.MY_QNAME,
                    org.apache.axiom.om.OMAbstractFactory.getOMFactory());
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }


    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDto param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDto.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDto param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDto.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToString param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToString.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsString param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsString.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsString param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsString.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinition param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinition.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDto param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDto.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDto param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDto.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */



    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEvent param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
            throws org.apache.axis2.AxisFault{


        try{

            org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEvent.MY_QNAME,factory));
            return emptyEnvelope;
        } catch(org.apache.axis2.databinding.ADBException e){
            throw org.apache.axis2.AxisFault.makeFault(e);
        }


    }
                                
                             
                             /* methods to provide back word compatibility */




    /**
     *  get the default envelope
     */
    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
    }


    private  java.lang.Object fromOM(
            org.apache.axiom.om.OMElement param,
            java.lang.Class type,
            java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDto.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDto.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDtoResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsDtoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDto.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDto.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDtoResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionDtoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToString.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToString.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToStringResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertEventStreamDefinitionDtoToStringResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsString.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsString.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsStringResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsStringResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsString.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsString.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsStringResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDefinitionAsStringResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDto.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetAllEventStreamDefinitionDtoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinition.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinition.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinitionResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.RemoveEventStreamDefinitionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsString.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.AddEventStreamDefinitionAsStringResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamId.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamDetailsForStreamIdResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDto.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDto.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDtoResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.EditEventStreamDefinitionAsDtoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDto.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDto.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDtoResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.ConvertStringToEventStreamDefinitionDtoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNames.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNamesResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GetStreamNamesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEvent.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEvent.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

            if (org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEventResponse.class.equals(type)){

                return org.wso2.carbon.pc.analytics.config.stubs.EventStreamAdminServiceStub.GenerateSampleEventResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());


            }

        } catch (java.lang.Exception e) {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }




}
   