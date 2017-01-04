
/**
 * TerminalServiceXmlServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package net.wit.webservice;

    /**
     *  TerminalServiceXmlServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class TerminalServiceXmlServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public TerminalServiceXmlServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public TerminalServiceXmlServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for directOrderStateQuery method
            * override this method for handling normal response from directOrderStateQuery operation
            */
           public void receiveResultdirectOrderStateQuery(
                    net.wit.webservice.TerminalServiceXmlServiceStub.DirectOrderStateQueryResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from directOrderStateQuery operation
           */
            public void receiveErrordirectOrderStateQuery(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTerminalCardType method
            * override this method for handling normal response from getTerminalCardType operation
            */
           public void receiveResultgetTerminalCardType(
                    net.wit.webservice.TerminalServiceXmlServiceStub.GetTerminalCardTypeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTerminalCardType operation
           */
            public void receiveErrorgetTerminalCardType(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for terminalReturnCard method
            * override this method for handling normal response from terminalReturnCard operation
            */
           public void receiveResultterminalReturnCard(
                    net.wit.webservice.TerminalServiceXmlServiceStub.TerminalReturnCardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from terminalReturnCard operation
           */
            public void receiveErrorterminalReturnCard(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for directQuery method
            * override this method for handling normal response from directQuery operation
            */
           public void receiveResultdirectQuery(
                    net.wit.webservice.TerminalServiceXmlServiceStub.DirectQueryResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from directQuery operation
           */
            public void receiveErrordirectQuery(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for qqCharge method
            * override this method for handling normal response from qqCharge operation
            */
           public void receiveResultqqCharge(
                    net.wit.webservice.TerminalServiceXmlServiceStub.QqChargeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from qqCharge operation
           */
            public void receiveErrorqqCharge(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDirectSrvInfo method
            * override this method for handling normal response from getDirectSrvInfo operation
            */
           public void receiveResultgetDirectSrvInfo(
                    net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectSrvInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDirectSrvInfo operation
           */
            public void receiveErrorgetDirectSrvInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for terminalDownloadQueryForDay method
            * override this method for handling normal response from terminalDownloadQueryForDay operation
            */
           public void receiveResultterminalDownloadQueryForDay(
                    net.wit.webservice.TerminalServiceXmlServiceStub.TerminalDownloadQueryForDayResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from terminalDownloadQueryForDay operation
           */
            public void receiveErrorterminalDownloadQueryForDay(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for directCharge method
            * override this method for handling normal response from directCharge operation
            */
           public void receiveResultdirectCharge(
                    net.wit.webservice.TerminalServiceXmlServiceStub.DirectChargeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from directCharge operation
           */
            public void receiveErrordirectCharge(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDirectAreaInfo method
            * override this method for handling normal response from getDirectAreaInfo operation
            */
           public void receiveResultgetDirectAreaInfo(
                    net.wit.webservice.TerminalServiceXmlServiceStub.GetDirectAreaInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDirectAreaInfo operation
           */
            public void receiveErrorgetDirectAreaInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for terminalDownloadQueryForMonth method
            * override this method for handling normal response from terminalDownloadQueryForMonth operation
            */
           public void receiveResultterminalDownloadQueryForMonth(
                    net.wit.webservice.TerminalServiceXmlServiceStub.TerminalDownloadQueryForMonthResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from terminalDownloadQueryForMonth operation
           */
            public void receiveErrorterminalDownloadQueryForMonth(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getDownLoadCard method
            * override this method for handling normal response from getDownLoadCard operation
            */
           public void receiveResultgetDownLoadCard(
                    net.wit.webservice.TerminalServiceXmlServiceStub.GetDownLoadCardResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getDownLoadCard operation
           */
            public void receiveErrorgetDownLoadCard(java.lang.Exception e) {
            }
                


    }
    