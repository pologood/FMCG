
/**
 * EpayDsfServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package net.wit.webservice;

    /**
     *  EpayDsfServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class EpayDsfServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public EpayDsfServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public EpayDsfServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for batchAuthCharge method
            * override this method for handling normal response from batchAuthCharge operation
            */
           public void receiveResultbatchAuthCharge(
                    net.wit.webservice.EpayDsfServiceStub.BatchAuthChargeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from batchAuthCharge operation
           */
            public void receiveErrorbatchAuthCharge(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for authCharge method
            * override this method for handling normal response from authCharge operation
            */
           public void receiveResultauthCharge(
                    net.wit.webservice.EpayDsfServiceStub.AuthChargeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from authCharge operation
           */
            public void receiveErrorauthCharge(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for batchPayTo method
            * override this method for handling normal response from batchPayTo operation
            */
           public void receiveResultbatchPayTo(
                    net.wit.webservice.EpayDsfServiceStub.BatchPayToResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from batchPayTo operation
           */
            public void receiveErrorbatchPayTo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for signConfirm method
            * override this method for handling normal response from signConfirm operation
            */
           public void receiveResultsignConfirm(
                    net.wit.webservice.EpayDsfServiceStub.SignConfirmResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from signConfirm operation
           */
            public void receiveErrorsignConfirm(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for qrySignStatus method
            * override this method for handling normal response from qrySignStatus operation
            */
           public void receiveResultqrySignStatus(
                    net.wit.webservice.EpayDsfServiceStub.QrySignStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from qrySignStatus operation
           */
            public void receiveErrorqrySignStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for qryServiceFee method
            * override this method for handling normal response from qryServiceFee operation
            */
           public void receiveResultqryServiceFee(
                    net.wit.webservice.EpayDsfServiceStub.QryServiceFeeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from qryServiceFee operation
           */
            public void receiveErrorqryServiceFee(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for charge method
            * override this method for handling normal response from charge operation
            */
           public void receiveResultcharge(
                    net.wit.webservice.EpayDsfServiceStub.ChargeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from charge operation
           */
            public void receiveErrorcharge(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for payTo method
            * override this method for handling normal response from payTo operation
            */
           public void receiveResultpayTo(
                    net.wit.webservice.EpayDsfServiceStub.PayToResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from payTo operation
           */
            public void receiveErrorpayTo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for qryAuthAccInfo method
            * override this method for handling normal response from qryAuthAccInfo operation
            */
           public void receiveResultqryAuthAccInfo(
                    net.wit.webservice.EpayDsfServiceStub.QryAuthAccInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from qryAuthAccInfo operation
           */
            public void receiveErrorqryAuthAccInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for cancelCharge method
            * override this method for handling normal response from cancelCharge operation
            */
           public void receiveResultcancelCharge(
                    net.wit.webservice.EpayDsfServiceStub.CancelChargeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from cancelCharge operation
           */
            public void receiveErrorcancelCharge(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for payToBank method
            * override this method for handling normal response from payToBank operation
            */
           public void receiveResultpayToBank(
                    net.wit.webservice.EpayDsfServiceStub.PayToBankResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from payToBank operation
           */
            public void receiveErrorpayToBank(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for batchPayToBank method
            * override this method for handling normal response from batchPayToBank operation
            */
           public void receiveResultbatchPayToBank(
                    net.wit.webservice.EpayDsfServiceStub.BatchPayToBankResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from batchPayToBank operation
           */
            public void receiveErrorbatchPayToBank(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for pay method
            * override this method for handling normal response from pay operation
            */
           public void receiveResultpay(
                    net.wit.webservice.EpayDsfServiceStub.PayResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from pay operation
           */
            public void receiveErrorpay(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryDetail method
            * override this method for handling normal response from queryDetail operation
            */
           public void receiveResultqueryDetail(
                    net.wit.webservice.EpayDsfServiceStub.QueryDetailResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryDetail operation
           */
            public void receiveErrorqueryDetail(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for daiFu method
            * override this method for handling normal response from daiFu operation
            */
           public void receiveResultdaiFu(
                    net.wit.webservice.EpayDsfServiceStub.DaiFuResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from daiFu operation
           */
            public void receiveErrordaiFu(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for daiShou method
            * override this method for handling normal response from daiShou operation
            */
           public void receiveResultdaiShou(
                    net.wit.webservice.EpayDsfServiceStub.DaiShouResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from daiShou operation
           */
            public void receiveErrordaiShou(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for qryAccCurrFee method
            * override this method for handling normal response from qryAccCurrFee operation
            */
           public void receiveResultqryAccCurrFee(
                    net.wit.webservice.EpayDsfServiceStub.QryAccCurrFeeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from qryAccCurrFee operation
           */
            public void receiveErrorqryAccCurrFee(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for queryResult method
            * override this method for handling normal response from queryResult operation
            */
           public void receiveResultqueryResult(
                    net.wit.webservice.EpayDsfServiceStub.QueryResultResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from queryResult operation
           */
            public void receiveErrorqueryResult(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for qryBindAccInfo method
            * override this method for handling normal response from qryBindAccInfo operation
            */
           public void receiveResultqryBindAccInfo(
                    net.wit.webservice.EpayDsfServiceStub.QryBindAccInfoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from qryBindAccInfo operation
           */
            public void receiveErrorqryBindAccInfo(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for bindBankAcc method
            * override this method for handling normal response from bindBankAcc operation
            */
           public void receiveResultbindBankAcc(
                    net.wit.webservice.EpayDsfServiceStub.BindBankAccResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from bindBankAcc operation
           */
            public void receiveErrorbindBankAcc(java.lang.Exception e) {
            }
                


    }
    