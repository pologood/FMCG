
/**
 * WebService1CallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package net.wit.webservice;

    /**
     *  WebService1CallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class xxkyServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public xxkyServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public xxkyServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for selectHuoKuanOutState method
            * override this method for handling normal response from selectHuoKuanOutState operation
            */
           public void receiveResultselectHuoKuanOutState(
                    net.wit.webservice.xxkyServiceStub.SelectHuoKuanOutStateResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from selectHuoKuanOutState operation
           */
            public void receiveErrorselectHuoKuanOutState(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for selectOrderTraceInfo method
            * override this method for handling normal response from selectOrderTraceInfo operation
            */
           public void receiveResultselectOrderTraceInfo(
                    net.wit.webservice.xxkyServiceStub.SelectOrderTraceInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from selectOrderTraceInfo operation
           */
            public void receiveErrorselectOrderTraceInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for selectHuoKuanArrivedInfo method
            * override this method for handling normal response from selectHuoKuanArrivedInfo operation
            */
           public void receiveResultselectHuoKuanArrivedInfo(
                    net.wit.webservice.xxkyServiceStub.SelectHuoKuanArrivedInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from selectHuoKuanArrivedInfo operation
           */
            public void receiveErrorselectHuoKuanArrivedInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for huoKuanApp method
            * override this method for handling normal response from huoKuanApp operation
            */
           public void receiveResulthuoKuanApp(
                    net.wit.webservice.xxkyServiceStub.HuoKuanAppResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from huoKuanApp operation
           */
            public void receiveErrorhuoKuanApp(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for add_WEB_ORDERENTER method
            * override this method for handling normal response from add_WEB_ORDERENTER operation
            */
           public void receiveResultadd_WEB_ORDERENTER(
                    net.wit.webservice.xxkyServiceStub.Add_WEB_ORDERENTERResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from add_WEB_ORDERENTER operation
           */
            public void receiveErroradd_WEB_ORDERENTER(java.lang.Exception e) {
            }
                


    }
    